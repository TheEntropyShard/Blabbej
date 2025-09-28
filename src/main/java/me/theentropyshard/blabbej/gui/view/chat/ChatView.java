/*
 * Blabbej - https://github.com/TheEntropyShard/Blabbej
 * Copyright (C) 2025 TheEntropyShard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package me.theentropyshard.blabbej.gui.view.chat;

import net.miginfocom.swing.MigLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import me.theentropyshard.blabbej.Message;
import me.theentropyshard.blabbej.Parser;
import me.theentropyshard.blabbej.gui.FlatSmoothScrollPaneUI;
import me.theentropyshard.blabbej.utils.FileUtils;

public class ChatView extends JPanel {
    private static final Logger LOG = LogManager.getLogger(ChatView.class);

    private static final String ROOM_NAME = System.getenv("BLABBER_ROOM") == null ?
        "general" : System.getenv("BLABBER_ROOM");

    private final JPanel messagesPanel;
    private final JScrollPane scrollPane;

    public ChatView() {
        this.messagesPanel = new JPanel();
        this.messagesPanel.setLayout(new MigLayout("insets 0, fillx, flowy"));

        this.scrollPane = new JScrollPane(
            this.messagesPanel,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        this.scrollPane.setUI(new FlatSmoothScrollPaneUI());

        this.setLayout(new MigLayout("fill", "[]", "[center][bottom]"));

        this.add(this.scrollPane, "grow, wrap");
        this.add(new ChatInput(
            () -> {
                this.fetch("");
            },
            message -> {
                this.fetch(message);

                return null;
            }
        ), "growx");

        this.loadFromFile();
    }

    private void scrollDown() {
        JScrollBar scrollBar = this.scrollPane.getVerticalScrollBar();
        scrollBar.setValue(scrollBar.getMaximum());
    }

    private void loadFromFile() {
        new SwingWorker<List<Message>, Void>() {
            @Override
            protected List<Message> doInBackground() {
                try {
                    Path general = Path.of("chats", ChatView.ROOM_NAME);

                    if (Files.exists(general)) {
                        return Parser.parse(Files.readString(general, StandardCharsets.UTF_8));
                    }
                } catch (IOException e) {
                    ChatView.LOG.error("Could not save messages to chatfile", e);
                }

                return Collections.emptyList();
            }

            @Override
            protected void done() {
                List<Message> messages;

                try {
                    messages = this.get();
                } catch (InterruptedException | ExecutionException e) {
                    ChatView.LOG.error("Could not get messages from worker", e);

                    return;
                }

                for (Message message : messages) {
                    ChatView.this.addMessage(message);
                }

                ChatView.this.revalidate();
                SwingUtilities.invokeLater(ChatView.this::scrollDown);
            }
        }.execute();
    }

    private void fetch(String message) {
        new SwingWorker<List<Message>, Void>() {
            @Override
            protected List<Message> doInBackground() {
                try {
                    Path general = Path.of("chats", ChatView.ROOM_NAME).toAbsolutePath();

                    long offset = 0;

                    if (Files.exists(general)) {
                        offset = Files.size(general);
                    }

                    String messages = ChatView.getMessages(offset, message);

                    if (messages == null) {
                        return Collections.emptyList();
                    }

                    FileUtils.createFileIfNotExists(general);
                    Files.writeString(general, messages, StandardCharsets.UTF_8, StandardOpenOption.APPEND);

                    return Parser.parse(messages);
                } catch (IOException e) {
                    ChatView.LOG.error("Could not save messages to chatfile", e);
                }

                return Collections.emptyList();
            }

            @Override
            protected void done() {
                List<Message> messages;

                try {
                    messages = this.get();
                } catch (InterruptedException | ExecutionException e) {
                    ChatView.LOG.error("Could not get messages from worker", e);

                    return;
                }

                for (Message message : messages) {
                    ChatView.this.addMessage(message);
                }

                ChatView.this.revalidate();
                SwingUtilities.invokeLater(ChatView.this::scrollDown);
            }
        }.execute();
    }

    private static String getMessages(long offset, String message) {
        try {
            String credentials = new String(
                Base64.getEncoder().encode(System.getenv("BLABBER_CREDS").getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);

            HttpClient client = HttpClient.newBuilder().build();

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(System.getenv("BLABBER_SERVER") + "/rooms/" + ChatView.ROOM_NAME))
                .version(HttpClient.Version.HTTP_1_1)
                .header("Authorization", "Basic " + credentials)
                .header("Range", "bytes=" + offset + "-")
                .POST(HttpRequest.BodyPublishers.ofString(message))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString(StandardCharsets.UTF_8));

            int code = response.statusCode();

            if (code / 100 != 2) {
                ChatView.LOG.error("Server returned code {}", code);

                return null;
            }

            return response.body();
        } catch (IOException | InterruptedException e) {
            ChatView.LOG.error("Could not load messages", e);
        }

        return null;
    }

    public void addMessage(Message message) {
        this.messagesPanel.add(new MessageComponent(message), "wmin 150, wmax 75%");
    }
}

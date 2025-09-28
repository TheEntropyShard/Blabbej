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

package me.theentropyshard.blabbej;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

import me.theentropyshard.blabbej.utils.FileUtils;

public class ChatManager {
    private static final Logger LOG = LogManager.getLogger(ChatManager.class);

    private final Path workDir;

    private final Set<ChatManagerListener> listeners;
    private final Map<String, List<ChatFile>> chatFiles;

    public ChatManager(Path workDir) {
        this.workDir = workDir;

        this.listeners = new HashSet<>();
        this.chatFiles = new HashMap<>();

        /*try {
            this.load();
        } catch (IOException e) {
            ChatManager.LOG.error("Could not load chats", e);
        }*/
    }

    public void load() throws IOException {
        FileUtils.createDirectoryIfNotExists(this.workDir);

        List<Path> hosts = FileUtils.list(this.workDir);

        for (Path host : hosts) {
            List<Path> chats = FileUtils.list(host);

            for (Path chat : chats) {
                String server = host.getFileName().toString();

                this.chatFiles
                    .computeIfAbsent(server, k -> new ArrayList<>())
                    .add(new ChatFile(server, chat.getFileName().toString()));
            }
        }
    }

    public Path create(String server, String name) {
        try {
            Path filePath = this.workDir.resolve(server).resolve(name);

            FileUtils.createFileIfNotExists(filePath);

            this.chatFiles
                .computeIfAbsent(server, k -> new ArrayList<>())
                .add(new ChatFile(server, name));

            this.listeners.forEach(listener -> listener.onChatCreated(name));

            return filePath;
        } catch (IOException e) {
            ChatManager.LOG.error("Could not create chat <{}>", name, e);

            return null;
        }
    }

    public void remove(String server, String name) {
        try {
            FileUtils.delete(this.workDir.resolve(server).resolve(name));

            if (this.chatFiles.containsKey(server)) {
                List<ChatFile> list = this.chatFiles.get(server);

                list.removeIf(c -> c.getName().equals(name));
            }

            this.listeners.forEach(listener -> listener.onChatRemoved(name));
        } catch (IOException e) {
            ChatManager.LOG.error("Could not remove chat <{}>", name, e);
        }
    }

    public void addListener(ChatManagerListener listener) {
        this.listeners.add(listener);
    }

    public interface ChatManagerListener {
        void onChatCreated(String name);

        void onChatRemoved(String name);
    }

    public Map<String, List<ChatFile>> getChatFiles() {
        return this.chatFiles;
    }
}

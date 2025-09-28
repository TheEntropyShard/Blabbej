/*
 * CRLauncher - https://github.com/TheEntropyShard/Blabbej
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

package me.theentropyshard.blabbej.gui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatSplitPaneUI;

import javax.swing.*;
import java.awt.*;

import me.theentropyshard.blabbej.Blabbej;
import me.theentropyshard.blabbej.gui.laf.LightBlabbejLaf;
import me.theentropyshard.blabbej.gui.view.chat.ChatList;
import me.theentropyshard.blabbej.gui.view.chat.ChatListItem;
import me.theentropyshard.blabbej.gui.view.chat.ChatView;
import me.theentropyshard.blabbej.utils.SwingUtils;

public class Gui {
    public Gui() {
        JDialog.setDefaultLookAndFeelDecorated(true);
        JFrame.setDefaultLookAndFeelDecorated(true);

        FlatLaf.registerCustomDefaultsSource("themes");

        LightBlabbejLaf.setup();

        JSplitPane root = new JSplitPane();
        root.setDividerLocation((int) (960 * 0.25));
        root.setUI(new FlatSplitPaneUI());
        root.setPreferredSize(new Dimension(960, 540));

        JPanel leftPanel = new JPanel(new BorderLayout());
        root.setLeftComponent(leftPanel);

        JPanel rightPanel = new JPanel(new BorderLayout());
        root.setRightComponent(rightPanel);

        ChatList chatList = new ChatList();
        Blabbej.getInstance().getChatManager().addListener(chatList);

        Blabbej.getInstance().getChatManager().getChatFiles().forEach((server, chats) -> {
            if (server.equals(Blabbej.getInstance().getSettings().currentServer)) {
                chats.forEach(chatFile -> {
                    ChatListItem item = new ChatListItem();
                    item.setName(chatFile.getName());

                    chatList.addChat(item);
                });
            }
        });

        leftPanel.add(chatList);

        ChatView chatView = new ChatView();
        rightPanel.add(chatView);

        JFrame frame = new JFrame("Blabbej");

        JMenuBar menuBar = new JMenuBar();

        JMenu serversMenu = new JMenu("Servers");
        JMenuItem noServersItem = new JMenuItem("No servers");
        noServersItem.setEnabled(false);
        serversMenu.add(noServersItem);
        serversMenu.addSeparator();
        JMenuItem manageServers = new JMenuItem("Manage servers");
        manageServers.addActionListener(e -> {
            SwingUtils.runWorker(() -> {

            });
        });
        serversMenu.add(manageServers);
        menuBar.add(serversMenu);

        JMenu accountsMenu = new JMenu("Accounts");
        JMenuItem noAccountsItem = new JMenuItem("No accounts");
        noAccountsItem.setEnabled(false);
        accountsMenu.add(noAccountsItem);
        accountsMenu.addSeparator();
        JMenuItem manageAccounts = new JMenuItem("Manage accounts");
        manageAccounts.addActionListener(e -> {
            SwingUtils.runWorker(() -> {

            });
        });
        accountsMenu.add(manageAccounts);
        menuBar.add(accountsMenu);

        JButton button = new JButton("Add chat");
        button.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
        button.addActionListener(e -> {
            SwingUtils.runWorker(() -> {
                String currentServer = Blabbej.getInstance().getSettings().currentServer;

                if (currentServer == null || currentServer.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(
                        frame,
                        "<html>Please add a server in the <b>Servers</b> menu first</html>",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                    );

                    return;
                }

                String chatName = JOptionPane.showInputDialog(frame, "Enter a chat name");

                if (chatName == null || chatName.trim().isEmpty()) {
                    return;
                }

                Blabbej.getInstance().getChatManager().create(currentServer, chatName);
            });
        });
        button.setFocusPainted(false);
        menuBar.add(button);

        frame.setJMenuBar(menuBar);

        frame.add(root);
        frame.pack();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}

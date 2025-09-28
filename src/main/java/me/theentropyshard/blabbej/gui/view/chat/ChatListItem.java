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

package me.theentropyshard.blabbej.gui.view.chat;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class ChatListItem extends JPanel {
    private final JLabel nameLabel;
    private final JLabel timeLabel;
    private final JLabel messageLabel;

    private String rawName;

    public ChatListItem() {
        this.setLayout(new MigLayout("fillx", "[]", "[][]"));
        this.setOpaque(false);
        this.setForeground(UIManager.getColor("onSecondaryContainer"));
        this.setBackground(UIManager.getColor("secondaryContainer"));

        this.nameLabel = new JLabel();
        this.add(this.nameLabel, "push");

        this.timeLabel = new JLabel();
        this.add(this.timeLabel, "wrap");

        this.messageLabel = new JLabel();
        this.add(this.messageLabel, "w 0:0:n, growx");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g.setColor(this.getBackground());
        g2d.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 16, 16);

        super.paintComponent(g);
    }

    public String getRawName() {
        return this.rawName;
    }

    public void setName(String name) {
        this.rawName = name;

        this.nameLabel.setText("<html><b>" + name + "</b></html>");
    }

    public void setTime(String time) {
        this.timeLabel.setText(time);
    }

    public void setMessage(String message) {
        this.messageLabel.setText(message);
    }
}

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

import me.theentropyshard.blabbej.Message;

public class MessageComponent extends JPanel {
    public MessageComponent(Message message) {
        super(new MigLayout("fill", "[]", "[][]"));

        this.setForeground(UIManager.getColor("onSecondaryContainer"));
        this.setBackground(UIManager.getColor("secondaryContainer"));

        this.setOpaque(false);

        JPanel metaInfoPanel = new JPanel(new MigLayout("fillx, insets 0", "[]20 push[]", "[]"));
        metaInfoPanel.setOpaque(false);
        metaInfoPanel.add(new JLabel(message.getAuthor()));
        metaInfoPanel.add(new JLabel(message.getDatetime()));
        this.add(metaInfoPanel, "growx, wrap");

        JTextPane pane = new JTextPane() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(this.getBackground());
                g2d.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 16, 16);

                super.paintComponent(g);
            }
        };
        pane.setOpaque(false);
        pane.setForeground(UIManager.getColor("onSurface"));
        pane.setBackground(UIManager.getColor("surface"));
        pane.setEditable(false);
        pane.setText(message.getText());
        this.add(pane, "grow");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(this.getBackground());
        g2d.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 16, 16);

        super.paintComponent(g2d);
    }
}

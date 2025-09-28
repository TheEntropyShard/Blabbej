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

package me.theentropyshard.blabbej.gui.view.entrance;

import com.formdev.flatlaf.FlatClientProperties;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class EntranceView extends JPanel {
    private final JTextField instanceField;

    public EntranceView() {
        this.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        JPanel panel = new JPanel(new MigLayout("fillx, insets 24 32 32 32")) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2d.setColor(UIManager.getColor("secondaryContainer"));
                g2d.fillRoundRect(0, 0, this.getWidth() - 1, this.getHeight() - 1, 16, 16);

                g2d.setColor(UIManager.getColor("background"));
                g2d.fillRoundRect(8, 8, this.getWidth() - 17, this.getHeight() - 17, 8, 8);

                super.paintComponent(g2d);
            }
        };
        panel.setOpaque(false);

        JLabel label = new JLabel("<html><b>Entrance</b></html>");
        label.setFont(label.getFont().deriveFont(24f));
        panel.add(label, "wrap, al 50% 50%");

        this.instanceField = new JTextField();
        this.instanceField.putClientProperty(FlatClientProperties.STYLE, "arc: 16");
        this.instanceField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Enter Blabber server URL");
        panel.add(this.instanceField, "wmin 320px, hmin 32px, growx, wrap 16px");

        JButton button = new JButton("<html><b>Proceed</b></html>");
        button.putClientProperty(FlatClientProperties.STYLE, "arc: 16; background: $primary; foreground: $onPrimary");
        button.addActionListener(this::onLoginButtonClicked);
        panel.add(button, "hmin 32px, growx");

        this.add(panel, gbc);
    }

    private void onLoginButtonClicked(ActionEvent e) {

    }
}

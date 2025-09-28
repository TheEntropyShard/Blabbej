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

package me.theentropyshard.blabbej;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;

import me.theentropyshard.blabbej.gui.Gui;
import me.theentropyshard.blabbej.model.Settings;

public class Blabbej {
    private static final Logger LOG = LogManager.getLogger(Blabbej.class);

    private final Path workDir;
    private final Settings settings;
    private final ChatManager chatManager;

    private Gui gui;

    public Blabbej() {
        Blabbej.instance = this;

        this.workDir = Path.of(System.getProperty("user.dir"));
        this.settings = Settings.load(this.workDir.resolve("settings.json"));
        this.chatManager = new ChatManager(this.workDir.resolve("chats"));

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            Settings.save(this.workDir.resolve("settings.json"), this.settings);
        }));

        this.createGUI();
    }

    private void createGUI() {
        try {
            SwingUtilities.invokeAndWait(() -> {
                this.gui = new Gui();
            });
        } catch (InterruptedException e) {
            Blabbej.LOG.error("Could not wait for the GUI", e);

            System.exit(1);
        } catch (InvocationTargetException e) {
            Blabbej.LOG.error("An exception occurred while creating the GUI", e);

            System.exit(1);
        }
    }

    private static Blabbej instance;

    public static Blabbej getInstance() {
        return Blabbej.instance;
    }

    public Settings getSettings() {
        return this.settings;
    }

    public ChatManager getChatManager() {
        return this.chatManager;
    }

    public Gui getGui() {
        return this.gui;
    }
}

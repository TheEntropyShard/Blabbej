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

package me.theentropyshard.blabbej.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class Settings {
    private static final Logger LOG = LogManager.getLogger(Settings.class);

    private static final Gson GSON = new GsonBuilder()
        .disableJdkUnsafe()
        .disableHtmlEscaping()
        .create();

    public String currentServer;
    public Map<String, Server> servers = new HashMap<>();

    public Settings() {

    }

    public interface SettingsListener {
        void onServerAdded(String address);

        void onServerRemoved(String address);
    }

    public static Settings load(Path path) {
        try {
            if (!Files.exists(path)) {
                return new Settings();
            }

            return Settings.GSON.fromJson(Files.readString(path, StandardCharsets.UTF_8), Settings.class);
        } catch (IOException e) {
            Settings.LOG.error("Could not load settings", e);

            return new Settings();
        }
    }

    public static void save(Path path, Settings settings) {
        try {
            Files.writeString(path, Settings.GSON.toJson(settings), StandardCharsets.UTF_8);
        } catch (IOException e) {
            Settings.LOG.error("Could not save settings", e);
        }
    }
}

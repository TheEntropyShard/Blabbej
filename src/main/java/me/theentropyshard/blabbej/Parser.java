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

import java.io.BufferedReader;
import java.io.StringReader;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static List<Message> parse(String s) {
        List<Message> messages = new ArrayList<>();

        List<String> messageContent = new ArrayList<>();
        for (String line : new BufferedReader(new StringReader(s)).lines().toList()) {
            if (line.startsWith("\\")) {
                line = line.substring(1);
                String[] parts = line.split(" ");
                messages.add(new Message(
                    parts[0].trim(), OffsetDateTime.parse(parts[2].trim(), DateTimeFormatter.ISO_DATE_TIME)
                    .atZoneSameInstant(ZoneId.systemDefault()).format(Parser.FORMATTER), String.join("\n", messageContent)
                ));
                messageContent.clear();
            } else {
                messageContent.add(line);
            }
        }

        return messages;
    }
}

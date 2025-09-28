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

public class Message {
    private final String author;
    private final String datetime;
    private final String text;

    public Message(String author, String datetime, String text) {
        this.author = author;
        this.datetime = datetime;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Message{" +
            "author='" + this.author + '\'' +
            ", datetime='" + this.datetime + '\'' +
            ", text='" + this.text + '\'' +
            '}';
    }

    public String getAuthor() {
        return this.author;
    }

    public String getDatetime() {
        return this.datetime;
    }

    public String getText() {
        return this.text;
    }
}

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

import java.util.HashMap;
import java.util.Map;

public class Server {
    private String address;
    private String currentAccount;
    private Map<String, Account> accounts = new HashMap<>();

    public Server() {

    }

    public Server(String address) {
        this.address = address;
    }

    public String getAddress() {
        return this.address;
    }

    public String getCurrentAccount() {
        return this.currentAccount;
    }

    public void setCurrentAccount(String currentAccount) {
        this.currentAccount = currentAccount;
    }

    public Map<String, Account> getAccounts() {
        return this.accounts;
    }
}

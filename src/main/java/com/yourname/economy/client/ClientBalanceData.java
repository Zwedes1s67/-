package com.yourname.economy.client;

public class ClientBalanceData {
    private static int playerBalance = 0;

    public static void set(int balance) {
        ClientBalanceData.playerBalance = balance;
    }

    public static int getPlayerBalance() {
        return playerBalance;
    }
}

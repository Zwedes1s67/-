package com.yourname.economy.capability;

import net.minecraft.nbt.CompoundTag;

public class PlayerBalance {
    private int balance = 0;

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = Math.max(0, balance);
    }

    public void addBalance(int amount) {
        this.balance = Math.max(0, this.balance + amount);
    }

    public void removeBalance(int amount) {
        this.balance = Math.max(0, this.balance - amount);
    }

    public void copyFrom(PlayerBalance source) {
        this.balance = source.balance;
    }

    public void saveNBTData(CompoundTag nbt) {
        nbt.putInt("EconomyBalance", balance);
    }

    public void loadNBTData(CompoundTag nbt) {
        balance = nbt.getInt("EconomyBalance");
    }
}

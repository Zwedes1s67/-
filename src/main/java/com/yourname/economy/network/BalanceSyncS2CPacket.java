package com.yourname.economy.network;

import com.yourname.economy.client.ClientBalanceData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BalanceSyncS2CPacket {
    private final int balance;

    public BalanceSyncS2CPacket(int balance) {
        this.balance = balance;
    }

    public BalanceSyncS2CPacket(FriendlyByteBuf buf) {
        this.balance = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(balance);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ClientBalanceData.set(balance);
        });
        return true;
    }
}

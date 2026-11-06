package com.yourname.economy.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.yourname.economy.capability.PlayerBalanceProvider;
import com.yourname.economy.network.BalanceSyncS2CPacket;
import com.yourname.economy.network.ModMessages;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class MoneyCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("money")
                .requires(source -> source.hasPermission(2))
                .then(Commands.literal("add")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(ctx -> addMoney(
                                                ctx.getSource(),
                                                EntityArgument.getPlayer(ctx, "player"),
                                                IntegerArgumentType.getInteger(ctx, "amount")
                                        )))))
                .then(Commands.literal("remove")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(1))
                                        .executes(ctx -> removeMoney(
                                                ctx.getSource(),
                                                EntityArgument.getPlayer(ctx, "player"),
                                                IntegerArgumentType.getInteger(ctx, "amount")
                                        )))))
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("amount", IntegerArgumentType.integer(0))
                                        .executes(ctx -> setMoney(
                                                ctx.getSource(),
                                                EntityArgument.getPlayer(ctx, "player"),
                                                IntegerArgumentType.getInteger(ctx, "amount")
                                        )))))
                .then(Commands.literal("get")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(ctx -> getMoney(
                                        ctx.getSource(),
                                        EntityArgument.getPlayer(ctx, "player")
                                ))))
        );
    }

    private static int addMoney(CommandSourceStack source, ServerPlayer player, int amount) {
        player.getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(balance -> {
            balance.addBalance(amount);
            ModMessages.sendToPlayer(new BalanceSyncS2CPacket(balance.getBalance()), player);
            source.sendSuccess(() -> Component.literal("Выдано " + amount + " валюты игроку " + player.getName().getString() + ". Текущий баланс: " + balance.getBalance()), true);
        });
        return 1;
    }

    private static int removeMoney(CommandSourceStack source, ServerPlayer player, int amount) {
        player.getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(balance -> {
            balance.removeBalance(amount);
            ModMessages.sendToPlayer(new BalanceSyncS2CPacket(balance.getBalance()), player);
            source.sendSuccess(() -> Component.literal("Забрано " + amount + " валюты у игрока " + player.getName().getString() + ". Текущий баланс: " + balance.getBalance()), true);
        });
        return 1;
    }

    private static int setMoney(CommandSourceStack source, ServerPlayer player, int amount) {
        player.getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(balance -> {
            balance.setBalance(amount);
            ModMessages.sendToPlayer(new BalanceSyncS2CPacket(balance.getBalance()), player);
            source.sendSuccess(() -> Component.literal("Баланс игрока " + player.getName().getString() + " установлен на " + balance.getBalance()), true);
        });
        return 1;
    }

    private static int getMoney(CommandSourceStack source, ServerPlayer player) {
        player.getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(balance -> {
            source.sendSuccess(() -> Component.literal("Баланс игрока " + player.getName().getString() + ": " + balance.getBalance()), false);
        });
        return 1;
    }
}

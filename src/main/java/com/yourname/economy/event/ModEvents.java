package com.yourname.economy.event;

import com.yourname.economy.EconomyMod;
import com.yourname.economy.capability.PlayerBalance;
import com.yourname.economy.capability.PlayerBalanceProvider;
import com.yourname.economy.command.MoneyCommand;
import com.yourname.economy.network.BalanceSyncS2CPacket;
import com.yourname.economy.network.ModMessages;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.server.command.ConfigCommand;

public class ModEvents {

    @Mod.EventBusSubscriber(modid = EconomyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEventBusEvents {
        @SubscribeEvent
        public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
            event.register(PlayerBalance.class);
        }
    }

    @Mod.EventBusSubscriber(modid = EconomyMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ForgeEventBusEvents {
        @SubscribeEvent
        public static void onAttachCapabilitiesPlayer(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                if (!event.getObject().getCapability(PlayerBalanceProvider.PLAYER_BALANCE).isPresent()) {
                    event.addCapability(new ResourceLocation(EconomyMod.MOD_ID, "properties"), new PlayerBalanceProvider());
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerCloned(PlayerEvent.Clone event) {
            if (event.isWasDeath()) {
                event.getOriginal().reviveCaps();
                event.getOriginal().getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(oldStore -> {
                    event.getEntity().getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
                event.getOriginal().invalidateCaps();
            }
        }

        @SubscribeEvent
        public static void onCommandsRegister(RegisterCommandsEvent event) {
            MoneyCommand.register(event.getDispatcher());
            ConfigCommand.register(event.getDispatcher());
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                serverPlayer.getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(balance -> {
                    ModMessages.sendToPlayer(new BalanceSyncS2CPacket(balance.getBalance()), serverPlayer);
                });
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                serverPlayer.getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(balance -> {
                    ModMessages.sendToPlayer(new BalanceSyncS2CPacket(balance.getBalance()), serverPlayer);
                });
            }
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity() instanceof ServerPlayer serverPlayer) {
                serverPlayer.getCapability(PlayerBalanceProvider.PLAYER_BALANCE).ifPresent(balance -> {
                    ModMessages.sendToPlayer(new BalanceSyncS2CPacket(balance.getBalance()), serverPlayer);
                });
            }
        }
    }
}

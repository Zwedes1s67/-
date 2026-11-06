package com.yourname.economy.client;

import com.yourname.economy.EconomyMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EconomyMod.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EconomyHudOverlay {

    @SubscribeEvent
    public static void onRenderGuiOverlayPost(RenderGuiOverlayEvent.Post event) {
        if (event.getOverlay().id().equals(VanillaGuiOverlay.EXPERIENCE_BAR.id())) {
            Minecraft mc = Minecraft.getInstance();
            if (mc.player == null || mc.options.hideGui) return;

            GuiGraphics guiGraphics = event.getGuiGraphics();
            Font font = mc.font;
            String text = "Баланс: " + ClientBalanceData.getPlayerBalance() + " $";

            int screenWidth = event.getWindow().getGuiScaledWidth();
            int screenHeight = event.getWindow().getGuiScaledHeight();

            int x = (screenWidth - font.width(text)) / 2;
            int y = screenHeight - 42; // Над полоской и цифрой опыта

            guiGraphics.drawString(font, text, x, y, 0xFFAA00, true);
        }
    }
}

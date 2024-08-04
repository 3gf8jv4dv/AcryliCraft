package io.devbobcorn.acrylic.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import io.devbobcorn.acrylic.AcrylicConfig;
import io.devbobcorn.acrylic.AcrylicMod;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;
import net.neoforged.neoforge.client.loading.NeoForgeLoadingOverlay;
import org.lwjgl.opengl.GL11;

@EventBusSubscriber(modid = AcrylicMod.MOD_ID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public class ScreenEventHandler {

    private static Minecraft s_minecraft;

    @SubscribeEvent
    public static void onScreenOpen(ScreenEvent.Opening event) {

        if (s_minecraft == null) {
            s_minecraft = Minecraft.getInstance();
        }

        AcrylicConfig.getInstance().updateTransparencyStatus(s_minecraft.level == null);
    }

    @SubscribeEvent
    public static void preScreenRender(ScreenEvent.Render.Pre event) {

        if (s_minecraft == null) {
            s_minecraft = Minecraft.getInstance();
        }

        if ((boolean) AcrylicConfig.getInstance().getValue(AcrylicConfig.TRANSPARENT_WINDOW)) {
            if (s_minecraft != null && s_minecraft.level == null) {
                // Vanilla will render the panorama to hide the pixels beneath,
                // but we don't use panorama here so clear them up.
                RenderSystem.clearColor(0, 0, 0, 0);
                RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT, Minecraft.ON_OSX);
            }
        }
    }
}

package io.devbobcorn.acrylic.mixin.forge;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.devbobcorn.acrylic.AcrylicConfig;
import io.devbobcorn.acrylic.AcrylicMod;
import io.devbobcorn.acrylic.client.window.IWindow;
import io.devbobcorn.acrylic.client.window.WindowUtil;

import com.mojang.blaze3d.platform.DisplayData;
import com.mojang.blaze3d.platform.ScreenManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.platform.WindowEventHandler;

import net.minecraft.Util;

@Mixin(Window.class)
public class WindowForgeMixin {

    @Final
    @Shadow
    private static Logger LOGGER;

    @Redirect(
            method = "<init>",
            remap = false, // Don't remap method name for constructors
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/lwjgl/glfw/GLFW;glfwDefaultWindowHints()V",
                    remap = false // Don't remap method name for native methods
            )
    )
    public void glfwWindowHintRedirect() {

        LOGGER.info("Applying default window hints...");

        GLFW.glfwDefaultWindowHints();

        // Window hints applied to vanilla window:

        // 0x22001            196609
        // GLFW_FOCUSED       0x30001

        // 0x2200B            221185
        // GLFW_HOVERED       0x36001

        // 0x22002            3
        // GLFW_ICONIFIED     0x11

        // 0x22003            2
        // GLFW_RESIZABLE     0x10

        // 0x22008            204801
        // GLFW_MAXIMIZED     0x32001

        // 0x22006            1 (GLFW_TRUE)
        // GLFW_AUTO_ICONIFY  0x1

        // Initialize Acrylic by accessing its instance
        if (AcrylicConfig.getInstance().getValue(AcrylicConfig.TRANSPARENT_WINDOW)) {
            // Magic! (This also works on macOS and linux)
            GLFW.glfwWindowHint(GLFW.GLFW_TRANSPARENT_FRAMEBUFFER, GLFW.GLFW_TRUE);
        }

        LOGGER.info("Window hints applied!");
    }

}

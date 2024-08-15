package io.devbobcorn.acrylic.mixin;

import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
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
public class WindowMixin implements IWindow {

    @Final
    @Shadow
    // GLFW Window id
    private long window;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void init(
            final WindowEventHandler handler, final ScreenManager manager,
            final DisplayData display, final String videoMode, final String title,
            final CallbackInfo callback
    ) {
        // Check if transparent frame buffer is enabled
        // See https://www.glfw.org/docs/3.3/window_guide.html#window_transparency
        var transparent = GLFW.glfwGetWindowAttrib(window, GLFW.GLFW_TRANSPARENT_FRAMEBUFFER) == 1;

        AcrylicMod.setTransparencyEnabled(transparent);

        var config = AcrylicConfig.getInstance();

        // Check if transparent framebuffer requested but failed to initialize
        if ((boolean) config.getValue(AcrylicConfig.TRANSPARENT_WINDOW) && !transparent) {
            AcrylicMod.setTransparencyInitFailed(true);
        }

        // Check OS
        if (Util.getPlatform() == Util.OS.WINDOWS) {
            // Store window handle for later use
            AcrylicMod.setWindowHandle(WindowUtil.getWindowHandle(window));

            // Apply Win11-Specific window setup
            config.ApplyWin11Specific();
        }
    }

    @Override
    public long acrylic_mod$getGLFWId() {
        return window;
    }

}

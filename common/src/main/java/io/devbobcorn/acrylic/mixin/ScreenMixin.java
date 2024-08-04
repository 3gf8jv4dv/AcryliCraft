package io.devbobcorn.acrylic.mixin;

import io.devbobcorn.acrylic.AcrylicConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {

    @Shadow
    protected Minecraft minecraft;

    @Inject(at = @At("HEAD"), method = "renderBackground", cancellable = true)
    public void renderBackground(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo callback) {

        var conf = AcrylicConfig.getInstance();

        if ((boolean) conf.getValue(AcrylicConfig.TRANSPARENT_WINDOW) && (boolean) conf.getValue(AcrylicConfig.REMOVE_SCREEN_BACKGROUND)) {
            if (minecraft.level == null) { // Not rendering a client world
                callback.cancel();
            }
        }
    }
}

package io.devbobcorn.acrylic.forge;

import io.devbobcorn.acrylic.AcrylicMod;
import io.devbobcorn.acrylic.client.screen.ConfigScreenUtil;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import org.jetbrains.annotations.NotNull;

@Mod(AcrylicMod.MOD_ID)
public class AcrylicModNeoForge {

    public AcrylicModNeoForge(IEventBus bus) {
        bus.addListener(this::clientSetup);
    }

    static class AcrylicConfigScreenFactory implements IConfigScreenFactory {

        @Override
        public @NotNull Screen createScreen(@NotNull ModContainer modContainer, @NotNull Screen screen) {
            return ConfigScreenUtil.create(screen);
        }
    }

    private void clientSetup(final FMLCommonSetupEvent event) {

        ModLoadingContext.get().registerExtensionPoint(
                IConfigScreenFactory.class,
                AcrylicConfigScreenFactory::new);

    }
}

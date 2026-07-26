package com.explo.projecttimereforged;

import com.explo.projecttimereforged.config.CommonConfig;
import com.explo.projecttimereforged.items.TimeWatchItem;
import com.mojang.logging.LogUtils;
import org.slf4j.Logger;
import moze_intel.projecte.api.capabilities.PECapabilities;
import moze_intel.projecte.api.capabilities.item.IPedestalItem;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(ProjectTimeMasterReforged.MODID)
public class ProjectTimeMasterReforged {
    public static final String MODID = "projecttimemasterreforged";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ProjectTimeMasterReforged(ModContainer modContainer, IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC);

        modEventBus.addListener(this::addCreative);
        // Registrando as Capabilities
        modEventBus.addListener(this::registerCapabilities);

        NeoForge.EVENT_BUS.register(this);

        if (FMLEnvironment.dist.isClient()) {
            modEventBus.addListener(ClientModEvents::onClientSetup);
        }
    }

    // =========================================================
    //  Mostrando para o ProjectE que nossos itens funcionam
    // =========================================================
    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                PECapabilities.PEDESTAL_ITEM_CAPABILITY, // <--- Verifique se é PECapabilities ou ProjectECapabilities
                (stack, context) -> (IPedestalItem) stack.getItem(), // O próprio item é a implementação

                // Lista de itens que vão ter essa capacidade
                ModItems.TIME_WATCH_MK2.get(),
                ModItems.TIME_WATCH_MK3.get(),
                ModItems.TIME_WATCH_MK4.get(),
                ModItems.TIME_WATCH_MK5.get(),
                ModItems.TIME_WATCH_MK6.get(),
                ModItems.TIME_WATCH_MK7.get(),
                ModItems.TIME_WATCH_MK8.get()
        );
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
            event.accept(ModItems.TIME_WATCH_MK2);
            event.accept(ModItems.TIME_WATCH_MK8);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    // ProjectTimeMasterReforgedClient

    static class ClientModEvents {
        public static void onClientSetup(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                registerWatchProperty(ModItems.TIME_WATCH_MK2.get());
                registerWatchProperty(ModItems.TIME_WATCH_MK3.get());
                registerWatchProperty(ModItems.TIME_WATCH_MK4.get());
                registerWatchProperty(ModItems.TIME_WATCH_MK5.get());
                registerWatchProperty(ModItems.TIME_WATCH_MK6.get());
                registerWatchProperty(ModItems.TIME_WATCH_MK7.get());
                registerWatchProperty(ModItems.TIME_WATCH_MK8.get());
            });
        }

        private static void registerWatchProperty(Item item) {
            ItemProperties.register(item,
                    ResourceLocation.fromNamespaceAndPath(ProjectTimeMasterReforged.MODID, "mode"),
                    (stack, level, entity, seed) -> {
                        if (stack.getItem() instanceof TimeWatchItem) {
                            var data = stack.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY);
                            return data.contains("TimeMode") ? data.copyTag().getInt("TimeMode") : 0.0F;
                        }
                        return 0.0F;
                    });
        }
    }
}
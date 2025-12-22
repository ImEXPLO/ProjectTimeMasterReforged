package com.explo.projecttimereforged;

import com.explo.projecttimereforged.items.TimeWatchMK2;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.slf4j.Logger;
import com.mojang.logging.LogUtils;

// Imports essenciais para separar Cliente de Servidor
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;

// Imports do ProjectE
import moze_intel.projecte.api.capabilities.PECapabilities;
import moze_intel.projecte.api.capabilities.item.IPedestalItem;

@Mod(ProjectTimeMasterReforged.MODID)
public class ProjectTimeMasterReforged {
    public static final String MODID = "projecttimemasterreforged";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ProjectTimeMasterReforged(IEventBus modEventBus, ModContainer modContainer) {
        // Registros básicos
        ModItems.register(modEventBus);
        ModCreativeTabs.register(modEventBus);

        // Registro de Capabilities (Pedestal)
        modEventBus.addListener(this::registerCapabilities);

        // --- CORREÇÃO DO DEPRECATED ---
        // Registramos o Cliente aqui, mas protegemos com um IF para não quebrar servidores
        if (FMLEnvironment.dist == Dist.CLIENT) {
            modEventBus.addListener(ProjectTimeMasterReforgedClient::onClientSetup);
        }
    }

    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(
                PECapabilities.PEDESTAL_ITEM_CAPABILITY,
                (stack, context) -> (IPedestalItem) stack.getItem(),
                ModItems.TIMEWATCH_MK2.get(),
                ModItems.TIMEWATCH_MK3.get(),
                ModItems.TIMEWATCH_MK4.get(),
                ModItems.TIMEWATCH_MK5.get(),
                ModItems.TIMEWATCH_MK6.get(),
                ModItems.TIMEWATCH_MK7.get(),
                ModItems.TIMEWATCH_MK8.get()
        );
    }
}
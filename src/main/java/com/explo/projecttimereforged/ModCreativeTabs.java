package com.explo.projecttimereforged;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    // Cria o registro de abas
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProjectTimeMasterReforged.MODID);

    // Registra a nossa aba "Project Time Master"
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PROJECT_TIME_TAB = CREATIVE_MODE_TABS.register("projecttimemaster_tab", () -> CreativeModeTab.builder()
            .title(Component.literal("Project Time Master: Reforged")) // Nome que aparece no topo
            .icon(() -> new ItemStack(ModItems.TIMEWATCH_MK8.get())) // Ícone da aba (coloquei o MK8 pra ficar estiloso)
            .displayItems((parameters, output) -> {
                // Adiciona os itens na ordem
                output.accept(ModItems.TIMEWATCH_MK2.get());
                output.accept(ModItems.TIMEWATCH_MK3.get());
                output.accept(ModItems.TIMEWATCH_MK4.get());
                output.accept(ModItems.TIMEWATCH_MK5.get());
                output.accept(ModItems.TIMEWATCH_MK6.get());
                output.accept(ModItems.TIMEWATCH_MK7.get());
                output.accept(ModItems.TIMEWATCH_MK8.get());
            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}

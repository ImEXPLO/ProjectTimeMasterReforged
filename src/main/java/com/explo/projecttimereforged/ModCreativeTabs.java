package com.explo.projecttimereforged;

import com.explo.projecttimereforged.ProjectTimeMasterReforged; // Importe sua classe principal
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTabs {
    // Registro das Abas
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProjectTimeMasterReforged.MODID);

    // Aba "Project Time Reforged"
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> PROJECT_TAB = CREATIVE_MODE_TABS.register("project_time_tab", () -> CreativeModeTab.builder()
            // "creativetab.projecttimemasterreforged.project_time_tab" ----
            .title(Component.translatable("creativetab." + ProjectTimeMasterReforged.MODID + ".project_time_tab"))

            .icon(() -> new ItemStack(ModItems.TIME_WATCH_MK8.get()))

            // Adicionando os itens na aba
            .displayItems((parameters, output) -> {
                // Adiciona todos os relógios
                output.accept(ModItems.TIME_WATCH_MK2.get());
                output.accept(ModItems.TIME_WATCH_MK3.get());
                output.accept(ModItems.TIME_WATCH_MK4.get());
                output.accept(ModItems.TIME_WATCH_MK5.get());
                output.accept(ModItems.TIME_WATCH_MK6.get());
                output.accept(ModItems.TIME_WATCH_MK7.get());
                output.accept(ModItems.TIME_WATCH_MK8.get());

            }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
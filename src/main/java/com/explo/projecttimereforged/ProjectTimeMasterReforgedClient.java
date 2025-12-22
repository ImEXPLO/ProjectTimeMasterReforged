package com.explo.projecttimereforged;

import com.explo.projecttimereforged.items.TimeWatchMK2;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

// Sem anotações @Mod ou @EventBusSubscriber aqui!
// Nós vamos chamar este método manualmente no arquivo principal.
public class ProjectTimeMasterReforgedClient {

    public static void onClientSetup(FMLClientSetupEvent event) {
        // Enfileira o trabalho para rodar na thread segura do cliente
        event.enqueueWork(() -> {
            // Registra a propriedade "projecte:active" para nossos itens
            registerWatchProperty(ModItems.TIMEWATCH_MK2.get());
            registerWatchProperty(ModItems.TIMEWATCH_MK3.get());
            registerWatchProperty(ModItems.TIMEWATCH_MK4.get());
            registerWatchProperty(ModItems.TIMEWATCH_MK5.get());
            registerWatchProperty(ModItems.TIMEWATCH_MK6.get());
            registerWatchProperty(ModItems.TIMEWATCH_MK7.get());
            registerWatchProperty(ModItems.TIMEWATCH_MK8.get());
        });
    }

    private static void registerWatchProperty(net.minecraft.world.item.Item item) {
        // Ensina o jogo a ler se o relógio está "Ativo" (1.0) ou não (0.0)
        ItemProperties.register(item, ResourceLocation.fromNamespaceAndPath("projecte", "active"),
                (stack, level, entity, seed) -> TimeWatchMK2.isActive(stack) ? 1.0F : 0.0F);
    }
}
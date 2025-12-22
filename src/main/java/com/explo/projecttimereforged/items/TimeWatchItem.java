package com.explo.projecttimereforged.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TimeWatchItem extends Item {

    public TimeWatchItem() {
        super(new Item.Properties().stacksTo(1)); // Só pode ter 1 por slot
    }

    // Faz o item brilhar como se estivesse encantado
    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }

    // O que acontece quando clica num bloco?
    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();

        // Só rodamos a lógica no Servidor (para não desincronizar)
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(context.getClickedPos());

            if (be != null) {
                // AQUI VAI ENTRAR A LÓGICA DE ACELERAÇÃO (VAMOS FAZER JÁ JÁ)
                // Por enquanto, só avisa no chat que funcionou
                context.getPlayer().displayClientMessage(Component.literal("§d[Time Watch] §fTarget detected!"), true);

                // Tenta acelerar (placeholder)
                accelerate(level, be, context.getClickedPos());
            }
        }
        return InteractionResult.SUCCESS;
    }

    private void accelerate(Level level, BlockEntity be, net.minecraft.core.BlockPos pos) {
        // Magia negra da aceleração vai aqui
    }

    // Adiciona texto quando passa o mouse em cima
    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(Component.translatable("item.projecttimemaster_reforged.time_watch.tooltip"));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}

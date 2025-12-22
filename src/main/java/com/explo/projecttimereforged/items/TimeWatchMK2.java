package com.explo.projecttimereforged.items;

import com.explo.projecttimereforged.util.ModTags;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import moze_intel.projecte.api.capabilities.item.IPedestalItem;
import moze_intel.projecte.api.block_entity.IDMPedestal;

import java.util.ArrayList;
import java.util.List;

public class TimeWatchMK2 extends Item implements IPedestalItem {

    private final double ticksMult;
    private final String tierName;
    private final int colorHex;
    private final int radius;

    public TimeWatchMK2(double ticksMult, String tierName, int colorHex, int radius) {
        super(new Properties().stacksTo(1));
        this.ticksMult = ticksMult;
        this.tierName = tierName;
        this.colorHex = colorHex;
        this.radius = radius;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            boolean isNowActive = !isActive(stack);
            setActive(stack, isNowActive);

            if (isNowActive) {
                // SOM DE LIGAR: Agudo
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        net.minecraft.sounds.SoundEvents.EXPERIENCE_ORB_PICKUP,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 1.5F);

                player.displayClientMessage(Component.translatable("message.projecttimemasterreforged.activated").withStyle(ChatFormatting.GREEN), true);
            } else {
                // SOM DE DESLIGAR: Grave
                level.playSound(null, player.getX(), player.getY(), player.getZ(),
                        net.minecraft.sounds.SoundEvents.LEVER_CLICK,
                        net.minecraft.sounds.SoundSource.PLAYERS, 1.0F, 0.6F);

                player.displayClientMessage(Component.translatable("message.projecttimemasterreforged.deactivated").withStyle(ChatFormatting.RED), true);
            }

            return InteractionResultHolder.success(stack);
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide || !isActive(stack)) return;

        if (entity instanceof Player player && (player.getMainHandItem() == stack || player.getOffhandItem() == stack)) {
            if (level instanceof ServerLevel serverLevel) {
                int bonusTicks = (int) (18 * this.ticksMult);
                scanAndAccelerate(serverLevel, player.blockPosition(), this.radius, bonusTicks);

                long timeBonus = (long) (this.ticksMult * 10);
                serverLevel.setDayTime(serverLevel.getDayTime() + timeBonus);
            }
        }
    }

    @Override
    public <PEDESTAL extends BlockEntity & IDMPedestal> boolean updateInPedestal(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockPos pos, @NotNull PEDESTAL pedestal) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return false;
        int bonusTicks = (int) (18 * this.ticksMult);
        scanAndAccelerate(serverLevel, pos, this.radius, bonusTicks);
        return true;
    }

    private void scanAndAccelerate(ServerLevel level, BlockPos center, int radius, int bonusTicks) {
        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos targetPos = center.offset(x, y, z);
                    if (targetPos.equals(center)) continue;
                    accelerate(level, targetPos, bonusTicks);
                }
            }
        }
    }

    private void accelerate(ServerLevel level, BlockPos pos, int bonusTicks) {
        BlockState state = level.getBlockState(pos);
        if (state.is(ModTags.Blocks.ACCELERATION_BLACKLIST)) return;

        BlockEntity be = level.getBlockEntity(pos);
        if (be != null && !be.isRemoved()) {
            BlockEntityTicker<BlockEntity> ticker = state.getTicker(level, (BlockEntityType<BlockEntity>) be.getType());
            if (ticker != null) {
                try {
                    for (int i = 0; i < bonusTicks; i++) {
                        ticker.tick(level, pos, state, be);
                    }
                } catch (Exception e) {
                    // Ignore errors
                }
            }
        }
    }

    // --- TOOLTIPS TRADUZÍVEIS ---
    @Override
    public @NotNull List<Component> getPedestalDescription(float partialTicks) {
        List<Component> list = new ArrayList<>();
        // Usa placeholders (%s) para inserir os números
        list.add(Component.translatable("pedestal.projecttimemasterreforged.mode").withStyle(ChatFormatting.YELLOW));
        list.add(Component.translatable("pedestal.projecttimemasterreforged.speed", (int)(18 * this.ticksMult)).withStyle(ChatFormatting.YELLOW));
        list.add(Component.translatable("pedestal.projecttimemasterreforged.radius", radius).withStyle(ChatFormatting.YELLOW));
        return list;
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int bonus = (int) (18 * this.ticksMult);

        // Bonus Ticks
        tooltipComponents.add(Component.translatable("tooltip.projecttimemasterreforged.bonus_ticks", bonus).withStyle(ChatFormatting.GRAY));

        // Raio
        tooltipComponents.add(Component.translatable("tooltip.projecttimemasterreforged.radius_area", radius, radius).withStyle(ChatFormatting.GRAY));

        // Estado (Ligado/Desligado)
        Component statusText = isActive(stack) ?
                Component.translatable("tooltip.projecttimemasterreforged.status.on").withStyle(ChatFormatting.GREEN) :
                Component.translatable("tooltip.projecttimemasterreforged.status.off").withStyle(ChatFormatting.RED);

        tooltipComponents.add(Component.translatable("tooltip.projecttimemasterreforged.status", statusText).withStyle(ChatFormatting.GRAY));

        // Tier (Cor dinâmica ainda precisa ser aplicada via Style)
        tooltipComponents.add(Component.translatable("tooltip.projecttimemasterreforged.tier", tierName)
                .withStyle(style -> style.withColor(colorHex)));

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    // Helpers
    public static boolean isActive(ItemStack stack) {
        return stack.has(DataComponents.CUSTOM_DATA) && stack.get(DataComponents.CUSTOM_DATA).contains("Active");
    }

    public static void setActive(ItemStack stack, boolean active) {
        stack.update(DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY, (data) -> {
            return data.update(tag -> {
                if (active) tag.putBoolean("Active", true); else tag.remove("Active");
            });
        });
    }

    @Override
    public boolean isFoil(ItemStack stack) { return isActive(stack); }
}
package com.explo.projecttimereforged.items;

import com.explo.projecttimereforged.ProjectTimeMasterReforged;
import com.explo.projecttimereforged.config.CommonConfig;
import com.explo.projecttimereforged.util.ModTags;

import moze_intel.projecte.api.capabilities.item.IPedestalItem;
import moze_intel.projecte.api.block_entity.IDMPedestal;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimeWatchItem extends Item implements IPedestalItem {
    private final int tier;

    public static final int MODE_OFF = 0;
    public static final int MODE_FORWARD = 1;
    public static final int MODE_REWIND = 2;
    public static final int MODE_STOP = 3;

    public TimeWatchItem(int tier) {
        super(new Item.Properties().stacksTo(1));
        this.tier = tier;
    }

    // =========================================================
    // 1. USO NA MÃO
    // =========================================================

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
        if (level.isClientSide || !(level instanceof ServerLevel serverLevel)) return;

        // Só processa se o relógio estiver em uma das mãos de um jogador,
        // evitando que várias cópias na mochila multipliquem o efeito.
        if (!(entity instanceof Player player)) return;
        if (player.getMainHandItem() != stack && player.getOffhandItem() != stack) return;

        int mode = getMode(stack);
        if (mode == MODE_OFF) return;

        // Aceleração de blocos ao redor do jogador: ativa em qualquer modo
        // diferente de OFF; o modo define apenas o efeito no tempo global.
        accelerateArea(serverLevel, player.blockPosition(), 4, getConfigSpeed(this.tier));

        int speed = getConfigSpeed(this.tier) * 10;

        applyWorldTimeMagic(serverLevel, speed, mode);
    }

    private void accelerateArea(ServerLevel level, BlockPos center, int range, int bonusTicks) {
        for (BlockPos currentPos : BlockPos.betweenClosed(center.offset(-range, -range, -range), center.offset(range, range, range))) {
            if (currentPos.equals(center)) continue;
            tickBlock(level, currentPos.immutable(), bonusTicks);
        }
    }

    private void applyWorldTimeMagic(ServerLevel serverLevel, int speed, int mode) {
        switch (mode) {
            case MODE_FORWARD:
                if (CommonConfig.ENABLE_TIME_SKIP.get()) {
                    serverLevel.setDayTime(serverLevel.getDayTime() + speed);
                }
                break;
            case MODE_REWIND:
                if (CommonConfig.ENABLE_REWIND.get()) {
                    long currentTime = serverLevel.getDayTime();
                    long newTime = currentTime - speed;
                    if (newTime < 0) newTime = 0;
                    serverLevel.setDayTime(newTime);
                }
                break;
            case MODE_STOP:
                if (CommonConfig.ALLOW_STOP_TIME.get()) {
                    GameRules rules = serverLevel.getGameRules();
                    if (rules.getBoolean(GameRules.RULE_DAYLIGHT)) {
                        rules.getRule(GameRules.RULE_DAYLIGHT).set(false, serverLevel.getServer());
                    }
                }
                break;
        }
    }

    // =========================================================
    // 2. LÓGICA DO PEDESTAL
    // =========================================================

    @Override
    public <PEDESTAL extends BlockEntity & IDMPedestal> boolean updateInPedestal(@NotNull ItemStack stack, @NotNull Level level, @NotNull BlockPos pos, @NotNull PEDESTAL pedestal) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            int range = 4; // Raio 9x9x9
            int speed = getConfigSpeed(this.tier);

            accelerateArea(serverLevel, pos, range, speed);
            return false;
        }
        return false;
    }

    private void tickBlock(ServerLevel level, BlockPos pos, int bonusTicks) {
        BlockState state = level.getBlockState(pos);
        BlockEntity be = level.getBlockEntity(pos);

        if (state.isAir()) return;
        if (state.is(ModTags.Blocks.ACCELERATION_BLACKLIST)) return;

        try {
            for (int i = 0; i < bonusTicks; i++) {
                if (state.isRandomlyTicking()) {
                    state.randomTick(level, pos, level.random);
                }
                if (be != null && !be.isRemoved()) {
                    BlockEntityTicker<BlockEntity> ticker = state.getTicker(level, (BlockEntityType<BlockEntity>) be.getType());
                    if (ticker != null) {
                        ticker.tick(level, pos, state, be);
                    }
                }
            }
        } catch (Exception e) {
            // Blocos de outros mods podem não suportar ticks forçados; isola a
            // falha para não derrubar o tick do servidor, mas deixa rastro no log.
            ProjectTimeMasterReforged.LOGGER.error("Failed to accelerate block {} at {}", state.getBlock(), pos, e);
        }
    }

    @Override
    public @NotNull List<Component> getPedestalDescription(float v) {
        return List.of(
                Component.literal("Machine Acceleration").withStyle(ChatFormatting.GREEN),
                Component.literal("Bonus Ticks: " + getConfigSpeed(this.tier)).withStyle(ChatFormatting.LIGHT_PURPLE),
                Component.literal("Area: 9x9x9").withStyle(ChatFormatting.GRAY)
        );
    }

    // =========================================================
    // 3. MÉTODOS AUXILIARES
    // =========================================================

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        if (!level.isClientSide && player.isCrouching()) {
            changeMode(player, player.getItemInHand(hand));
            return InteractionResultHolder.success(player.getItemInHand(hand));
        }
        return super.use(level, player, hand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Player player = context.getPlayer();
        if (player != null && !context.getLevel().isClientSide && player.isCrouching()) {
            changeMode(player, context.getItemInHand());
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }

    private void changeMode(Player player, ItemStack stack) {
        int currentMode = getMode(stack);
        int nextMode = (currentMode + 1) % 4;
        setMode(stack, nextMode);

        // Anti-Congelamento: ao sair do modo STOP, devolve o ciclo do dia.
        // A restauração acontece na transição de modo (e não a cada tick) para
        // não sobrescrever /gamerule ajustado manualmente por admins.
        if (currentMode == MODE_STOP && player.level() instanceof ServerLevel serverLevel) {
            GameRules rules = serverLevel.getGameRules();
            if (!rules.getBoolean(GameRules.RULE_DAYLIGHT)) {
                rules.getRule(GameRules.RULE_DAYLIGHT).set(true, serverLevel.getServer());
            }
        }

        String modeKey = getModeTranslationKey(nextMode);
        ChatFormatting color = switch (nextMode) {
            case MODE_OFF -> ChatFormatting.GRAY;
            case MODE_FORWARD -> ChatFormatting.GOLD;
            case MODE_REWIND -> ChatFormatting.AQUA;
            case MODE_STOP -> ChatFormatting.RED;
            default -> ChatFormatting.WHITE;
        };
        player.displayClientMessage(Component.translatable("message." + ProjectTimeMasterReforged.MODID + ".mode_changed",
                Component.translatable(modeKey).withStyle(color)), true);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        int mode = getMode(stack);
        String modeKey = getModeTranslationKey(mode);
        int speed = getConfigSpeed(this.tier);
        tooltipComponents.add(Component.translatable("tooltip." + ProjectTimeMasterReforged.MODID + ".mode").append(": ").append(Component.translatable(modeKey).withStyle(ChatFormatting.YELLOW)));
        tooltipComponents.add(Component.translatable("tooltip." + ProjectTimeMasterReforged.MODID + ".speed").append(": ").append(Component.literal("+" + speed).withStyle(ChatFormatting.LIGHT_PURPLE)));
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private int getConfigSpeed(int tier) {
        try {
            return switch (tier) {
                case 1 -> CommonConfig.MK1_SPEED.get();
                case 2 -> CommonConfig.MK2_SPEED.get();
                case 3 -> CommonConfig.MK3_SPEED.get();
                case 4 -> CommonConfig.MK4_SPEED.get();
                case 5 -> CommonConfig.MK5_SPEED.get();
                case 6 -> CommonConfig.MK6_SPEED.get();
                case 7 -> CommonConfig.MK7_SPEED.get();
                case 8 -> CommonConfig.MK8_SPEED.get();
                default -> 1;
            };
        } catch (Exception e) { return 10; }
    }

    private String getModeTranslationKey(int mode) {
        String modId = ProjectTimeMasterReforged.MODID;
        return switch (mode) {
            case MODE_FORWARD -> "mode." + modId + ".forward";
            case MODE_REWIND -> "mode." + modId + ".rewind";
            case MODE_STOP -> "mode." + modId + ".stop";
            default -> "mode." + modId + ".off";
        };
    }

    private int getMode(ItemStack stack) {
        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        return data.contains("TimeMode") ? data.copyTag().getInt("TimeMode") : MODE_OFF;
    }

    private void setMode(ItemStack stack, int mode) {
        CustomData.update(DataComponents.CUSTOM_DATA, stack, tag -> tag.putInt("TimeMode", mode));
    }
}
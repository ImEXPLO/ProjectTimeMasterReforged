package com.explo.projecttimereforged;

import com.explo.projecttimereforged.items.TimeWatchMK2;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ProjectTimeMasterReforged.MODID);

    // MK2 (1.5x, Raio 3)
    public static final DeferredItem<Item> TIMEWATCH_MK2 = ITEMS.register("time_watch_mk2",
            () -> new TimeWatchMK2(1.5, "MK2", 0xFF5555, 3));

    // MK3 (2.0x, Raio 5)
    public static final DeferredItem<Item> TIMEWATCH_MK3 = ITEMS.register("time_watch_mk3",
            () -> new TimeWatchMK2(2.0, "MK3", 0xFFAA00, 5));

    // MK4 (2.5x, Raio 7)
    public static final DeferredItem<Item> TIMEWATCH_MK4 = ITEMS.register("time_watch_mk4",
            () -> new TimeWatchMK2(2.5, "MK4", 0xFFFF55, 7));

    // MK5 (3.0x, Raio 9)
    public static final DeferredItem<Item> TIMEWATCH_MK5 = ITEMS.register("time_watch_mk5",
            () -> new TimeWatchMK2(3.0, "MK5", 0x55FF55, 9));

    // MK6 (3.5x, Raio 12)
    public static final DeferredItem<Item> TIMEWATCH_MK6 = ITEMS.register("time_watch_mk6",
            () -> new TimeWatchMK2(3.5, "MK6", 0x55FFFF, 12));

    // MK7 (4.0x, Raio 16 - Um Chunk)
    public static final DeferredItem<Item> TIMEWATCH_MK7 = ITEMS.register("time_watch_mk7",
            () -> new TimeWatchMK2(4.0, "MK7", 0x5555FF, 16));

    // MK8 (4.5x, Raio 32 - Área Gigante)
    public static final DeferredItem<Item> TIMEWATCH_MK8 = ITEMS.register("time_watch_mk8",
            () -> new TimeWatchMK2(4.5, "MK8", 0xFF55FF, 32));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
package com.explo.projecttimereforged;

import com.explo.projecttimereforged.items.TimeWatchItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ProjectTimeMasterReforged.MODID);

    // --- RELÓGIOS (MK2 ao MK8) ---

    public static final DeferredItem<Item> TIME_WATCH_MK2 = ITEMS.register("time_watch_mk2",
            () -> new TimeWatchItem(2));

    public static final DeferredItem<Item> TIME_WATCH_MK3 = ITEMS.register("time_watch_mk3",
            () -> new TimeWatchItem(3));

    public static final DeferredItem<Item> TIME_WATCH_MK4 = ITEMS.register("time_watch_mk4",
            () -> new TimeWatchItem(4));

    public static final DeferredItem<Item> TIME_WATCH_MK5 = ITEMS.register("time_watch_mk5",
            () -> new TimeWatchItem(5));

    public static final DeferredItem<Item> TIME_WATCH_MK6 = ITEMS.register("time_watch_mk6",
            () -> new TimeWatchItem(6));

    public static final DeferredItem<Item> TIME_WATCH_MK7 = ITEMS.register("time_watch_mk7",
            () -> new TimeWatchItem(7));

    public static final DeferredItem<Item> TIME_WATCH_MK8 = ITEMS.register("time_watch_mk8",
            () -> new TimeWatchItem(8));

    // --- REGISTRO ---
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
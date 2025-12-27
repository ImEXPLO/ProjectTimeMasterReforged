package com.explo.projecttimereforged.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public class CommonConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Features Toggle
    public static final ModConfigSpec.BooleanValue ENABLE_TIME_SKIP;
    public static final ModConfigSpec.BooleanValue ENABLE_REWIND;
    public static final ModConfigSpec.BooleanValue ALLOW_STOP_TIME;

    // Speed Values (MK1 to MK8)
    public static final ModConfigSpec.IntValue MK1_SPEED;
    public static final ModConfigSpec.IntValue MK2_SPEED;
    public static final ModConfigSpec.IntValue MK3_SPEED;
    public static final ModConfigSpec.IntValue MK4_SPEED;
    public static final ModConfigSpec.IntValue MK5_SPEED;
    public static final ModConfigSpec.IntValue MK6_SPEED;
    public static final ModConfigSpec.IntValue MK7_SPEED;
    public static final ModConfigSpec.IntValue MK8_SPEED;

    static {
        BUILDER.push("General Settings");
        ENABLE_TIME_SKIP = BUILDER.comment("Allow watches to fast-forward world time?")
                .define("enableTimeSkip", true);
        ENABLE_REWIND = BUILDER.comment("Allow watches to rewind world time? (Experimental)")
                .define("enableRewind", true);
        ALLOW_STOP_TIME = BUILDER.comment("Allow watches to stop the daylight cycle?")
                .define("allowStopTime", true);
        BUILDER.pop();

        BUILDER.push("Watch Acceleration Settings (Bonus Ticks added per tick)");
        MK1_SPEED = BUILDER.comment("Bonus ticks for MK1").defineInRange("mk1Speed", 2, 1, 10000);
        MK2_SPEED = BUILDER.comment("Bonus ticks for MK2").defineInRange("mk2Speed", 4, 1, 10000);
        MK3_SPEED = BUILDER.comment("Bonus ticks for MK3").defineInRange("mk3Speed", 8, 1, 10000);
        MK4_SPEED = BUILDER.comment("Bonus ticks for MK4").defineInRange("mk4Speed", 16, 1, 10000);
        MK5_SPEED = BUILDER.comment("Bonus ticks for MK5").defineInRange("mk5Speed", 32, 1, 10000);
        MK6_SPEED = BUILDER.comment("Bonus ticks for MK6").defineInRange("mk6Speed", 64, 1, 10000);
        MK7_SPEED = BUILDER.comment("Bonus ticks for MK7").defineInRange("mk7Speed", 128, 1, 10000);
        MK8_SPEED = BUILDER.comment("Bonus ticks for MK8").defineInRange("mk8Speed", 256, 1, 10000);
        BUILDER.pop();

        SPEC = BUILDER.build();
    }
}
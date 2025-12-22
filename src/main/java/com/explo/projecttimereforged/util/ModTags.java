package com.explo.projecttimereforged.util;

import com.explo.projecttimereforged.ProjectTimeMasterReforged;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModTags {

    // Classe interna para organizar Tags de Blocos
    public static class Blocks {
        // Define a tag: "projecttimemasterreforged:acceleration_blacklist"
        public static final TagKey<Block> ACCELERATION_BLACKLIST = createTag("acceleration_blacklist");

        private static TagKey<Block> createTag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ProjectTimeMasterReforged.MODID, name));
        }
    }

    // Função Futura: Se form filtrar mobs, será criado a classe "Entities" aqui dentro.
    /*
    public static class Entities {
        public static final TagKey<EntityType<?>> ACCELERATION_BLACKLIST = ...
    }
    */
}
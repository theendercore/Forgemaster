package aug.forgemaster.block;

import aug.forgemaster.Forgemaster;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;

public class ModBlocks {

    public static final Block ATTACCA_SHARD = registerBlock("attacca_shard", new AttaccaShardBlock(AbstractBlock.Settings.create().strength(0.5f).sounds(BlockSoundGroup.NETHERITE).noCollision()), new Item.Settings().fireproof());

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Forgemaster.id(name), block);
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Forgemaster.id(name), new BlockItem(block, new Item.Settings()));
    }

    private static Block registerBlock(String name, Block block, Item.Settings settings) {
        registerBlockItem(name, block, settings);
        return Registry.register(Registries.BLOCK, Forgemaster.id(name), block);
    }

    private static void registerBlockItem(String name, Block block, Item.Settings settings) {
        Registry.register(Registries.ITEM, Forgemaster.id(name), new BlockItem(block, settings));
    }

    public static void register() {
        Forgemaster.LOGGER.info("Registering Mod Blocks for " + Forgemaster.MOD_ID);
    }
}

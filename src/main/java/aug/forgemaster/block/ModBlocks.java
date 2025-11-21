package aug.forgemaster.block;

import aug.forgemaster.Forgemaster;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.text.Text;

import java.util.List;

public class ModBlocks {

    public static final Block ATTACCA_SHARD = registerBlock("attacca_shard",
            new AttaccaShardBlock(AbstractBlock.Settings.create()
                    .strength(0.5f)
                    .sounds(BlockSoundGroup.NETHERITE)
                    .noCollision()
                    .luminance(state -> 3)
                    .emissiveLighting(Blocks::always)
            ), new Item.Settings().fireproof().component(DataComponentTypes.LORE, new LoreComponent(List.of(Text.translatable("block.forgemaster.attacca_shard.desc")))));
    public static final Block GREEK_FIRE = registerBlock("greek_fire", new GreekFireBlock(AbstractBlock.Settings.copy(Blocks.FIRE)));

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

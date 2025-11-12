package aug.forgemaster;

import aug.forgemaster.block.ModBlocks;
import aug.forgemaster.effect.ModEffects;
import aug.forgemaster.enchantment.ModEnchantmentEffects;
import aug.forgemaster.item.ModItemGroups;
import aug.forgemaster.item.ModItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.item.Items;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Forgemaster implements ModInitializer {
	public static final String MOD_ID = "forgemaster";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static Identifier id(String path) {
		return Identifier.of(MOD_ID, path);
	}

	@Override
	public void onInitialize() {
		ModItems.register();
		ModBlocks.register();
		ModItemGroups.register();
		ModEffects.register();
		ModEnchantmentEffects.register();

		LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
            if (key.getValue().toString().equals("minecraft:gameplay/piglin_bartering")) {
                builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).weight(5)));
            }
		});
	}
}
package aug.forgemaster;

import aug.forgemaster.block.ModBlocks;
import aug.forgemaster.effect.ModEffects;
import aug.forgemaster.enchantment.ModEnchantmentEffects;
import aug.forgemaster.entity.ModEntities;
import aug.forgemaster.item.ModItemComponentTypes;
import aug.forgemaster.item.ModItemGroups;
import aug.forgemaster.item.ModItems;
import aug.forgemaster.particle.ModParticles;
import aug.forgemaster.util.ModTags;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.EnchantingContext;
import net.fabricmc.fabric.api.item.v1.EnchantmentEvents;
import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.registry.entry.RegistryEntry;
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
		ModItemComponentTypes.register();
		ModEntities.register();
		ModParticles.register();

		LootTableEvents.MODIFY.register((key, builder, source, registries) -> {
			if (key.equals(LootTables.PIGLIN_BARTERING_GAMEPLAY)) {
				builder.modifyPools(pool -> pool.with(ItemEntry.builder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).weight(5)));
			}
		});
        EnchantmentEvents.ALLOW_ENCHANTING.register((enchantment, target, context) -> {
            if (target.isOf(ModItems.ATTACCA) && enchantment.isIn(ModTags.Enchantments.TEMPERATURE_BASED)) {
                return TriState.FALSE;
            }

            return TriState.DEFAULT;
        });
	}
}
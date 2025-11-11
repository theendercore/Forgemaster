package aug.forgemaster;

import aug.forgemaster.effect.ModEffects;
import aug.forgemaster.enchantment.ModEnchantmentEffects;
import aug.forgemaster.item.ModItemGroups;
import aug.forgemaster.item.ModItems;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Forgemaster implements ModInitializer {
	public static final String MOD_ID = "forgemaster";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		ModItemGroups.registerModItemGroups();
		ModEffects.registerEffects();
		ModEnchantmentEffects.registerEnchantmentEffects();
	}
}
package aug.forgemaster.datagen;

import aug.forgemaster.enchantment.ModEnchantments;
import aug.forgemaster.world.gen.ModFeatures;
import aug.forgemaster.world.gen.ModStructures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ForgemasterDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		pack.addProvider(ModItemTagProvider::new);
		pack.addProvider(ModBlockTagProvider::new);
        pack.addProvider(ModBiomeTagProvider::new);
		pack.addProvider(ModModelProvider::new);
		pack.addProvider(ModRecipeProvider::new);
		pack.addProvider(ModRegistryDataGeneration::new);
		pack.addProvider(ModBlockLootTableProvider::new);
		pack.addProvider(ModEnchantmentTagProvider::new);
		pack.addProvider(ModAdvancementProvider::new);
	}

	@Override
	public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.ENCHANTMENT, ModEnchantments::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ModFeatures::bootstrapConfigured);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ModFeatures::bootstrapPlaced);
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE, ModStructures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.STRUCTURE_SET, ModStructures::bootstrapSet);
        registryBuilder.addRegistry(RegistryKeys.TEMPLATE_POOL, ModStructures::bootstrapPools);
	}
}

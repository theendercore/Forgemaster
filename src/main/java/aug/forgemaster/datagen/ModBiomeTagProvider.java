package aug.forgemaster.datagen;

import aug.forgemaster.util.ModTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.fabricmc.fabric.api.tag.convention.v2.ConventionalBiomeTags;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.world.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class ModBiomeTagProvider extends FabricTagProvider<Biome> {
    public ModBiomeTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> completableFuture) {
        super(output, RegistryKeys.BIOME, completableFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
        getOrCreateTagBuilder(ModTags.Biomes.HAS_CRATER)
                .addOptionalTag(ConventionalBiomeTags.IS_BIRCH_FOREST)
                .addOptionalTag(ConventionalBiomeTags.IS_DESERT)
                .addOptionalTag(ConventionalBiomeTags.IS_FLOWER_FOREST)
                .addOptionalTag(ConventionalBiomeTags.IS_FOREST)
                .addOptionalTag(ConventionalBiomeTags.IS_JUNGLE)
                .addOptionalTag(ConventionalBiomeTags.IS_MUSHROOM)
                .addOptionalTag(ConventionalBiomeTags.IS_OLD_GROWTH)
                .addOptionalTag(ConventionalBiomeTags.IS_PLAINS)
                .addOptionalTag(ConventionalBiomeTags.IS_SAVANNA)
                .addOptionalTag(ConventionalBiomeTags.IS_SNOWY_PLAINS)
                .addOptionalTag(ConventionalBiomeTags.IS_SPOOKY) // ??
                .addOptionalTag(ConventionalBiomeTags.IS_TAIGA)
                ;
    }
}

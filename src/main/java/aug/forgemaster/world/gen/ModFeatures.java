package aug.forgemaster.world.gen;

import aug.forgemaster.world.gen.configured_feature.CraterFeature;
import aug.forgemaster.world.gen.configured_feature.CraterFeatureConfig;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.floatprovider.ConstantFloatProvider;
import net.minecraft.util.math.floatprovider.UniformFloatProvider;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.RandomOffsetPlacementModifier;

import static aug.forgemaster.Forgemaster.id;
import static aug.forgemaster.util.Helpers.key;

public class ModFeatures {
    public static final CraterFeature CRATER = register("crater", new CraterFeature(CraterFeatureConfig.CODEC));

    private static <C extends FeatureConfig, F extends Feature<C>> F register(String name, F feature) {
        return Registry.register(Registries.FEATURE, id(name), feature);
    }

    public static final RegistryKey<ConfiguredFeature<?, ?>> C_CRATER = key(RegistryKeys.CONFIGURED_FEATURE, "crater");
    public static final RegistryKey<PlacedFeature> P_CRATER = key(RegistryKeys.PLACED_FEATURE, "crater");

    public static void bootstrapConfigured(Registerable<ConfiguredFeature<?, ?>> c) {
        ConfiguredFeatures.register(c, C_CRATER, CRATER, new CraterFeatureConfig(
                BlockTags.GEODE_INVALID_BLOCKS,
                UniformIntProvider.create(16, 28), // Width
                UniformIntProvider.create(6, 10), // Height
                UniformIntProvider.create(12, 24), // Clear Height

                ConstantFloatProvider.create(5), // Ring count / Size
                UniformFloatProvider.create(1.75f, 2), // Ring Height
                UniformFloatProvider.create(.35f, .5f), // Thickness

                UniformFloatProvider.create(.2f, .3f), // Offset Noise
                UniformFloatProvider.create(.8f, 1.1f) //  Texture noise
        ));
    }

    public static void bootstrapPlaced(Registerable<PlacedFeature> c) {
        var feat = c.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        PlacedFeatures.register(c,
                P_CRATER, feat.getOrThrow(C_CRATER), RandomOffsetPlacementModifier.horizontally(ConstantIntProvider.create(12))
        );
    }

    public static void register() {
    }
}

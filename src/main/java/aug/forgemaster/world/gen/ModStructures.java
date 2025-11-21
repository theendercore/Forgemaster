package aug.forgemaster.world.gen;

import aug.forgemaster.util.ModTags;
import com.mojang.datafixers.util.Pair;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.StructureSet;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.chunk.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.gen.chunk.placement.SpreadType;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;

import java.util.List;

import static aug.forgemaster.util.Helpers.key;

public class ModStructures {
    public static final RegistryKey<StructurePool> CRATER_POOL = key(RegistryKeys.TEMPLATE_POOL, "crater");
    public static final RegistryKey<Structure> CRATER = key(RegistryKeys.STRUCTURE, "crater");
    public static final RegistryKey<StructureSet> CRATER_SET = key(RegistryKeys.STRUCTURE_SET, "craters");

    public static void bootstrap(Registerable<Structure> c) {
        var biomes = c.getRegistryLookup(RegistryKeys.BIOME);
        var pools = c.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);

        c.register(CRATER, new JigsawStructure(
                new Structure.Config(biomes.getOrThrow(ModTags.Biomes.HAS_CRATER)),
                pools.getOrThrow(CRATER_POOL), 3,
                ConstantHeightProvider.create(YOffset.fixed(0)),
                false,
                Heightmap.Type.WORLD_SURFACE_WG
        ));
    }

    public static void bootstrapSet(Registerable<StructureSet> c) {
        var struct = c.getRegistryLookup(RegistryKeys.STRUCTURE);
        c.register(CRATER_SET, new StructureSet(
                struct.getOrThrow(CRATER),
                new RandomSpreadStructurePlacement(50, 35, SpreadType.LINEAR, 328438)
        ));
    }

    public static void bootstrapPools(Registerable<StructurePool> c) {
        var pools = c.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        var feat = c.getRegistryLookup(RegistryKeys.PLACED_FEATURE);
        c.register(CRATER_POOL, new StructurePool(
                pools.getOrThrow(StructurePools.EMPTY),
                List.of(Pair.of(StructurePoolElement.ofFeature(feat.getOrThrow(ModFeatures.P_CRATER)), 1)),
                StructurePool.Projection.TERRAIN_MATCHING
        ));
    }
}

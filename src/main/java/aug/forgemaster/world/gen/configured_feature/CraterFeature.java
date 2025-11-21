package aug.forgemaster.world.gen.configured_feature;

import aug.forgemaster.block.ModBlocks;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.noise.DoublePerlinNoiseSampler;
import net.minecraft.util.math.random.CheckedRandom;
import net.minecraft.util.math.random.ChunkRandom;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.HashMap;

import static java.lang.Math.*;

public class CraterFeature extends Feature<CraterFeatureConfig> {
    public CraterFeature(Codec<CraterFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(FeatureContext<CraterFeatureConfig> context) {
        var origin = context.getOrigin();
        var world = context.getWorld();
        var config = context.getConfig();

        var chunkRandom = new ChunkRandom(new CheckedRandom(world.getSeed()));
        var random = context.getRandom();

        var width = config.width.get(random);
        var widthSqr = width * width;
        var height = config.height.get(random);
        var blockClearRage = config.blockClearRage.get(random);


        var data = new ShapeData(
                widthSqr, height,
                config.ringSize.get(random), config.ringHeight.get(random), config.thickness.get(random),
                config.textureNoiseMultiplier.get(random), config.offsetNoiseMultiplier.get(random),
                DoublePerlinNoiseSampler.create(chunkRandom, -2, 1.0), chunkRandom
        );

        var area = BlockPos.iterate(origin.add(width, height, width), origin.add(-width, -height, -width));
        var map = new HashMap<Vec3d, BlockState>();
        for (BlockPos pos : area) {
            if (world.getDimension().logicalHeight() > pos.getY()) {
                var rPos = relative(pos, origin);
                var state = shape(rPos, pos, data);
                if (state != null) {
                    map.put(rPos, state);
                }
            }
        }

        var pairMap = new HashMap<Pair<Integer, Integer>, Integer>();
        for (var entry : map.entrySet()) {
            var pos = entry.getKey();
            var state = entry.getValue();

            var x = (pos.getX() * pos.getX()) / widthSqr;
            var z = (pos.getZ() * pos.getZ()) / widthSqr;
            var horizontalDist = sqrt(x + z);


            var realPos = origin.add((int) -pos.x, (int) -pos.y, (int) -pos.z);
            var wgPos = min(world.getChunk(realPos).sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, realPos.getX(), realPos.getZ()), world.getChunk(realPos).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, realPos.getX(), realPos.getZ()));

            realPos = realPos.up(wgPos - origin.getY()).up(height / -3);


            if (horizontalDist <= 0.75) {
                BlockPos finalRealPos = realPos;
                pairMap.compute(Pair.of(realPos.getX(), realPos.getZ()), (k, yLoc) -> (yLoc != null) ? max(yLoc, finalRealPos.getY()) : finalRealPos.getY());
            }

            setBlockState(world, realPos, state);
        }

        for (var entry : pairMap.entrySet()) {
            var key = entry.getKey();
            var pos = new BlockPos(key.getFirst(), entry.getValue(), key.getSecond());
            for (int i = 1; i <= blockClearRage; i++) {
                setBlockState(world, pos.up(i), Blocks.AIR.getDefaultState());
            }

        }

        var anchor = origin.up(1);
        while (true) {
            if (!world.getBlockState(anchor.down()).isReplaceable()) {
                var shard = ModBlocks.ATTACCA_SHARD.getDefaultState()
                        .with(HorizontalFacingBlock.FACING, Direction.Type.HORIZONTAL.random(random));
                world.setBlockState(anchor, shard, 3);
                break;
            }
            anchor = anchor.down();
        }
        return true;
    }

    private Vec3d relative(BlockPos pos, BlockPos origin) {
        var x = (origin.getX() - pos.getX());
        var y = (origin.getY() - pos.getY());
        var z = (origin.getZ() - pos.getZ());
        return new Vec3d(x, y, z);
    }

    private BlockState shape(Vec3d pos, BlockPos blockPos, ShapeData data) {
        var x = (pos.getX() * pos.getX()) / data.widthSqr;
        var scaledY = pos.getY() / data.height;
        var z = (pos.getZ() * pos.getZ()) / data.widthSqr;
        var horizontalDist = sqrt(x + z);

        if (horizontalDist >= 0.85) {
            if (horizontalDist >= 1) {
                return null;
            }
            if (data.random.nextBetween(0, (int) (2 * horizontalDist)) > 0) {
                return null;
            }
        }

        var thickness = data.thickness;
        var calcPos = (cos(horizontalDist * data.ringSize)) / (data.ringHeight);

        var noise = data.dps.sample(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (data.blockNoise != 0) {
            thickness += (horizontalDist * horizontalDist * noise * data.blockNoise);
        }

        var isInShape = scaledY >= (calcPos - thickness) && scaledY <= calcPos + thickness;

        if (isInShape) {
            var sample = noise * data.texturesNoise * 10;
            if (sample < -3) {
                return Blocks.MAGMA_BLOCK.getDefaultState();
            }
            if (sample > 3) {
                return Blocks.BLACKSTONE.getDefaultState();
            }
            return Blocks.NETHERRACK.getDefaultState();
        }

        return null;
    }

    record ShapeData(
            int widthSqr, int height,
            double ringSize, double ringHeight,
            double thickness,
            double texturesNoise, double blockNoise,
            DoublePerlinNoiseSampler dps, Random random
    ) {
    }
}

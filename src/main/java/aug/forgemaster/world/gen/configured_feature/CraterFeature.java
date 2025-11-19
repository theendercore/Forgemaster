package aug.forgemaster.world.gen.configured_feature;

import aug.forgemaster.block.ModBlocks;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
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
        var random = context.getRandom();
        var world = context.getWorld();
        var config = context.getConfig();

        var chunkRandom = new ChunkRandom(new CheckedRandom(world.getSeed()));
        var dps = DoublePerlinNoiseSampler.create(chunkRandom, -2, 1.0);

        var width = 20;//config.ringWidth.get(random);
        var height = 6;//config.ringHeight.get(random);
        var texturesNoise = 1;//((height + width) / 4.0) * config.noiseMultiplier.get(random);
        var blockNoise = 0.25;//((height + width) / 4.0) * config.noiseMultiplier.get(random);

        var blockClearRage = height * 2;

        var area = BlockPos.iterate(origin.add(width, height, width), origin.add(-width, -height, -width));
        var map = new HashMap<Vec3d, BlockState>();
        for (BlockPos pos : area) {
            if (world.getDimension().logicalHeight() > pos.getY()) {
                var rPos = relative(pos, origin);
                var state = shape(rPos, width, height, texturesNoise, blockNoise, dps, random, pos);
                if (state != null) {
                    map.put(rPos, state);
                }
            }
        }

        var radius = width * width;

        var pairMap = new HashMap<Pair<Integer, Integer>, Integer>();
        for (var entry : map.entrySet()) {
            var pos = entry.getKey();
            var state = entry.getValue();

            var x = (pos.getX() * pos.getX()) / radius;
            var z = (pos.getZ() * pos.getZ()) / radius;
            var horizontalDist = sqrt(x + z);


            var realPos = origin.add((int) -pos.x, (int) -pos.y, (int) -pos.z);
            var wgPos = min(world.getChunk(realPos).sampleHeightmap(Heightmap.Type.OCEAN_FLOOR_WG, realPos.getX(), realPos.getZ()), world.getChunk(realPos).sampleHeightmap(Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, realPos.getX(), realPos.getZ()));

            var movePos = new BlockPos.Mutable(realPos.getX(), wgPos + 1, realPos.getZ());
            while (true) {
                var testState = world.getBlockState(movePos.move(Direction.DOWN));
                if (!(testState.isIn(BlockTags.REPLACEABLE) || testState.isIn(BlockTags.LOGS)) || testState.isIn(BlockTags.OVERWORLD_CARVER_REPLACEABLES)) {
//                    setBlockState(world, movePos.toImmutable().up(), Blocks.OAK_LEAVES.getDefaultState());
                    wgPos = movePos.toImmutable().offset(Direction.DOWN).getY();
                    break;
                }
            }

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


        var anchor = origin;
        while (true) {
            if (!world.getBlockState(anchor.down()).isAir()) {
                world.setBlockState(anchor, ModBlocks.ATTACCA_SHARD.getDefaultState(), 3);
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

    private BlockState shape(Vec3d pos, int widthR, int heightR, double texturesNoise, double blockNoise, DoublePerlinNoiseSampler dps, Random random, BlockPos blockPos) {
        var radius = widthR * widthR;

        var x = (pos.getX() * pos.getX()) / radius;
        var scaledY = pos.getY() / heightR;
        var z = (pos.getZ() * pos.getZ()) / radius;
        var horizontalDist = sqrt(x + z);

        if (horizontalDist >= 0.85) {
            if (horizontalDist >= 1) {
                return null;
            }
            if (random.nextBetween(0, (int) (2 * horizontalDist)) > 0) {
                return null;
            }
        }

        var ringSize = 5;
        var ringHeight = 2;
        var bounds = 0.5;
        var calcPos = (cos(horizontalDist * ringSize)) / (ringHeight);

        var noise = dps.sample(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (blockNoise != 0) {
            bounds += (horizontalDist * horizontalDist * noise * blockNoise);
        }

        var isInShape = scaledY >= (calcPos - bounds) && scaledY <= calcPos + bounds;

        if (isInShape) {
            var sample = noise * texturesNoise * 10;
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
}

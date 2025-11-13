package aug.forgemaster.enchantment;

import aug.forgemaster.effect.ModEffects;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public record FireEngineEnchantmentEffect(EnchantmentLevelBasedValue radiusMultiplier) implements EnchantmentEntityEffect {
    public static final MapCodec<FireEngineEnchantmentEffect> CODEC = EnchantmentLevelBasedValue.CODEC.fieldOf("radius").xmap(FireEngineEnchantmentEffect::new, FireEngineEnchantmentEffect::radiusMultiplier);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d entityPos) {
        if (context.owner() != null && context.owner().fallDistance > 5) {
            int radius = (int) (radiusMultiplier.getValue(level) * (context.owner().fallDistance / 10)) + 3;

            for (LivingEntity target : context.owner().getWorld().getNonSpectatingEntities(LivingEntity.class, Box.from(context.owner().getPos()).expand(radius))) {
                if (target != context.owner()) {
                    target.addStatusEffect(new StatusEffectInstance(ModEffects.SCORCHED, 200), context.owner());
                }
            }

            BlockPos center = user.getBlockPos();

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = center.add(x, 0, z);
                    BlockState state = world.getBlockState(pos);

                    if (!state.isReplaceable() && state.isSolidBlock(world, pos)) {
                        while (!(state = world.getBlockState(pos.up())).isSolidBlock(world, pos.up()) && state.isReplaceable()) {
                            pos = pos.up();

                            if (!pos.isWithinDistance(center, radius)) {
                                break;
                            }
                        }
                    }

                    while (!(state = world.getBlockState(pos.down())).isSolidBlock(world, pos.down()) && state.isReplaceable()) {
                        pos = pos.down();
                    }

                    if (pos.isWithinDistance(center, radius)) {
                        world.setBlockState(pos, Blocks.FIRE.getDefaultState());

                        if (Math.random() < 0.1) {
                            world.spawnParticles(
                                    ParticleTypes.FLAME,
                                    pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                                    1,
                                    0, 0, 0,
                                    0.1
                            );
                        }
                    }
                }
            }
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}




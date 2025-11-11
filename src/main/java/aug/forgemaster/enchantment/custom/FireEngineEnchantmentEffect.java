package aug.forgemaster.enchantment.custom;

import aug.forgemaster.Forgemaster;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import org.apache.http.FormattedHeader;

import java.util.EventObject;

import static com.mojang.blaze3d.platform.GlStateManager.Viewport.getX;
import static net.minecraft.item.MaceItem.shouldDealAdditionalDamage;

public record FireEngineEnchantmentEffect() implements EnchantmentEntityEffect {
    public static final MapCodec<FireEngineEnchantmentEffect> CODEC = MapCodec.unit(FireEngineEnchantmentEffect::new);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d entityPos) {
        //fire ring

        if (context.owner() instanceof LivingEntity livingEntity) {
            int radius = (int) (level * (livingEntity.fallDistance / 10));

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




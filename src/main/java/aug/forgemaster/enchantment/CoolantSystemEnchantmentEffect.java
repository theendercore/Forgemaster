package aug.forgemaster.enchantment;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentEffectContext;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

public record CoolantSystemEnchantmentEffect(EnchantmentLevelBasedValue radiusMultiplier) implements EnchantmentEntityEffect {
    public static final MapCodec<FireEngineEnchantmentEffect> CODEC = EnchantmentLevelBasedValue.CODEC.fieldOf("radius").xmap(FireEngineEnchantmentEffect::new, FireEngineEnchantmentEffect::radiusMultiplier);

    @Override
    public void apply(ServerWorld world, int level, EnchantmentEffectContext context, Entity user, Vec3d entityPos) {
        if (context.owner().getMainHandStack().hasEnchantments()) {
            context.owner().addStatusEffect((StatusEffectInstance) StatusEffects.FIRE_RESISTANCE);
        }

    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> getCodec() {
        return CODEC;
    }
}
package aug.forgemaster.mixin;

import aug.forgemaster.effect.ModEffects;
import aug.forgemaster.item.AttaccaItem;
import aug.forgemaster.item.ModItemComponentTypes;
import aug.forgemaster.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityFireTicksMixin {
    @Unique
    public abstract boolean hasStatusEffect(RegistryEntry<StatusEffect> effect);

    @Shadow
    public abstract boolean damage(DamageSource source, float amount);

    @Shadow
    public abstract DamageSources getDamageSources();

    @Inject(
            method = "baseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private void baseTick(CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity living && living.getAttacker() != null) {
            ItemStack stack = living.getAttacker().getMainHandStack();

            if (hasStatusEffect(ModEffects.SPARKED)) {
                damage(getDamageSources().onFire(), 1.5f);
            }

            if (stack.isOf(ModItems.ATTACCA)) {
                stack.apply(ModItemComponentTypes.ATTACCA_CHARGE, 0, i -> MathHelper.clamp(i + 1, 0, AttaccaItem.MAX_CHARGE));
                living.getAttacker().setStackInHand(Hand.MAIN_HAND, stack);
            }
        }
    }
}

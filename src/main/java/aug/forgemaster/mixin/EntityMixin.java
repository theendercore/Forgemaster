package aug.forgemaster.mixin;

import aug.forgemaster.effect.ModEffects;
import aug.forgemaster.item.AttaccaItem;
import aug.forgemaster.item.ModItemComponentTypes;
import aug.forgemaster.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @ModifyArg(
            method = "baseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            ),
            index = 1
    )
    private float baseTick1(float amount) {
        if ((Object) this instanceof LivingEntity living && living.hasStatusEffect(ModEffects.SPARKED)) {
            amount += 1.5f;
        }

        return amount;
    }

    @Inject(
            method = "baseTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private void baseTick2(CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity living && living.getAttacker() != null) {
            ItemStack stack = living.getAttacker().getMainHandStack();

            if (stack.isOf(ModItems.ATTACCA)) {
                stack.apply(ModItemComponentTypes.ATTACCA_CHARGE, 0, i -> MathHelper.clamp(i + 1, 0, AttaccaItem.MAX_CAPACITY));
                living.getAttacker().setStackInHand(Hand.MAIN_HAND, stack);
            }
        }
    }

    @Inject(
            method = "isFireImmune",
            at = @At("HEAD"),
            cancellable = true
    )
    private void isFireImmune(CallbackInfoReturnable<Boolean> cir) {
        if ((Object) this instanceof LivingEntity living && living.hasStatusEffect(ModEffects.COOLANT)) {
            cir.setReturnValue(true);
        }
    }
}

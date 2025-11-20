package aug.forgemaster.mixin;

import aug.forgemaster.enchantment.ModEnchantmentEffects;
import aug.forgemaster.item.AttaccaItem;
import aug.forgemaster.item.ModItemComponentTypes;
import aug.forgemaster.item.ModItems;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractFireBlock.class)
public class AbstractFireBlockMixin {
    @Inject(
            method = "onEntityCollision",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private void baseTick(CallbackInfo ci) {
        if ((Object) this instanceof LivingEntity living && living.getAttacker() != null) {
            ItemStack stack = living.getAttacker().getMainHandStack();

            if (stack.isOf(ModItems.ATTACCA) && ModEnchantmentEffects.getAffannato(stack) > 0) {
                stack.apply(ModItemComponentTypes.ATTACCA_CHARGE, 0, i -> MathHelper.clamp(i + 1, 0, AttaccaItem.MAX_CHARGE));
                living.getAttacker().setStackInHand(Hand.MAIN_HAND, stack);
            }
        }
    }
}

package aug.forgemaster.mixin;

import aug.forgemaster.item.AttaccaItem;
import aug.forgemaster.item.ModItemComponentTypes;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class BonusAttackDamageMixin {
    @Inject(
            method = "attack",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/item/Item;getBonusAttackDamage(Lnet/minecraft/entity/Entity;FLnet/minecraft/entity/damage/DamageSource;)F"
            )
    )
    private void attack(Entity target, CallbackInfo ci, @Local(ordinal = 0) LocalFloatRef damage, @Local ItemStack stack) {
        if (stack.getItem() instanceof AttaccaItem && stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0) >= AttaccaItem.MAX_CHARGE) {
            damage.set(damage.get() + 6);
        }
    }
}

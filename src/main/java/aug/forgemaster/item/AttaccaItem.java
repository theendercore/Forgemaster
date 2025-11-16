package aug.forgemaster.item;

import aug.forgemaster.effect.ModEffects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class AttaccaItem extends SwordItem implements DualModelItem {
    public static final int MAX_CHARGE = 50;

    public AttaccaItem(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public Identifier worldModel() {
        return Registries.ITEM.getId(this).withSuffixedPath("_3d");
    }

    @Override
    public Identifier guiModel() {
        return Registries.ITEM.getId(this).withSuffixedPath("_2d");
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0) >= MAX_CHARGE) {
            stack.set(ModItemComponentTypes.ATTACCA_CHARGE, 0);
            target.addStatusEffect(new StatusEffectInstance(ModEffects.SCORCHED, 100));
        }

        return true;
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        return super.isItemBarVisible(stack) || stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0) != 0;
    }

    @Override
    public int getItemBarStep(ItemStack stack) {
        int charge = stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0);

        if (charge == 0 && super.isItemBarVisible(stack)) {
            return super.getItemBarStep(stack);
        }

        return MathHelper.clamp(Math.round(13 - charge * 13f / MAX_CHARGE), 0, 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0) == 0 ? super.getItemBarColor(stack) : 0xFFF48522;
    }
}
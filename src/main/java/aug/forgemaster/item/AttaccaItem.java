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
import org.lwjgl.glfw.GLFW;

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
        target.addStatusEffect(new StatusEffectInstance(ModEffects.SPARKED, 200));

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

        return MathHelper.clamp(Math.round(charge * 13f / MAX_CHARGE), 0, 13);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        int charge = stack.getOrDefault(ModItemComponentTypes.ATTACCA_CHARGE, 0);

        if (charge == 0) {
            return super.getItemBarColor(stack);
        }

        if (charge >= MAX_CHARGE) {
            float blend = (float) Math.sin(GLFW.glfwGetTime() * 4) / 2 + 0.5f;

            return (MathHelper.lerp(blend, 0xF8, 0xAF) << 16) | (MathHelper.lerp(blend, 0x9E, 0x40) << 8) | (MathHelper.lerp(blend, 0x44, 0x00));
        }

        return 0xFFF48522;
    }
}
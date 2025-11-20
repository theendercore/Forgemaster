package aug.forgemaster.enchantment;

import aug.forgemaster.Forgemaster;
import com.mojang.serialization.MapCodec;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.enchantment.effect.EnchantmentValueEffect;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.math.random.Random;
import org.apache.commons.lang3.mutable.MutableFloat;

public class ModEnchantmentEffects {

    public static final ComponentType<EnchantmentValueEffect> AFFANNATO_STRENGTH = Registry.register(Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, Forgemaster.id("affannato_strength"), ComponentType.<EnchantmentValueEffect>builder().codec(EnchantmentValueEffect.CODEC).build());

    private static MapCodec<? extends EnchantmentEntityEffect> register(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return Registry.register(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Forgemaster.id(name), codec);
    }

    public static float getAffannato(ItemStack stack) {
        MutableFloat strength = new MutableFloat(0);

        for (Object2IntMap.Entry<RegistryEntry<Enchantment>> entry : stack.getEnchantments().getEnchantmentEntries()) {
            entry.getKey().value().modifyValue(ModEnchantmentEffects.AFFANNATO_STRENGTH, Random.create(), entry.getIntValue(), strength);
        }

        return strength.floatValue();
    }

    public static void register() {
        Forgemaster.LOGGER.info("Registering Mod Enchantment Effects for " + Forgemaster.MOD_ID);
    }
}

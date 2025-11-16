package aug.forgemaster.enchantment;

import aug.forgemaster.Forgemaster;
import aug.forgemaster.util.ModTags;
import com.mojang.serialization.MapCodec;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.ItemTags;

import static aug.forgemaster.enchantment.ModEnchantmentEffects.COOLANT_SYSTEM;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> FIRE_ENGINE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Forgemaster.id("fire_engine"));

    public static final RegistryKey<Enchantment> COOLANT_SYSTEM =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Forgemaster.id("coolant_system"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, ModEnchantmentEffects.FIRE_ENGINE, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ModTags.Items.FIRED_UP),
                        3,
                        5,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        2,
                        AttributeModifierSlot.MAINHAND
                ))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new FireEngineEnchantmentEffect(EnchantmentLevelBasedValue.linear(1))));

        register(registerable, ModEnchantmentEffects.COOLANT_SYSTEM, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ItemTags.WEAPON_ENCHANTABLE),
                        items.getOrThrow(ModTags.Items.FIRED_UP),
                        3,
                        1,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(5, 7),
                        2,
                        AttributeModifierSlot.MAINHAND
                ))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.VICTIM,
                        new CoolantSystemEnchantmentEffect(EnchantmentLevelBasedValue.linear(1))));
    }


    private static void register(Registerable<Enchantment> registry, MapCodec<? extends EnchantmentEntityEffect> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}

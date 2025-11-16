package aug.forgemaster.enchantment;

import aug.forgemaster.Forgemaster;
import aug.forgemaster.effect.ModEffects;
import aug.forgemaster.util.ModTags;
import net.minecraft.component.EnchantmentEffectComponentTypes;
import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentLevelBasedValue;
import net.minecraft.enchantment.effect.EnchantmentEffectTarget;
import net.minecraft.enchantment.effect.entity.ApplyMobEffectEnchantmentEffect;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> FIRE_ENGINE =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Forgemaster.id("fire_engine"));

    public static final RegistryKey<Enchantment> COOLANT_SYSTEM =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Forgemaster.id("coolant_system"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var enchantments = registerable.getRegistryLookup(RegistryKeys.ENCHANTMENT);
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, FIRE_ENGINE, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ModTags.Items.FIRED_UP),
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

        register(registerable, COOLANT_SYSTEM, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ModTags.Items.FIRED_UP),
                        items.getOrThrow(ModTags.Items.FIRED_UP),
                        3,
                        1,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(5, 7),
                        2,
                        AttributeModifierSlot.MAINHAND
                ))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.ATTACKER,
                        new ApplyMobEffectEnchantmentEffect(
                                RegistryEntryList.of(ModEffects.SPARKED),
                                EnchantmentLevelBasedValue.constant(30),
                                EnchantmentLevelBasedValue.constant(30),
                                EnchantmentLevelBasedValue.constant(1),
                                EnchantmentLevelBasedValue.constant(1)
                        )));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}

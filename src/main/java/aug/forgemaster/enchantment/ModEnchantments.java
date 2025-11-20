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
import net.minecraft.enchantment.effect.value.AddEnchantmentEffect;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;

public class ModEnchantments {

    public static final RegistryKey<Enchantment> AFFANNATO =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Forgemaster.id("affannato"));

    public static final RegistryKey<Enchantment> COMODO_CON_FUOCO =
            RegistryKey.of(RegistryKeys.ENCHANTMENT, Forgemaster.id("comodo_con_fuoco"));

    public static void bootstrap(Registerable<Enchantment> registerable) {
        var items = registerable.getRegistryLookup(RegistryKeys.ITEM);

        register(registerable, AFFANNATO, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ModTags.Items.FIRED_UP),
                        items.getOrThrow(ModTags.Items.FIRED_UP),
                        3,
                        5,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(25, 9),
                        2,
                        AttributeModifierSlot.MAINHAND
                ))
                        .addNonListEffect(ModEnchantmentEffects.AFFANNATO_STRENGTH, new AddEnchantmentEffect(EnchantmentLevelBasedValue.linear(1, 0.5f))));

        register(registerable, COMODO_CON_FUOCO, Enchantment.builder(Enchantment.definition(
                        items.getOrThrow(ModTags.Items.FIRED_UP),
                        items.getOrThrow(ModTags.Items.FIRED_UP),
                        3,
                        3,
                        Enchantment.leveledCost(5, 7),
                        Enchantment.leveledCost(5, 7),
                        2,
                        AttributeModifierSlot.MAINHAND
                ))
                .addEffect(EnchantmentEffectComponentTypes.POST_ATTACK,
                        EnchantmentEffectTarget.ATTACKER, EnchantmentEffectTarget.ATTACKER,
                        new ApplyMobEffectEnchantmentEffect(
                                RegistryEntryList.of(ModEffects.COOLANT),
                                EnchantmentLevelBasedValue.linear(10, 10),
                                EnchantmentLevelBasedValue.linear(10, 10),
                                EnchantmentLevelBasedValue.constant(0),
                                EnchantmentLevelBasedValue.constant(0)
                        )));
    }

    private static void register(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }
}

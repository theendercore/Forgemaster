package aug.forgemaster.effect;

import aug.forgemaster.Forgemaster;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;

public class ModEffects {

    public static final RegistryEntry<StatusEffect> SCORCHED = registerStatusEffect("scorched",
            new ScorchedEffect(StatusEffectCategory.HARMFUL, 0x511515));

    public static final RegistryEntry<StatusEffect> SPARKED = registerStatusEffect("sparked",
            new ScorchedEffect(StatusEffectCategory.HARMFUL, 0x511515));

    private static RegistryEntry<StatusEffect> registerStatusEffect(String name, StatusEffect statusEffect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Forgemaster.id(name), statusEffect);
    }

    public static void register() {
        Forgemaster.LOGGER.info("Registering Mod Effects for " + Forgemaster.MOD_ID);
    }
}
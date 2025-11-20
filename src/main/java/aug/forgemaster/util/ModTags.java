package aug.forgemaster.util;

import aug.forgemaster.Forgemaster;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> FIRED_UP = of("fired_up");

        private static TagKey<Item> of(String name) {
            return TagKey.of(RegistryKeys.ITEM, Forgemaster.id(name));
        }
    }

    public static class Enchantments {
        public static final TagKey<Enchantment> TEMPERATURE_BASED = of("temperature_based");

        private static TagKey<Enchantment> of(String name) {
            return TagKey.of(RegistryKeys.ENCHANTMENT, Forgemaster.id(name));
        }
    }
}

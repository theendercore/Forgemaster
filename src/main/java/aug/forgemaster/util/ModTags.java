package aug.forgemaster.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.world.biome.Biome;

import static aug.forgemaster.Forgemaster.id;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> FIRED_UP = of("fired_up");

        private static TagKey<Item> of(String name) {
            return TagKey.of(RegistryKeys.ITEM, id(name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> HAS_CRATER = of("has_structure/crater");

        private static TagKey<Biome> of(String name) {
            return TagKey.of(RegistryKeys.BIOME, id(name));
        }
    }

    public static class Enchantments {
        public static final TagKey<Enchantment> TEMPERATURE_BASED = of("temperature_based");

        private static TagKey<Enchantment> of(String name) {
            return TagKey.of(RegistryKeys.ENCHANTMENT, id(name));
        }
    }
}

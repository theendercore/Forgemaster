package aug.forgemaster.util;

import aug.forgemaster.Forgemaster;
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
}

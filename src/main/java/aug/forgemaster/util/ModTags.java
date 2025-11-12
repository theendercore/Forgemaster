package aug.forgemaster.util;

import aug.forgemaster.Forgemaster;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> FIRED_UP = createTag("fired_up");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, Forgemaster.id(name));
        }


    }
}

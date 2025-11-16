package aug.forgemaster.item;

import aug.forgemaster.Forgemaster;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class ModItems {

    public static final Item BROKEN_ATTACCA = registerItem("broken_attacca", new Item(new Item.Settings().fireproof()));

    public static final Item ATTACCA = registerItem("attacca", new AttaccaItem(ToolMaterials.NETHERITE, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 4, -3.2f))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Forgemaster.id(name), item);
    }

    public static void register() {
        Forgemaster.LOGGER.info("Registering Mod Items for " + Forgemaster.MOD_ID);
    }
}

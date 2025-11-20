package aug.forgemaster.item;

import aug.forgemaster.Forgemaster;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import java.util.List;

public class ModItems {

    public static final Item BROKEN_ATTACCA = registerItem("broken_attacca", new Item(new Item.Settings().fireproof().component(DataComponentTypes.LORE, new LoreComponent(List.of(Text.translatable("item.forgemaster.broken_attacca.desc"))))));

    public static final Item ATTACCA = registerItem("attacca", new AttaccaItem(ToolMaterials.NETHERITE, new Item.Settings().fireproof().attributeModifiers(SwordItem.createAttributeModifiers(ToolMaterials.NETHERITE, 4, -2.8f)).component(DataComponentTypes.LORE, new LoreComponent(List.of(Text.translatable("item.forgemaster.attacca.desc"))))));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Forgemaster.id(name), item);
    }

    public static void register() {
        Forgemaster.LOGGER.info("Registering Mod Items for " + Forgemaster.MOD_ID);
    }
}

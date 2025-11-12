package aug.forgemaster.item;

import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public class AttaccaSword extends SwordItem implements DualModelItem {
    public AttaccaSword(ToolMaterial toolMaterial, Settings settings) {
        super(toolMaterial, settings);
    }

    @Override
    public Identifier worldModel() {
        return Registries.ITEM.getId(this).withSuffixedPath("_3d");
    }

    @Override
    public Identifier guiModel() {
        return Registries.ITEM.getId(this).withSuffixedPath("_2d");
    }
}
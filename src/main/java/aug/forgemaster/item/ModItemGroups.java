package aug.forgemaster.item;

import aug.forgemaster.Forgemaster;
import aug.forgemaster.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;

public class ModItemGroups {
    public static final RegistryKey<ItemGroup> FORGEMASTER_KEY = RegistryKey.of(RegistryKeys.ITEM_GROUP, Forgemaster.id("forgemaster"));
    public static final ItemGroup FORGEMASTER = Registry.register(
            Registries.ITEM_GROUP,
            FORGEMASTER_KEY,
            FabricItemGroup.builder().icon(() -> new ItemStack(ModItems.ATTACCA))
                    .displayName(Text.translatable("itemGroup.forgemaster.forgemaster"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.ATTACCA_SHARD);
                        entries.add(ModItems.BROKEN_ATTACCA);
                        entries.add(ModItems.ATTACCA);
                    }).build()
    );

    public static void register() {
        Forgemaster.LOGGER.info("Registering Item Groups for " + Forgemaster.MOD_ID);
    }
}
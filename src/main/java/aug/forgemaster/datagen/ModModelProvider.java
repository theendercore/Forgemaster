package aug.forgemaster.datagen;

import aug.forgemaster.block.ModBlocks;
import aug.forgemaster.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.List;

import static net.minecraft.data.client.BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator gen) {
        List<Identifier> voidFireFloor = gen.getFireFloorModels(ModBlocks.GREEK_FIRE);
        List<Identifier> voidFireSide = gen.getFireSideModels(ModBlocks.GREEK_FIRE);
        gen.blockStateCollector
                .accept(
                        MultipartBlockStateSupplier.create(ModBlocks.GREEK_FIRE)
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireFloor, v -> v))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R90)))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R180)))
                                .with(BlockStateModelGenerator.buildBlockStateVariants(voidFireSide, v -> v.put(VariantSettings.Y, VariantSettings.Rotation.R270)))
                );

        Identifier model = Registries.BLOCK.getId(ModBlocks.ATTACCA_SHARD).withPrefixedPath("block/");
        gen.blockStateCollector
                .accept(
                        VariantsBlockStateSupplier.create(ModBlocks.ATTACCA_SHARD, BlockStateVariant.create().put(VariantSettings.MODEL, model))
                                .coordinate(createNorthDefaultHorizontalRotationStates())
                );
    }

    @Override
    public void generateItemModels(ItemModelGenerator gen) {
        gen.register(ModBlocks.ATTACCA_SHARD.asItem(), Models.GENERATED);
        gen.register(ModItems.BROKEN_ATTACCA, Models.GENERATED);
    }
}

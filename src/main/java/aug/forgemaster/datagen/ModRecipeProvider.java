package aug.forgemaster.datagen;

import aug.forgemaster.Forgemaster;
import aug.forgemaster.block.ModBlocks;
import aug.forgemaster.item.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.block.Blocks;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.recipe.input.SmithingRecipeInput;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.Identifier;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    public void generate(RecipeExporter recipeExporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.ATTACCA_SHARD, 1)
                .pattern(" N ")
                .pattern("NFN")
                .pattern(" N ")
                .input('N', Items.NETHERITE_SCRAP)
                .input('F', Items.FIRE_CHARGE)
                .criterion("has_attacca_shard", FabricRecipeProvider.conditionsFromItem(ModBlocks.ATTACCA_SHARD))
                .offerTo(recipeExporter, Forgemaster.id("attacca_shard"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, ModItems.BROKEN_ATTACCA, 1)
                .pattern("SS")
                .pattern("SS")
                .pattern("SS")
                .input('S', ModBlocks.ATTACCA_SHARD)
                .criterion("has_attacca_shard", FabricRecipeProvider.conditionsFromItem(ModBlocks.ATTACCA_SHARD))
                .offerTo(recipeExporter, Forgemaster.id("broken_attacca"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, ModItems.ATTACCA, 1)
                .pattern("B")
                .pattern("B")
                .pattern("S")
                .input('B', ModItems.BROKEN_ATTACCA)
                .input('S', Items.STICK)
                .criterion("has_broken_attacca", FabricRecipeProvider.conditionsFromItem(ModItems.BROKEN_ATTACCA))
                .offerTo(recipeExporter, Forgemaster.id("attacca"));
    }
}

package aug.forgemaster.item.custom;

import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class AttaccaSword extends Item {
    private final int FireLineRange = 12;
    private final int FireConeRange = 6;

    public TypedActionResult<ItemStack> inventoryTick(PlayerEntity user, Hand hand) {

        return TypedActionResult.success(user.getStackInHand(hand));
    }




    public TypedActionResult<ItemStack> onCraft(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        BlockPos frontOfPlayer = user.getBlockPos().offset(user.getHorizontalFacing(), 2);



        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult<ItemStack> useOnBlock(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        BlockPos frontOfPlayer = user.getBlockPos().offset(user.getHorizontalFacing(), 1);



        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public AttaccaSword(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TootltipContext context, List<Text> tooltip, TooltipType options) {
        tooltip.add(Text.translatable("tooltip.forgemaster.attacca.tooltip"));
        super.appendTooltip(stack, context, tooltip, options);
    }

}
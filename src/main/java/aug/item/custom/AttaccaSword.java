public class AttaccaSword extends Item {
    private final FireLineRange = 12;
    private final FireConeRange = 

    public TypedActionResult<ItemStack> inventoryTick(PlayerEntity user, Hand hand) {

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult<ItemStack> postHit(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        BlockPos blockTarget = user.getBlockPos().offset(user.getHorizontalFacing(), 2);

        for (int x = 0; x < FireLineRange; x++) {
            
            world#setBlockState(blockTarget, Blocks.FIRE.getDefaultState());
            blockTarget++;
        }
        

        return TypedActionResult.success(user.getStackInHand(hand));
    }


    public TypedActionResult<ItemStack> onCraft(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        BlockPos frontOfPlayer = user.getBlockPos().offset(user.getHorizontalFacing(), 2);

        LightningEntity lightningBolt new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightningBolt.setPosition(frontOfPlayer.toCenterPos());
        world.spawnEntity(lightningBolt);

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public TypedActionResult<ItemStack> useOnBlock(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) {
            return TypedActionResult.pass(user.getStackInHand(hand));
        }

        BlockPos frontOfPlayer = user.getBlockPos().offset(user.getHorizontalFacing(), 1);

        LightningEntity lightningBolt new LightningEntity(EntityType.LIGHTNING_BOLT, world);
        lightningBolt.setPosition(frontOfPlayer.toCenterPos());
        world.spawnEntity(lightningBolt);

        return TypedActionResult.success(user.getStackInHand(hand));
    }

    public AttaccaSword(Settings settings) {
        super(settings);
    }
}
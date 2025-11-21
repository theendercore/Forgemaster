package aug.forgemaster.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractFireBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class GreekFireBlock extends AbstractFireBlock {
    public static final MapCodec<GreekFireBlock> CODEC = createCodec(GreekFireBlock::new);
    public static final IntProperty TIMER = Properties.AGE_15;

    public GreekFireBlock(Settings settings) {
        super(settings, 1);
        setDefaultState(getStateManager().getDefaultState().with(TIMER, 2));
    }

    @Override
    protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int charge = state.get(TIMER);

        if (charge <= 1) {
            world.setBlockState(pos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL);
        } else {
            System.out.println("Decreasing charge " + charge);
            world.setBlockState(pos, state.with(TIMER, charge - 1), Block.NOTIFY_ALL);
            world.scheduleBlockTick(pos, this, 20);
        }
    }

    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        super.onBlockAdded(state, world, pos, oldState, notify);

        if (!oldState.isOf(this)) {
            world.scheduleBlockTick(pos, this, 20);
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(TIMER);
    }

    @Override
    protected void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (!(entity instanceof ItemEntity)) {
            super.onEntityCollision(state, world, pos, entity);
        }
    }

    @Override
    protected MapCodec<? extends AbstractFireBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos
    ) {
        return canPlaceAt(state, world, pos) ? getDefaultState() : Blocks.AIR.getDefaultState();
    }

    @Override
    protected boolean isFlammable(BlockState state) {
        return true;
    }
}
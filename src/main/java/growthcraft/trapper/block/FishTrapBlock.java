package growthcraft.trapper.block;

import com.mojang.serialization.MapCodec;
import growthcraft.trapper.GrowthcraftTrapper;
import growthcraft.trapper.block.entity.FishTrapBlockEntity;
import growthcraft.trapper.init.GrowthcraftTrapperBlockEntities;
import growthcraft.trapper.lib.utils.BlockPropertiesUtils;
import growthcraft.trapper.lib.utils.BlockStateUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class FishTrapBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    // TODO: Convert to use AGE instead of ticking.
    // TODO: Move max age to config
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 1);
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final MapCodec<FishTrapBlock> CODEC = simpleCodec(FishTrapBlock::new);

    public FishTrapBlock() {
        this(BlockPropertiesUtils.getInitProperties("fish_trap_wooden", Blocks.OAK_PLANKS));
    }

    public FishTrapBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)));
    }

    @Override
    protected MapCodec<FishTrapBlock> codec() {
        return CODEC;
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        // TODO: Move to config
        return 1;
    }

    public int getAge(BlockState blockState) {
        return blockState.getValue(this.getAgeProperty());
    }

    public BlockState getStateForAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), Integer.valueOf(age));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, BlockHitResult pHitResult) {
        // TODO: Implement useWithoutItem
        return super.useWithoutItem(pState, pLevel, pPos, pPlayer, pHitResult);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState fluidState = context.getLevel().getFluidState(context.getClickedPos());
        BlockState blockState = this.defaultBlockState();
        return blockState.setValue(FACING, context.getClickedFace()).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, WATERLOGGED);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return Boolean.TRUE.equals(blockState.getValue(WATERLOGGED)) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return GrowthcraftTrapperBlockEntities.FISH_TRAP_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    /**
     * Handles the random tick event for the fishing trap block. This method increments the age of the block
     * under certain conditions, specifically based on the calculated trapping speed and a random chance.
     *
     * @param blockState  The current state of the block.
     * @param serverLevel The server level in which the block is located.
     * @param blockPos    The position of the block being ticked.
     * @param random      A random number generator.
     */
    @Override
    public void randomTick(@NotNull BlockState blockState, ServerLevel serverLevel, @NotNull BlockPos blockPos, @NotNull RandomSource random) {
        if (!serverLevel.isAreaLoaded(blockPos, 1)) return;
        int i = this.getAge(blockState);
        if (i < this.getMaxAge()) {
            // Get the percentage chance out of 100 to determine if we increment the age.
            int trappingSpeed = getTrappingSpeed(blockState, serverLevel, blockPos);
            if (random.nextInt(100) >= trappingSpeed) {
                serverLevel.setBlock(blockPos, this.getStateForAge(i + 1), 2);
            }
        }
    }

    /**
     * Determines the trapping speed based on the surrounding block states.
     * The trapping speed is calculated as a percentage based on the number of surrounding blocks that contain water.
     *
     * @param blockState  The state of the block for which the trapping speed is being calculated.
     * @param serverLevel The server level in which the block is located.
     * @param blockPos    The position of the block for which the trapping speed is being calculated.
     * @return The trapping speed as a percentage.
     */
    private int getTrappingSpeed(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos) {
        AtomicReference<Integer> chance = new AtomicReference<>(0);
        // Get the surrounding block states to determine if we have a fluid source block so that we can determine the
        // chance of progressing the trapping AGE.
        Map<String, BlockState> surroundingBlocks = BlockStateUtils.getSurroundingBlockStates(serverLevel, blockPos);
        surroundingBlocks.forEach((key, state) -> {
            if (state.getFluidState().is(Tags.Fluids.WATER)) {
                chance.getAndSet((chance.get() + 1));
            }
        });
        // Return the percentage based on the number of sides that have water.
        return chance.get() / 6 * 100;
    }

    public final boolean isMaxAge(BlockState pState) {
        return this.getAge(pState) >= this.getMaxAge();
    }

    @Override
    protected boolean isRandomlyTicking(@NotNull BlockState blockState) {
        return !this.isMaxAge(blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, GrowthcraftTrapperBlockEntities.FISH_TRAP_BLOCK_ENTITY.get(), (worldLevel, pos, state, blockEntity) -> blockEntity.tick());
    }

    @Override
    public void onRemove(BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (blockState.getBlock() != newBlockState.getBlock()) {
            try {
                FishTrapBlockEntity blockEntity = (FishTrapBlockEntity) level.getBlockEntity(blockPos);
                assert blockEntity != null;
                blockEntity.dropItems();
            } catch (Exception ex) {
                GrowthcraftTrapper.LOGGER.error(String.format("Unable to drop inventory items: Invalid blockEntity type at %s, expected FishTrapBlockEntity", blockPos));
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

}

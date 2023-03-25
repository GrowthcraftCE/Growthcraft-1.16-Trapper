package growthcraft.trapper.block;

import growthcraft.trapper.GrowthcraftTrapper;
import growthcraft.trapper.block.entity.FishtrapBlockEntity;
import growthcraft.trapper.init.GrowthcraftTrapperBlockEntities;
import growthcraft.trapper.utils.BlockPropertiesUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
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
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

public class FishtrapBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public FishtrapBlock() {
        super(BlockPropertiesUtils.getInitProperties("fishtrap_wooden", Blocks.OAK_PLANKS));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(WATERLOGGED, Boolean.valueOf(false)));

    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(!level.isClientSide) {
            // Play sound
            level.playSound(player, blockPos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, 1.0F, 1.0F);
            // Open the menu container
            try {
                FishtrapBlockEntity blockEntity = (FishtrapBlockEntity) level.getBlockEntity(blockPos);
                NetworkHooks.openGui(((ServerPlayer) player), blockEntity, blockPos);
            } catch (Exception ex) {
                GrowthcraftTrapper.LOGGER.error(String.format("%s unable to open FishtrapBlockEntity GUI at %s.", player.getDisplayName().getString(), blockPos));
            }
        } else {
            // Do nothing
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState p_49232_) {
        return RenderShape.MODEL;
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
        return Boolean.TRUE.equals(blockState.getValue(WATERLOGGED))
                ? Fluids.WATER.getSource(false)
                : super.getFluidState(blockState);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return GrowthcraftTrapperBlockEntities.FISHTRAP_BLOCK_ENTITY.get().create(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(
                blockEntityType,
                GrowthcraftTrapperBlockEntities.FISHTRAP_BLOCK_ENTITY.get(),
                (worldLevel, pos, state, blockEntity) -> ((FishtrapBlockEntity) blockEntity).tick()
        );
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (blockState.getBlock() != newBlockState.getBlock()) {
            try {
                FishtrapBlockEntity blockEntity = (FishtrapBlockEntity) level.getBlockEntity(blockPos);
                blockEntity.dropItems();
            } catch (Exception ex) {
                GrowthcraftTrapper.LOGGER.error(String.format("Invalid blockEntity type at %s, expected FishtrapBlockEntity", blockPos.toString()));
            }
        }
        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }
}

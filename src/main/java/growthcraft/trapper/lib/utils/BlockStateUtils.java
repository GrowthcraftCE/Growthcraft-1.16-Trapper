package growthcraft.trapper.lib.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockStateUtils {

    public static Map<String, Block> getSurroundingBlocks(Level level, BlockPos pos) {
        return getBlocksByDirection(level, pos, true, true);
    }

    public static Map<String, Block> getHorizontalBlocks(Level level, BlockPos pos) {
        return getBlocksByDirection(level, pos, true, false);
    }

    public static Map<String, Block> getVerticalBlocks(Level level, BlockPos pos) {
        return getBlocksByDirection(level, pos, false, true);
    }

    public static Map<BlockPos, BlockState> getHorizontalBlockStates(Level level, BlockPos pos) {
        return getBlockStatesByBlockPos(level, pos, true, false);
    }

    public static Map<BlockPos, BlockState> getVerticalBlockStates(Level level, BlockPos pos) {
        return getBlockStatesByBlockPos(level, pos, false, true);
    }

    public static Map<String, BlockState> getSurroundingBlockStates(Level level, BlockPos pos) {
        return getBlockStatesByDirection(level, pos, true, true);
    }

    private static Map<BlockPos, BlockState> getBlockStatesByBlockPos(Level world, BlockPos pos, boolean horizontal, boolean vertical) {
        Map<BlockPos, BlockState> blockMap = new HashMap<>();
        if (horizontal) {
            blockMap.put(pos.north(), world.getBlockState(pos.north()));
            blockMap.put(pos.east(), world.getBlockState(pos.east()));
            blockMap.put(pos.south(), world.getBlockState(pos.south()));
            blockMap.put(pos.west(), world.getBlockState(pos.west()));
        }
        if (vertical) {
            blockMap.put(pos.above(), world.getBlockState(pos.above()));
            blockMap.put(pos.below(), world.getBlockState(pos.below()));
        }
        return blockMap;
    }

    private static Map<String, Block> getBlocksByDirection(Level world, BlockPos pos, boolean horizontal, boolean vertical) {
        Map<String, Block> blockMap = new HashMap<>();
        if (horizontal) {
            blockMap.put("north", world.getBlockState(pos.north()).getBlock());
            blockMap.put("east", world.getBlockState(pos.east()).getBlock());
            blockMap.put("south", world.getBlockState(pos.south()).getBlock());
            blockMap.put("west", world.getBlockState(pos.west()).getBlock());
        }
        if (vertical) {
            blockMap.put("up", world.getBlockState(pos.above()).getBlock());
            blockMap.put("down", world.getBlockState(pos.below()).getBlock());
        }
        return blockMap;
    }

    private static Map<String, BlockState> getBlockStatesByDirection(Level world, BlockPos pos, boolean horizontal, boolean vertical) {
        Map<String, BlockState> blockMap = new HashMap<>();
        if (horizontal) {
            blockMap.put("north", world.getBlockState(pos.north()));
            blockMap.put("east", world.getBlockState(pos.east()));
            blockMap.put("south", world.getBlockState(pos.south()));
            blockMap.put("west", world.getBlockState(pos.west()));
        }
        if (vertical) {
            blockMap.put("up", world.getBlockState(pos.above()));
            blockMap.put("down", world.getBlockState(pos.below()));
        }
        return blockMap;
    }


}

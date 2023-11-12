package growthcraft.lib.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;

public class BlockStateUtils {

    public static Map<String, Block> getSurroundingBlocks(LevelReader world, BlockPos pos) {
        Map<String, Block> blockMap = new HashMap<>();
        blockMap.put("north", world.getBlockState(pos.north()).getBlock());
        blockMap.put("east", world.getBlockState(pos.east()).getBlock());
        blockMap.put("south", world.getBlockState(pos.south()).getBlock());
        blockMap.put("west", world.getBlockState(pos.west()).getBlock());
        blockMap.put("up", world.getBlockState(pos.above()).getBlock());
        blockMap.put("down", world.getBlockState(pos.below()).getBlock());
        return blockMap;
    }

    public static Map<String, Block> getSurroundingBlocks(Level level, BlockPos pos) {
        Map<String, Block> blockMap = new HashMap<>();
        blockMap.put("north", level.getBlockState(pos.north()).getBlock());
        blockMap.put("east", level.getBlockState(pos.east()).getBlock());
        blockMap.put("south", level.getBlockState(pos.south()).getBlock());
        blockMap.put("west", level.getBlockState(pos.west()).getBlock());
        blockMap.put("up", level.getBlockState(pos.above()).getBlock());
        blockMap.put("down", level.getBlockState(pos.below()).getBlock());
        return blockMap;
    }

    public static Map<BlockPos, BlockState> getSurroundingBlockPos(Level world, BlockPos pos) {
        Map<BlockPos, BlockState> blockMap = new HashMap<>();
        blockMap.put(pos.north(), world.getBlockState(pos.north()));
        blockMap.put(pos.east(), world.getBlockState(pos.east()));
        blockMap.put(pos.south(), world.getBlockState(pos.south()));
        blockMap.put(pos.west(), world.getBlockState(pos.west()));
        blockMap.put(pos.above(), world.getBlockState(pos.above()));
        blockMap.put(pos.below(), world.getBlockState(pos.below()));
        return blockMap;
    }

    public static Map<String, Block> getHorizontalBlocks(Level world, BlockPos pos) {
        Map<String, Block> blockMap = new HashMap<>();
        blockMap.put("north", world.getBlockState(pos.north()).getBlock());
        blockMap.put("east", world.getBlockState(pos.east()).getBlock());
        blockMap.put("south", world.getBlockState(pos.south()).getBlock());
        blockMap.put("west", world.getBlockState(pos.west()).getBlock());
        return blockMap;
    }

    public static Map<BlockPos, BlockState> getHorizontalBlockPos(Level world, BlockPos pos) {
        Map<BlockPos, BlockState> blockMap = new HashMap<>();
        blockMap.put(pos.north(), world.getBlockState(pos.north()));
        blockMap.put(pos.east(), world.getBlockState(pos.east()));
        blockMap.put(pos.south(), world.getBlockState(pos.south()));
        blockMap.put(pos.west(), world.getBlockState(pos.west()));
        return blockMap;
    }

    public static Map<BlockPos, BlockState> getVerticalBlockPos(Level world, BlockPos pos) {
        Map<BlockPos, BlockState> blockMap = new HashMap<>();
        blockMap.put(pos.above(), world.getBlockState(pos.above()));
        blockMap.put(pos.below(), world.getBlockState(pos.below()));
        return blockMap;
    }

    public static Map<String, BlockState> getSurroundingBlockStates(Level world, BlockPos pos) {
        Map<String, BlockState> blockMap = new HashMap<>();
        blockMap.put("north", world.getBlockState(pos.north()));
        blockMap.put("east", world.getBlockState(pos.east()));
        blockMap.put("south", world.getBlockState(pos.south()));
        blockMap.put("west", world.getBlockState(pos.west()));
        blockMap.put("up", world.getBlockState(pos.above()));
        blockMap.put("down", world.getBlockState(pos.below()));
        return blockMap;
    }
}

package growthcraft.trapper.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockPropertiesUtils {

    /**
     * Generate the initial block properties for the given block.
     *
     * @param blockType String with block type criteria.
     * @param block     Block to base the block properties from.
     * @return Customized block properties.
     */
    public static BlockBehaviour.Properties getInitProperties(String blockType, Block block) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.copy(block);

        switch (blockType) {
            case "fishtrap_wooden" -> {
                properties.sound(SoundType.WOOD);
                properties.strength(2.0F, 3.0F);
                properties.noOcclusion();
                properties.requiresCorrectToolForDrops();
                properties.isValidSpawn(BlockPropertiesUtils::never);
                properties.isRedstoneConductor(BlockPropertiesUtils::never);
                properties.isSuffocating(BlockPropertiesUtils::never);
                properties.isViewBlocking(BlockPropertiesUtils::never);
            }
            case "spawneggtrap" -> {
                properties.sound(SoundType.NETHERITE_BLOCK);
                properties.strength(2.0F, 3.0F);
                properties.noOcclusion();
                properties.isValidSpawn(BlockPropertiesUtils::never);
                properties.isRedstoneConductor(BlockPropertiesUtils::never);
                properties.isSuffocating(BlockPropertiesUtils::never);
                properties.isViewBlocking(BlockPropertiesUtils::never);
            }
            case "animal_trap" -> {
                properties.sound(SoundType.CHAIN);
                properties.strength(2.0F, 3.0F);
                properties.noOcclusion();
                properties.isValidSpawn(BlockPropertiesUtils::never);
                properties.isRedstoneConductor(BlockPropertiesUtils::never);
                properties.isSuffocating(BlockPropertiesUtils::never);
                properties.isViewBlocking(BlockPropertiesUtils::never);
            }
            default -> {
                // Do nothing.
            }
        }

        return properties;

    }


    private static Boolean never(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> entity) {
        return BlockPropertiesUtils.never(state, world, pos);
    }

    private static Boolean always(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> entity) {
        return BlockPropertiesUtils.always(state, world, pos);
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    private static boolean never(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }
}

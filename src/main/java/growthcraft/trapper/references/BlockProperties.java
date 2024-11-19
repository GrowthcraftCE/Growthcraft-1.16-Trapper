package growthcraft.trapper.references;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BlockProperties {
    /**
     * Generate the initial block properties for the given block.
     *
     * @param blockType String with block type criteria.
     * @param block     Block to base the block properties from.
     * @return Customized block properties.
     */
    public static BlockBehaviour.Properties getInitProperties(String blockType, Block block) {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties.ofFullCopy(block);

        switch (blockType) {
            case "fishtrap_wooden" -> {
                properties.sound(SoundType.WOOD);
                properties.strength(2.0F, 3.0F);
                properties.noOcclusion();
                properties.requiresCorrectToolForDrops();
                properties.isValidSpawn(BlockProperties::never);
                properties.isRedstoneConductor(BlockProperties::never);
                properties.isSuffocating(BlockProperties::never);
                properties.isViewBlocking(BlockProperties::never);
            }
            case "spawneggtrap" -> {
                properties.sound(SoundType.NETHERITE_BLOCK);
                properties.strength(2.0F, 3.0F);
                properties.noOcclusion();
                properties.isValidSpawn(BlockProperties::never);
                properties.isRedstoneConductor(BlockProperties::never);
                properties.isSuffocating(BlockProperties::never);
                properties.isViewBlocking(BlockProperties::never);
            }
            case "animal_trap" -> {
                properties.sound(SoundType.CHAIN);
                properties.strength(2.0F, 3.0F);
                properties.noOcclusion();
                properties.isValidSpawn(BlockProperties::never);
                properties.isRedstoneConductor(BlockProperties::never);
                properties.isSuffocating(BlockProperties::never);
                properties.isViewBlocking(BlockProperties::never);
            }
            default -> {
                // Do nothing.
            }
        }

        return properties;

    }


    private static Boolean never(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> entity) {
        return BlockProperties.never(state, world, pos);
    }

    private static Boolean always(BlockState state, BlockGetter world, BlockPos pos, EntityType<?> entity) {
        return BlockProperties.always(state, world, pos);
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos) {
        return true;
    }

    private static boolean never(BlockState state, BlockGetter world, BlockPos pos) {
        return false;
    }

    private BlockProperties() {
        /* Prevent automatic implementation of public constructor */
    }
}

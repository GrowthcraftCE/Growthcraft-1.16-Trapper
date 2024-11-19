package growthcraft.trapper.init;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static growthcraft.trapper.GrowthcraftTrapper.MODID;
import static growthcraft.trapper.init.GrowthcraftTrapperItems.ITEMS;

public class GrowthcraftTrapperBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> EXAMPLE_BLOCK = registerBlock("example_block", BlockBehaviour.Properties.of().mapColor(MapColor.STONE), true);


    /**
     * Registers a block with the specified unlocalized name, properties, and optionally registers a block item if specified.
     *
     * @param unlocalizedName   The unlocalized name of the block to register.
     * @param properties        The properties of the block to configure.
     * @param registerBlockItem Flag indicating whether to register a corresponding block item.
     * @return A DeferredBlock object representing the registered block.
     */
    private static DeferredBlock<Block> registerBlock(String unlocalizedName, BlockBehaviour.Properties properties, boolean registerBlockItem) {
        DeferredBlock<Block> registryObject = BLOCKS.registerSimpleBlock(unlocalizedName, properties);
        if (registerBlockItem) registerBlockItem(unlocalizedName, registryObject);
        return registryObject;
    }

    /**
     * Registers a block item with the specified unlocalized name and associated registry object.
     *
     * @param unlocalizedName The unlocalized name of the block item to register.
     * @param registryObject  The DeferredBlock object representing the block to be registered.
     */
    private static void registerBlockItem(String unlocalizedName, DeferredBlock<Block> registryObject) {
        ITEMS.registerSimpleBlockItem(unlocalizedName, registryObject);
    }
}

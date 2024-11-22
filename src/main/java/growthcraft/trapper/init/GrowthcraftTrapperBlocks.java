package growthcraft.trapper.init;

import growthcraft.trapper.block.FishTrapBlock;
import growthcraft.trapper.references.UnlocalizedNames;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static growthcraft.trapper.GrowthcraftTrapper.MODID;
import static growthcraft.trapper.init.GrowthcraftTrapperItems.ITEMS;

public class GrowthcraftTrapperBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    /*
    public static final DeferredBlock<Block> ANIMAL_TRAP_COPPER = registerBlock(
            UnlocalizedNames.ANIMAL_TRAP_COPPER,
            () -> new AnimalTrapBlock(1)
    );
    public static final DeferredBlock<Block> ANIMAL_TRAP_DIAMOND = registerBlock(
            UnlocalizedNames.ANIMAL_TRAP_DIAMOND,
            () -> new AnimalTrapBlock(4)
    );
    public static final DeferredBlock<Block> ANIMAL_TRAP_GOLD = registerBlock(
            UnlocalizedNames.ANIMAL_TRAP_GOLD,
            () -> new AnimalTrapBlock(3)
    );
    public static final DeferredBlock<Block> ANIMAL_TRAP_IRON = registerBlock(
            UnlocalizedNames.ANIMAL_TRAP_IRON,
            () -> new AnimalTrapBlock(2)
    );
    */

    public static final DeferredBlock<Block> FISH_TRAP_OAK = registerBlock(
            UnlocalizedNames.FISH_TRAP_OAK,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_ACACIA = registerBlock(
            UnlocalizedNames.FISH_TRAP_ACACIA,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_DARK_OAK = registerBlock(
            UnlocalizedNames.FISH_TRAP_DARK_OAK,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_BIRCH = registerBlock(
            UnlocalizedNames.FISH_TRAP_BIRCH,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_SPRUCE = registerBlock(
            UnlocalizedNames.FISH_TRAP_SPRUCE,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_JUNGLE = registerBlock(
            UnlocalizedNames.FISH_TRAP_JUNGLE,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_BAMBOO = registerBlock(
            UnlocalizedNames.FISH_TRAP_BAMBOO,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_CHERRY = registerBlock(
            UnlocalizedNames.FISH_TRAP_CHERRY,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_CRIMSON = registerBlock(
            UnlocalizedNames.FISH_TRAP_CRIMSON,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_MANGROVE = registerBlock(
            UnlocalizedNames.FISH_TRAP_MANGROVE,
            FishTrapBlock::new
    );

    public static final DeferredBlock<Block> FISH_TRAP_WARPED = registerBlock(
            UnlocalizedNames.FISH_TRAP_WARPED,
            FishTrapBlock::new
    );

    /*
    public static final DeferredBlock<Block> SPAWNEGGTRAP = registerBlock(
            UnlocalizedNames.SPAWN_EGG_TRAP,
            SpawnEggTrapBlock::new
    );
    */

    /**
     * Registers a block with the specified name and supplier, and automatically registers a corresponding block item.
     *
     * @param name  the name of the block to register
     * @param block the supplier that provides the block instance
     * @return a DeferredBlock object representing the registered block
     */
    private static DeferredBlock<Block> registerBlock(String name, Supplier<Block> block) {
        DeferredBlock<Block> registryObject = BLOCKS.register(name, block);
        registerBlockItem(name, registryObject);
        return registryObject;
    }
    
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

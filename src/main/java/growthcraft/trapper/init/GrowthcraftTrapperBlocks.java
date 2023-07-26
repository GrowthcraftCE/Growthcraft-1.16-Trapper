package growthcraft.trapper.init;

import growthcraft.trapper.block.FishtrapBlock;
import growthcraft.trapper.block.SpawnEggTrapBlock;
import growthcraft.trapper.shared.Reference;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class GrowthcraftTrapperBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Reference.MODID);

    public static final RegistryObject<Block> FISHTRAP_OAK = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_OAK,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_ACACIA = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_ACACIA,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_DARK_OAK = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_DARK_OAK,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_BIRCH = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_BIRCH,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_SPRUCE = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_SPRUCE,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_JUNGLE = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_JUNGLE,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_BAMBOO = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_BAMBOO,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_CHERRY = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_CHERRY,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_CRIMSON = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_CRIMSON,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_MANGROVE = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_MANGROVE,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> FISHTRAP_WARPED = registerBlock(
            Reference.UnlocalizedName.FISHTRAP_WARPED,
            FishtrapBlock::new
    );

    public static final RegistryObject<Block> SPAWNEGGTRAP = registerBlock(
            Reference.UnlocalizedName.SPAWNEGGTRAP,
            SpawnEggTrapBlock::new
    );

    private static RegistryObject<Block> registerBlock(String name, Supplier<Block> block) {
        RegistryObject<Block> registryObject = BLOCKS.register(name, block);
        if (!excludeBlockItemRegistry(registryObject.getId())) {
            registerBlockItem(name, registryObject);
        }
        return registryObject;
    }

    private static void registerBlockItem(String unlocalizedName, RegistryObject<Block> blockRegistryObject) {
        GrowthcraftTrapperItems.ITEMS.register(unlocalizedName, () -> new BlockItem(blockRegistryObject.get(), getDefaultItemProperties()));
    }

    private static Item.Properties getDefaultItemProperties() {
        Item.Properties properties = new Item.Properties();
        return properties;
    }

    public static boolean excludeBlockItemRegistry(ResourceLocation registryName) {
        ArrayList<String> excludeBlocks = new ArrayList<>();
        //excludeBlocks.add(Reference.MODID + ":" + Reference.UnlocalizedName.APPLE_TREE_FRUIT);
        return excludeBlocks.contains(registryName.toString());
    }
}

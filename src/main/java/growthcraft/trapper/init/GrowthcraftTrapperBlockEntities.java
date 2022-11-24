package growthcraft.trapper.init;

import growthcraft.trapper.block.entity.FishtrapBlockEntity;
import growthcraft.trapper.block.entity.SpawnEggTrapBlockEntity;
import growthcraft.trapper.shared.Reference;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GrowthcraftTrapperBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            ForgeRegistries.BLOCK_ENTITY_TYPES, Reference.MODID
    );

    public static final RegistryObject<BlockEntityType<FishtrapBlockEntity>> FISHTRAP_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            Reference.UnlocalizedName.FISHTRAP,
            () -> BlockEntityType.Builder.of(FishtrapBlockEntity::new,
                    GrowthcraftTrapperBlocks.FISHTRAP_BIRCH.get(),
                    GrowthcraftTrapperBlocks.FISHTRAP_ACACIA.get(),
                    GrowthcraftTrapperBlocks.FISHTRAP_DARK_OAK.get(),
                    GrowthcraftTrapperBlocks.FISHTRAP_OAK.get(),
                    GrowthcraftTrapperBlocks.FISHTRAP_JUNGLE.get(),
                    GrowthcraftTrapperBlocks.FISHTRAP_SPRUCE.get()

            ).build(null)
    );

    public static final RegistryObject<BlockEntityType<SpawnEggTrapBlockEntity>> SPAWNEGGTRAP_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            Reference.UnlocalizedName.SPAWNEGGTRAP,
            () -> BlockEntityType.Builder.of(SpawnEggTrapBlockEntity::new,
                    GrowthcraftTrapperBlocks.SPAWNEGGTRAP.get()
            ).build(null)
    );

    private GrowthcraftTrapperBlockEntities() {
        /* Disable automative default public constructor */
    }
}

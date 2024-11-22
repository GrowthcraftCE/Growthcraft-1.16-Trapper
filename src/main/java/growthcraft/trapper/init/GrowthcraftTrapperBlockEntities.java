package growthcraft.trapper.init;

import growthcraft.trapper.GrowthcraftTrapper;
import growthcraft.trapper.block.entity.FishTrapBlockEntity;
import growthcraft.trapper.references.UnlocalizedNames;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class GrowthcraftTrapperBlockEntities {

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            Registries.BLOCK_ENTITY_TYPE, GrowthcraftTrapper.MODID
    );

    public static final DeferredHolder<BlockEntityType<FishTrapBlockEntity>> FISH_TRAP_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            UnlocalizedNames.FISH_TRAP,
            () -> BlockEntityType.Builder.of(FishTrapBlockEntity::new,
                    GrowthcraftTrapperBlocks.FISH_TRAP_BIRCH.get()
                    //GrowthcraftTrapperBlocks.FISHTRAP_ACACIA.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_DARK_OAK.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_OAK.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_JUNGLE.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_SPRUCE.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_BAMBOO.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_CHERRY.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_CRIMSON.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_MANGROVE.get(),
                    //GrowthcraftTrapperBlocks.FISHTRAP_WARPED.get()
            ).build(null)
    );

    /*
    public static final RegistryObject<BlockEntityType<AnimalTrapBlockEntity>> ANIMAL_TRAP_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            Reference.UnlocalizedName.ANIMAL_TRAP,
            () -> BlockEntityType.Builder.of(AnimalTrapBlockEntity::new,
                    GrowthcraftTrapperBlocks.ANIMAL_TRAP_IRON.get(),
                    GrowthcraftTrapperBlocks.ANIMAL_TRAP_COPPER.get(),
                    GrowthcraftTrapperBlocks.ANIMAL_TRAP_GOLD.get(),
                    GrowthcraftTrapperBlocks.ANIMAL_TRAP_DIAMOND.get()
            ).build(null)
    );
    */

    /*
    public static final RegistryObject<BlockEntityType<SpawnEggTrapBlockEntity>> SPAWNEGGTRAP_BLOCK_ENTITY = BLOCK_ENTITIES.register(
            Reference.UnlocalizedName.SPAWNEGGTRAP,
            () -> BlockEntityType.Builder.of(SpawnEggTrapBlockEntity::new,
                    GrowthcraftTrapperBlocks.SPAWNEGGTRAP.get()
            ).build(null)
    );
    */

    private GrowthcraftTrapperBlockEntities() {
        /* Disable automative default public constructor */
    }
}

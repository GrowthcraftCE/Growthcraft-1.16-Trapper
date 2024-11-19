package growthcraft.trapper;

import com.mojang.logging.LogUtils;
import growthcraft.trapper.init.GrowthcraftTrapperBlocks;
import growthcraft.trapper.init.config.GrowthcraftTrapperConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

import static growthcraft.trapper.init.GrowthcraftTrapperBlocks.BLOCKS;
import static growthcraft.trapper.init.GrowthcraftTrapperCreativeTabs.CREATIVE_MODE_TABS;
import static growthcraft.trapper.init.GrowthcraftTrapperItems.ITEMS;

/**
 * The GrowthcraftTrapper mod class is the main entry point for the Growthcraft Trapper mod.
 * This class handles the registration of items, blocks, and creative tabs, as well as setup
 * and event listening mechanisms.
 */
@Mod(GrowthcraftTrapper.MODID)
public class GrowthcraftTrapper {
    public static final String MODID = "growthcraft_trapper";

    // TODO: Move to init package
    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "examplemod" namespace
    // Creates a creative tab with the id "examplemod:example_tab" for the example item, that is placed after the combat tab

    private static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Constructs a new instance of GrowthcraftTrapper. This constructor handles the registration of
     * various event listeners and mod configurations required for the Growthcraft Trapper mod.
     *
     * @param modEventBus  The event bus used for managing mod events, such as block and item registration.
     * @param modContainer The mod container containing the mod's configuration and other related information.
     */
    public GrowthcraftTrapper(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        modContainer.registerConfig(ModConfig.Type.COMMON, GrowthcraftTrapperConfig.SPEC);

        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);

        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");

        if (GrowthcraftTrapperConfig.logDirtBlock)
            LOGGER.info("DIRT BLOCK >> {}", BuiltInRegistries.BLOCK.getKey(Blocks.DIRT));

        LOGGER.info(GrowthcraftTrapperConfig.magicNumberIntroduction + GrowthcraftTrapperConfig.magicNumber);

        GrowthcraftTrapperConfig.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
            event.accept(GrowthcraftTrapperBlocks.EXAMPLE_BLOCK.asItem().getDefaultInstance());
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("Growthcraft Trapper initializing ...");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}

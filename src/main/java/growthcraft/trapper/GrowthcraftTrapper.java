package growthcraft.trapper;

import growthcraft.trapper.init.*;
import growthcraft.trapper.init.client.GrowthcraftTrapperBlockRenders;
import growthcraft.trapper.init.config.GrowthcraftTrapperConfig;
import growthcraft.trapper.shared.Reference;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Reference.MODID)
@Mod.EventBusSubscriber(modid = Reference.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class GrowthcraftTrapper {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MODID);

    public GrowthcraftTrapper() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetupEvent);

        GrowthcraftTrapperConfig.loadConfig();

        GrowthcraftTrapperBlocks.BLOCKS.register(modEventBus);
        GrowthcraftTrapperItems.ITEMS.register(modEventBus);
        GrowthcraftTrapperBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        GrowthcraftTrapperMenus.MENUS.register(modEventBus);

        GrowthcraftTrapperCreativeTabs.CREATIVE_MODE_TABS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Do nothing for now ...
    }

    private void clientSetupEvent(final FMLClientSetupEvent event) {
        GrowthcraftTrapperBlockRenders.registerBlockRenders();
        GrowthcraftTrapperMenus.registerMenus();
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info(String.format("%s is starting ...", Reference.NAME));
    }

}

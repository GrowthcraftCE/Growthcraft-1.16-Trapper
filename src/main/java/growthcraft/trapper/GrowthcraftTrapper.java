package growthcraft.trapper;

import growthcraft.trapper.init.GrowthcraftTrapperBlockEntities;
import growthcraft.trapper.init.GrowthcraftTrapperBlocks;
import growthcraft.trapper.init.GrowthcraftTrapperItems;
import growthcraft.trapper.init.GrowthcraftTrapperMenus;
import growthcraft.trapper.init.client.GrowthcraftTrapperBlockRenders;
import growthcraft.trapper.init.config.GrowthcraftTrapperConfig;
import growthcraft.trapper.shared.Reference;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
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
        modEventBus.addListener(this::registerCreativeModeTab);

        GrowthcraftTrapperConfig.loadConfig();

        GrowthcraftTrapperBlocks.BLOCKS.register(modEventBus);
        GrowthcraftTrapperItems.ITEMS.register(modEventBus);
        GrowthcraftTrapperBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        GrowthcraftTrapperMenus.MENUS.register(modEventBus);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Do nothing for now ...
    }

    private void clientSetupEvent(final FMLClientSetupEvent event) {
        GrowthcraftTrapperBlockRenders.registerBlockRenders();
        GrowthcraftTrapperMenus.registerMenus();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info(String.format("%s is starting ...", Reference.NAME));
    }

    public void registerCreativeModeTab(CreativeModeTabEvent.Register event) {
        GrowthcraftTrapper.LOGGER.warn("CREATIVE_TAB_TRAPPER is registering ...");

        event.registerCreativeModeTab(new ResourceLocation(Reference.MODID, "tab"), builder ->
                // Set name of tab to display
                builder.title(Component.translatable("item_group." + Reference.MODID + ".tab"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(GrowthcraftTrapperBlocks.FISHTRAP_OAK.get()))
                        // Add default items to tab
                        .displayItems((params, output) -> {
                            // Add blocks
                            GrowthcraftTrapperBlocks.BLOCKS.getEntries().forEach(
                                    blockRegistryObject -> {
                                        if (!GrowthcraftTrapperBlocks.excludeBlockItemRegistry(blockRegistryObject.getId())) {
                                            output.accept(new ItemStack(blockRegistryObject.get()));
                                        }
                                    }
                            );
                            // Add items
                            GrowthcraftTrapperItems.ITEMS.getEntries().forEach(itemRegistryObject -> {
                                if (!GrowthcraftTrapperItems.excludeItemRegistry(itemRegistryObject.getId())) {
                                    output.accept(new ItemStack(itemRegistryObject.get()));
                                }
                            });
                        })
        );
    }
}

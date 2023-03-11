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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

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
        GrowthcraftTrapper.LOGGER.warn("CREATIVE_TAB_DECO is registering ...");

        event.registerCreativeModeTab(new ResourceLocation(Reference.MODID, "tab"), builder ->
                // Set name of tab to display
                builder.title(Component.translatable("item_group." + Reference.MODID + ".tab"))
                        // Set icon of creative tab
                        .icon(() -> new ItemStack(GrowthcraftTrapperBlocks.FISHTRAP_OAK.get()))
                        // Add default items to tab
                        .displayItems((enabledFlags, populator, hasPermissions) -> {
                            // Add blocks
                            for (Field field : GrowthcraftTrapperBlocks.class.getFields()) {
                                if (field.getType() != RegistryObject.class) continue;

                                try {
                                    RegistryObject<Block> block = (RegistryObject) field.get(null);
                                    if (!GrowthcraftTrapperBlocks.excludeBlockItemRegistry(block.getId())) {
                                        populator.accept(new ItemStack(block.get()));
                                    }
                                } catch (IllegalAccessException e) {

                                }
                            }

                            // Add items
                            for (Field field : GrowthcraftTrapperItems.class.getFields()) {
                                if (field.getType() != RegistryObject.class) continue;

                                try {
                                    RegistryObject<Item> item = (RegistryObject) field.get(null);
                                    if (!GrowthcraftTrapperItems.excludeItemRegistry(item.getId())) {
                                        populator.accept(new ItemStack(item.get()));
                                    }
                                } catch (IllegalAccessException e) {

                                }
                            }
                        })
        );
    }

}

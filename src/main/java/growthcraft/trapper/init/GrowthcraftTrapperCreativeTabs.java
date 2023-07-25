package growthcraft.trapper.init;

import growthcraft.trapper.shared.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class GrowthcraftTrapperCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Reference.MODID);

    public static final RegistryObject<CreativeModeTab> CREATIVE_TAB = CREATIVE_MODE_TABS.register(Reference.UnlocalizedName.CREATIVE_TAB, () -> CreativeModeTab.builder()
            .title(Component.translatable("item_group." + Reference.MODID + ".tab"))
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> GrowthcraftTrapperBlocks.FISHTRAP_OAK.get().asItem().getDefaultInstance())
            .displayItems((parameters, output) -> {
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
            }).build());
}

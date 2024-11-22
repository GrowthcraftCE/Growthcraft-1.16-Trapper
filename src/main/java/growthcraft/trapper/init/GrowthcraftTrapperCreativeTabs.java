package growthcraft.trapper.init;

import growthcraft.trapper.references.UnlocalizedNames;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import static growthcraft.trapper.init.GrowthcraftTrapperItems.EXAMPLE_ITEM;

/**
 * GrowthcraftTrapperCreativeTabs is a utility class responsible for managing the registration of creative mode tabs
 * for the Growthcraft mod in Minecraft. The class ensures that the predefined creative tab is registered and available
 * for displaying custom items related to the mod.
 */
public class GrowthcraftTrapperCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "growthcraft");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> GROWTHCRAFT_TAB = registerCreativeTab();

    private GrowthcraftTrapperCreativeTabs() {
        /* Prevent automatic implementation of public constructor */

    }

    /**
     * Registers a new creative tab for the mod, or retrieves an existing one if it is already loaded.
     *
     * @return The creative tab for the Growthcraft mod.
     */
    private static DeferredHolder<CreativeModeTab, CreativeModeTab> registerCreativeTab() {
        return CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder().title(Component.translatable(UnlocalizedNames.CREATIVE_TAB_KEY)).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> EXAMPLE_ITEM.get().getDefaultInstance()).displayItems((parameters, output) -> {
            output.accept(EXAMPLE_ITEM.get());
        }).build());
    }

    public static CreativeModeTab getCreativeTab() {
        return BuiltInRegistries.CREATIVE_MODE_TAB.get(ResourceLocation.fromNamespaceAndPath("growthcraft", "tab"));
    }
}

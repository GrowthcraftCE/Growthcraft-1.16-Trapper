package growthcraft.trapper.init;

import growthcraft.trapper.references.UnlocalizedName;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredRegister;

import static growthcraft.trapper.GrowthcraftTrapper.MODID;
import static growthcraft.trapper.init.GrowthcraftTrapperItems.EXAMPLE_ITEM;
import static growthcraft.trapper.references.UnlocalizedName.CREATIVE_TAB;

public class GrowthcraftTrapperCreativeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final CreativeModeTab GROWTHCRAFT_TAB = registerCreativeTab();

    private GrowthcraftTrapperCreativeTabs() {
        /* Prevent automatic implementation of public constructor */
    }

    private static CreativeModeTab registerCreativeTab() {
        CreativeModeTab growthcraftTab;

        if (ModList.get().isLoaded(CREATIVE_TAB)) {
            growthcraftTab = BuiltInRegistries.CREATIVE_MODE_TAB.get(ResourceLocation.fromNamespaceAndPath(CREATIVE_TAB, "tab"));
        } else {
            // If Growthcraft is not installed, then create our own creatvie tab.
            growthcraftTab = CREATIVE_MODE_TABS.register(CREATIVE_TAB, () -> CreativeModeTab.builder().title(Component.translatable(UnlocalizedName.CREATIVE_TAB_KEY)).withTabsBefore(CreativeModeTabs.COMBAT).icon(() -> EXAMPLE_ITEM.get().getDefaultInstance()).displayItems((parameters, output) -> {
                output.accept(EXAMPLE_ITEM.get());
            }).build()).get();
        }
        return growthcraftTab;
    }
}

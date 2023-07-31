package growthcraft.trapper.init;

import growthcraft.trapper.screen.*;
import growthcraft.trapper.shared.Reference;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class GrowthcraftTrapperMenus {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(
            ForgeRegistries.MENU_TYPES, Reference.MODID
    );

    public static final RegistryObject<MenuType<AnimalTrapMenu>> ANIMAL_TRAP_MENU =
            registerMenuType(Reference.UnlocalizedName.ANIMAL_TRAP_CONTAINER, AnimalTrapMenu::new);

    public static final RegistryObject<MenuType<FishtrapMenu>> FISHTRAP_MENU =
            registerMenuType(Reference.UnlocalizedName.FISHTRAP, FishtrapMenu::new);

    public static final RegistryObject<MenuType<SpawnEggTrapMenu>> SPAWNEGGTRAP_MENU =
            registerMenuType(Reference.UnlocalizedName.SPAWNEGGTRAP, SpawnEggTrapMenu::new);

    private static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerMenuType(
            String name, IContainerFactory<T> factory) {
        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }

    public static void registerMenus() {
        MenuScreens.register(ANIMAL_TRAP_MENU.get(), AnimalTrapScreen::new);
        MenuScreens.register(FISHTRAP_MENU.get(), FishtrapScreen::new);
        MenuScreens.register(SPAWNEGGTRAP_MENU.get(), SpawnEggTrapScreen::new);
    }

}

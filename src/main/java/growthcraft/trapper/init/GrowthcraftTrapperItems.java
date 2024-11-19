package growthcraft.trapper.init;

import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import static growthcraft.trapper.GrowthcraftTrapper.MODID;

public class GrowthcraftTrapperItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredItem<Item> EXAMPLE_ITEM = registerSimpleFoodItem("example_item", 1, 2f);

    /**
     * Registers a simple food item with the specified properties.
     *
     * @param unlocalizedName the unlocalized name of the item
     * @param nutrition       the nutritional value of the food item
     * @param saturation      the saturation modifier of the food item
     * @return a DeferredItem representing the registered food item
     */
    private static DeferredItem<Item> registerSimpleFoodItem(String unlocalizedName, int nutrition, float saturation) {
        return ITEMS.registerSimpleItem(unlocalizedName, new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().nutrition(nutrition).saturationModifier(saturation).build()));
    }

    //private static DeferredItem<Item> registerItem(String unlocalizedName, Item item) {
    //
    //}
}

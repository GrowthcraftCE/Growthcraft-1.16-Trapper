package growthcraft.trapper.init;

import com.google.common.collect.Sets;
import growthcraft.trapper.shared.Reference;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import java.util.Collections;
import java.util.Set;

public class GrowthcraftTrapperLootTables {
    private static final Set<ResourceKey<LootTable>> LOOT_TABLES = Sets.newHashSet();
    private static final Set<ResourceKey<LootTable>> IMMUTABLE_KEYS = Collections.unmodifiableSet(LOOT_TABLES);

    public static final ResourceKey<LootTable> ANIMAL_TRAP_CARROT = register(Reference.LootTables.ANIMAL_TRAP_CARROT);
    public static final ResourceKey<LootTable> ANIMAL_TRAP_LEAVES = register(Reference.LootTables.ANIMAL_TRAP_LEAVES);
    public static final ResourceKey<LootTable> ANIMAL_TRAP_SEEDS = register(Reference.LootTables.ANIMAL_TRAP_SEEDS);
    public static final ResourceKey<LootTable> ANIMAL_TRAP_WHEAT = register(Reference.LootTables.ANIMAL_TRAP_WHEAT);
    public static final ResourceKey<LootTable> SPAWNEGGTRAP_WHEAT = register(Reference.LootTables.SPAWNEGGTRAP_WHEAT);

    private GrowthcraftTrapperLootTables() {
        /* Prevent generation of default public constructor. */
    }

    private static ResourceKey<LootTable> register(ResourceLocation resourceLocation) {
        return register(ResourceKey.create(Registries.LOOT_TABLE, resourceLocation));
    }

    private static ResourceKey<LootTable> register(ResourceKey<LootTable> lootTableResourceKey) {
        if (LOOT_TABLES.add(lootTableResourceKey)) {
            return lootTableResourceKey;
        } else {
            throw new IllegalArgumentException(lootTableResourceKey + " is already a registered built-in loot table");
        }
    }

    public static Set<ResourceKey<LootTable>> all() {
        return IMMUTABLE_KEYS;
    }
}

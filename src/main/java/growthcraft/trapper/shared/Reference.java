package growthcraft.trapper.shared;

import net.minecraft.resources.ResourceLocation;

public class Reference {
    public static final String MODID = "growthcraft_trapper";

    public static final String NAME = "Growthcraft Trapper";
    public static final String NAME_SHORT = "trapper";
    public static final String VERSION = "8.1.0";

    private Reference() { /* Prevent default public constructor */ }

    public static class UnlocalizedName {
        public static final String ANIMAL_TRAP = "animal_trap";
        public static final String ANIMAL_TRAP_CONTAINER = "animal_trap_container";

        public static final String ANIMAL_TRAP_COPPER = "animal_trap_copper";
        public static final String ANIMAL_TRAP_DIAMOND = "animal_trap_diamond";
        public static final String ANIMAL_TRAP_GOLD = "animal_trap_gold";
        public static final String ANIMAL_TRAP_IRON = "animal_trap_iron";

        public static final String FISHTRAP = "fishtrap";
        public static final String FISHTRAP_CONTAINER = "fishtrap_container";
        public static final String FISHTRAP_OAK = "fishtrap_oak";
        public static final String FISHTRAP_ACACIA = "fishtrap_acacia";
        public static final String FISHTRAP_SPRUCE = "fishtrap_spruce";
        public static final String FISHTRAP_JUNGLE = "fishtrap_jungle";
        public static final String FISHTRAP_DARK_OAK = "fishtrap_dark_oak";
        public static final String FISHTRAP_BIRCH = "fishtrap_birch";
        public static final String FISHTRAP_BAMBOO = "fishtrap_bamboo";
        public static final String FISHTRAP_CHERRY = "fishtrap_cherry";
        public static final String FISHTRAP_CRIMSON = "fishtrap_crimson";
        public static final String FISHTRAP_MANGROVE = "fishtrap_mangrove";
        public static final String FISHTRAP_WARPED = "fishtrap_warped";

        public static final String SPAWNEGGTRAP = "spawneggtrap";

        public static final String TAG_FISHING_BAIT = "fishing_bait";

        public static final String SOUND_FISHTRAP_OPEN = "fishtrap_open";
        public static final String CREATIVE_TAB = "tab";

        private UnlocalizedName() { /* Disable default public constructor. */ }

    }

    public static class LootTables {

        public static final ResourceLocation ANIMAL_TRAP_CARROT = new ResourceLocation(Reference.MODID, "gameplay/trapping/animal_trap_carrot");
        public static final ResourceLocation ANIMAL_TRAP_LEAVES = new ResourceLocation(Reference.MODID, "gameplay/trapping/animal_trap_leaves");
        public static final ResourceLocation ANIMAL_TRAP_SEEDS = new ResourceLocation(Reference.MODID, "gameplay/trapping/animal_trap_seeds");
        public static final ResourceLocation ANIMAL_TRAP_WHEAT = new ResourceLocation(Reference.MODID, "gameplay/trapping/animal_trap_wheat");
        public static final ResourceLocation FISHTRAP_BAIT = new ResourceLocation(Reference.MODID, "gameplay/trapping/fishtrap_bait");
        public static final ResourceLocation FISHTRAP_BAIT_FORTUNE = new ResourceLocation(Reference.MODID, "gameplay/trapping/fishtrap_bait_fortune");
        public static final ResourceLocation FISHTRAP_BAIT_JUNK = new ResourceLocation(Reference.MODID, "gameplay/trapping/fishtrap_bait_junk");
        public static final ResourceLocation SPAWNEGGTRAP_WHEAT = new ResourceLocation(Reference.MODID, "gameplay/trapping/spawneggtrap_wheat");
    }

}

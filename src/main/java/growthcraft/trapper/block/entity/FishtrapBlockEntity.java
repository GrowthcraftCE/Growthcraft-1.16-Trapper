package growthcraft.trapper.block.entity;

import growthcraft.trapper.GrowthcraftTrapper;
import growthcraft.trapper.block.FishtrapBlock;
import growthcraft.trapper.init.GrowthcraftTrapperBlockEntities;
import growthcraft.trapper.init.GrowthcraftTrapperTags;
import growthcraft.trapper.init.config.GrowthcraftTrapperConfig;
import growthcraft.trapper.lib.handler.WrappedInventoryHandler;
import growthcraft.trapper.lib.utils.BlockStateUtils;
import growthcraft.trapper.lib.utils.TickUtils;
import growthcraft.trapper.screen.FishtrapMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FishtrapBlockEntity extends BlockEntity implements BlockEntityTicker<FishtrapBlockEntity>, MenuProvider {

    private final int minTickFishing = TickUtils.toTicks(GrowthcraftTrapperConfig.getMinTickFishingInMinutes(), "minutes");
    private final int maxTickFishing = TickUtils.toTicks(GrowthcraftTrapperConfig.getMaxTickFishingInMinutes(), "minutes");
    private final ItemStackHandler itemStackHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> true;
                default -> false;
            };
        }
    };

    private int tickTimer = 0;
    private int tickCooldown = 0;
    private LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.empty();

    private final Map<Direction, LazyOptional<WrappedInventoryHandler>> directionWrappedHandlerMap =
            Map.of(
                    Direction.UP, LazyOptional.of(() -> new WrappedInventoryHandler(
                            itemStackHandler,
                            (index) -> index == 0,
                            (index, stack) -> itemStackHandler.isItemValid(0, stack))
                    ),
                    Direction.DOWN, LazyOptional.of(() -> new WrappedInventoryHandler(
                            itemStackHandler,
                            (i) -> i >= 1,
                            (i, s) -> false)
                    ),
                    Direction.NORTH, LazyOptional.of(() -> new WrappedInventoryHandler(
                            itemStackHandler,
                            (index) -> index == 0,
                            (index, stack) -> itemStackHandler.isItemValid(0, stack))
                    ),
                    Direction.SOUTH, LazyOptional.of(() -> new WrappedInventoryHandler(
                            itemStackHandler,
                            (index) -> index == 0,
                            (index, stack) -> itemStackHandler.isItemValid(0, stack))
                    ),
                    Direction.EAST, LazyOptional.of(() -> new WrappedInventoryHandler(
                            itemStackHandler,
                            (index) -> index == 0,
                            (index, stack) -> itemStackHandler.isItemValid(0, stack))
                    ),
                    Direction.WEST, LazyOptional.of(() -> new WrappedInventoryHandler(
                            itemStackHandler,
                            (index) -> index == 0,
                            (index, stack) -> itemStackHandler.isItemValid(0, stack))
                    )
            );

    private Component customName;

    public FishtrapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(GrowthcraftTrapperBlockEntities.FISHTRAP_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void tick() {
        if (this.getLevel() != null) {
            this.tick(this.getLevel(), this.getBlockPos(), this.getBlockState(), this);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, FishtrapBlockEntity fishtrapBlockEntity) {
        if (level.isClientSide) return;

        if (GrowthcraftTrapperConfig.isDebugEnabled() && (tickTimer % 100 == 0)) {
            GrowthcraftTrapper.LOGGER.debug(String.format("FishtrapBlockEntity [%s] - tickTimer - %d/%d ", blockPos.toShortString(), tickTimer, tickCooldown));
        }

        tickTimer++;
        if (tickCooldown != 0 && tickTimer > tickCooldown && canDoFishing(level, blockPos)) {
            this.doFishing(blockPos);
            tickTimer = 0;
            tickCooldown = TickUtils.getRandomTickCooldown(minTickFishing, maxTickFishing);
        } else if(tickCooldown == 0 && canDoFishing(level,blockPos)) {
            tickCooldown = TickUtils.getRandomTickCooldown(minTickFishing, maxTickFishing);
        }
    }

    /**
     * Determine if this entity is surrounded by fluid blocks.
     *
     * @param level World level
     * @param pos   BlockPos
     * @return Returns true if surrounded by proper Fluids.
     */
    @ParametersAreNonnullByDefault
    private boolean canDoFishing(Level level, BlockPos pos) {
        Map<String, Block> blockMap = BlockStateUtils.getSurroundingBlocks(level, pos);

        // Scenario 1 - BlockUp and BlockDown are water.
        if (blockMap.get("down") instanceof LiquidBlock
                && blockMap.get("up") instanceof LiquidBlock) {
            return true;
        }

        // Scenario 2 - BlockNorth, BlockEast, BlockSouth, and BlockWest are water.
        if (blockMap.get("north") instanceof LiquidBlock
                && blockMap.get("east") instanceof LiquidBlock
                && blockMap.get("south") instanceof LiquidBlock
                && blockMap.get("west") instanceof LiquidBlock) {
            return true;
        }

        // Scenario 3 - Horizontal blocks are Water and Block is WATERLOGGED.
        boolean eastWest = blockMap.get("east") instanceof LiquidBlock
                && blockMap.get("west") instanceof LiquidBlock;
        boolean northSouth = blockMap.get("north") instanceof LiquidBlock
                && blockMap.get("south") instanceof LiquidBlock;

        return (eastWest || northSouth) && this.getBlockState().getValue(BlockStateProperties.WATERLOGGED).equals(Boolean.TRUE);
    }

    private void doFishing(BlockPos blockPos) {
        if (level == null) return;

        // Check for any bait in slot 0
        ItemStack baitItemStack = itemStackHandler.getStackInSlot(0);

        LootTable lootTable;

        int luck = 0;

        // Virtual Fishing Rod for calculating the loot tables.
        ItemStack fishingRod = new ItemStack(Items.FISHING_ROD);

        if (baitItemStack.is(GrowthcraftTrapperTags.Items.FISHTRAP_BAIT_FORTUNE)) {
            luck = 3;
            fishingRod.enchant(Enchantments.LUCK_OF_THE_SEA, luck);
            // Fish from the Treasure Loot Table
            lootTable = Objects.requireNonNull(level.getServer().reloadableRegistries()).getLootTable(BuiltInLootTables.FISHING_TREASURE);
        } else if (baitItemStack.is(GrowthcraftTrapperTags.Items.FISHTRAP_BAIT)) {
            // Fish from the Standard Loot Table
            lootTable = Objects.requireNonNull(level.getServer().reloadableRegistries()).getLootTable(BuiltInLootTables.FISHING);
        } else {
            // Fish from the Junk Loot Table
            lootTable = Objects.requireNonNull(level.getServer().reloadableRegistries()).getLootTable(BuiltInLootTables.FISHING_JUNK);
        }

        LootParams lootContext = new LootParams.Builder((ServerLevel) level)
                .withLuck(luck)
                .withParameter(LootContextParams.ORIGIN, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                .withParameter(LootContextParams.TOOL, fishingRod)
                .create(LootContextParamSets.FISHING);

        List<ItemStack> lootItemStacks = lootTable.getRandomItems(lootContext);

        for (ItemStack itemStack : lootItemStacks) {
            for (int i = 1; i < itemStackHandler.getSlots(); i++) {
                ItemStack storedItemStack = itemStackHandler.getStackInSlot(i);
                if (itemStackHandler.getStackInSlot(i).isEmpty() || storedItemStack.getItem() == itemStack.getItem()) {
                    itemStackHandler.setStackInSlot(i, new ItemStack(itemStack.getItem(), itemStackHandler.getStackInSlot(i).getCount() + 1));
                    break;
                }
            }
        }

        itemStackHandler.getStackInSlot(0).shrink(1);

        this.getLevel().playSound(null, this.worldPosition, SoundEvents.FISHING_BOBBER_RETRIEVE, SoundSource.BLOCKS, 0.5F, 0.5F);

    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return this.serializeCaps();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag, HolderLookup.Provider provider) {
        this.loadAdditional(tag, provider);
    }

    @Override
    public void loadAdditional(CompoundTag nbt, HolderLookup.Provider provider) {
        super.loadAdditional(nbt, provider);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.tickTimer = nbt.getInt("tickTimer");
        this.tickCooldown = nbt.getInt("tickCooldown");
        if (nbt.contains("CustomName", 8)) {
            this.customName = Component.Serializer.fromJson(nbt.getString("CustomName"), provider);
        }
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt, HolderLookup.Provider provider) {
        this.loadWithComponents(pkt.getTag(), provider);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        itemHandlerLazyOptional = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        itemHandlerLazyOptional.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt, HolderLookup .Provider provider) {
        super.saveAdditional(nbt, provider);

        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt("tickTimer", this.tickTimer);
        nbt.putInt("tickCooldown", this.tickCooldown);
        if (this.customName != null) {
            nbt.putString("CustomName", Component.Serializer.toJson(this.customName, provider));
        }
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.growthcraft_trapper.fishtrap");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new FishtrapMenu(containerId, inventory, this);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == null) return itemHandlerLazyOptional.cast();

            if(directionWrappedHandlerMap.containsKey(side)) {
                return directionWrappedHandlerMap.get(side).cast();
            }
        }
        return super.getCapability(cap, side);
    }

    public void dropItems() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        for (int i = 0; i < itemStackHandler.getSlots(); i++) {
            inventory.setItem(i, itemStackHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.getLevel(), this.worldPosition, inventory);
    }

}

package growthcraft.trapper.block.entity;

import growthcraft.trapper.GrowthcraftTrapper;
import growthcraft.trapper.init.GrowthcraftTrapperBlockEntities;
import growthcraft.trapper.init.GrowthcraftTrapperLootTables;
import growthcraft.trapper.init.config.GrowthcraftTrapperConfig;
import growthcraft.trapper.lib.handler.WrappedInventoryHandler;
import growthcraft.trapper.lib.utils.BlockStateUtils;
import growthcraft.trapper.lib.utils.TickUtils;
import growthcraft.trapper.screen.SpawnEggTrapMenu;
import growthcraft.trapper.shared.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.state.BlockState;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SpawnEggTrapBlockEntity extends BlockEntity implements BlockEntityTicker<SpawnEggTrapBlockEntity>, MenuProvider {

    private final int minTick = TickUtils.toTicks(GrowthcraftTrapperConfig.getMinTickTrappingInMinutes(), "minutes");
    private final int maxTick = TickUtils.toTicks(GrowthcraftTrapperConfig.getMaxTickTrappingInMinutes(), "minutes");
    private int tickTimer = 0;
    private int tickCooldown = 0;

    private Component customName;

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

    public SpawnEggTrapBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(GrowthcraftTrapperBlockEntities.SPAWNEGGTRAP_BLOCK_ENTITY.get(), blockPos, blockState);
    }

    public void tick() {
        if (this.getLevel() != null) {
            this.tick(this.getLevel(), this.getBlockPos(), this.getBlockState(), this);
        }
    }

    @Override
    public void tick(Level level, BlockPos blockPos, BlockState blockState, SpawnEggTrapBlockEntity blockEntity) {
        if (level.isClientSide) return;

        if (GrowthcraftTrapperConfig.isDebugEnabled() && (tickTimer % 100 == 0)) {
            GrowthcraftTrapper.LOGGER.debug(String.format("SpawnEggTrapBlockEntity [%s] - tickTimer - %d/%d ", blockPos.toShortString(), tickTimer, tickCooldown));
        }

        tickTimer++;
        if (tickCooldown != 0 && tickTimer > tickCooldown && canDoTrapping(level, blockPos)) {
            this.doTrapping(blockPos);
            tickTimer = 0;
            tickCooldown = TickUtils.getRandomTickCooldown(minTick, maxTick);
        } else if(tickCooldown == 0 && canDoTrapping(level,blockPos)) {
            tickCooldown = TickUtils.getRandomTickCooldown(minTick, maxTick);
        }
    }

    private boolean canDoTrapping(Level level, BlockPos blockPos) {
        // The SpawnEggTrap needs to be at ground level and needs to be surrounded by solid blocks
        // and the above be air.
        Map<String, Block> blockMap = BlockStateUtils.getSurroundingBlocks(level, blockPos);

        if (blockMap.get("up") != Blocks.AIR) return false;

        List<Block> horizontalBlocks = Arrays.asList(blockMap.get("north"), blockMap.get("north"), blockMap.get("north"), blockMap.get("north"));

        for (Block block : horizontalBlocks) {
            if (block == Blocks.AIR || block instanceof LiquidBlock) return false;
        }

        return true;
    }

    private void doTrapping(@NotNull BlockPos blockPos) {
        if (level == null) return;

        ItemStack baitItemStack = itemStackHandler.getStackInSlot(0);

        LootTable lootTable;

        String lootTableType = "";

        // Depending on the bait that was used, determines what gets caught.
        if (baitItemStack.getItem() == Items.WHEAT) {
            lootTableType = "wheat";
        }

        // TODO: Add additional baits.

        switch (lootTableType) {
            case "wheat":
                lootTable = Objects.requireNonNull(level.getServer().reloadableRegistries()).getLootTable(GrowthcraftTrapperLootTables.SPAWNEGGTRAP_WHEAT);
                break;
            default:
                lootTable = Objects.requireNonNull(level.getServer().reloadableRegistries()).getLootTable(BuiltInLootTables.EMPTY);
        }

        // If loot table is null, fail now.
        if (lootTable == null) return;

        LootParams lootContext = new LootParams.Builder((ServerLevel) level)
                .withParameter(LootContextParams.ORIGIN, new Vec3(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                .create(LootContextParamSets.EMPTY);

        List<ItemStack> lootItemStacks = lootTable.getRandomItems(lootContext);

        for (ItemStack itemStack : lootItemStacks) {
            if (GrowthcraftTrapperConfig.isDebugEnabled() && (tickTimer % 100 == 0)) {
                GrowthcraftTrapper.LOGGER.debug(
                        String.format("SpawnEggTrapBlockEntity [%s] - doTrapping - Caught a %s from %s loot table.", blockPos.toShortString(), itemStack, lootTableType)
                );
            }

            if (itemStack.getItem() != Items.AIR) {
                for (int i = 1; i < itemStackHandler.getSlots(); i++) {
                    ItemStack storedItemStack = itemStackHandler.getStackInSlot(i);
                    if (itemStackHandler.getStackInSlot(i).isEmpty() || storedItemStack.getItem() == itemStack.getItem()) {
                        itemStackHandler.setStackInSlot(i, new ItemStack(itemStack.getItem(), itemStackHandler.getStackInSlot(i).getCount() + 1));
                        break;
                    }
                }
            }
        }

        itemStackHandler.getStackInSlot(0).shrink(1);

        this.getLevel().playSound(null, this.worldPosition, SoundEvents.TRIPWIRE_CLICK_ON, SoundSource.BLOCKS, 0.5F, 0.5F);

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
        this.loadAdditional(pkt.getTag(), provider);
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
        nbt.put("inventory", itemStackHandler.serializeNBT());
        nbt.putInt("tickTimer", this.tickTimer);
        nbt.putInt("tickCooldown", this.tickCooldown);
        if (this.customName != null) {
            nbt.putString("CustomName", Component.Serializer.toJson(this.customName, provider));
        }
        super.saveAdditional(nbt, provider);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.growthcraft_trapper.spawneggtrap");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new SpawnEggTrapMenu(containerId, inventory, this);
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

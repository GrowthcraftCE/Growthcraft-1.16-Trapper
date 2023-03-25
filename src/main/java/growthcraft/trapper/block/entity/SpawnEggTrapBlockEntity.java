package growthcraft.trapper.block.entity;

import growthcraft.lib.utils.BlockStateUtils;
import growthcraft.lib.utils.TickUtils;
import growthcraft.trapper.init.GrowthcraftTrapperBlockEntities;
import growthcraft.trapper.screen.SpawnEggTrapMenu;
import growthcraft.trapper.shared.Reference;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class SpawnEggTrapBlockEntity extends BlockEntity implements BlockEntityTicker<SpawnEggTrapBlockEntity>, MenuProvider {

    private final int minTick = TickUtils.toTicks(1, "minutes");
    private final int maxTick = TickUtils.toTicks(5, "minutes");
    private int tickTimer = 0;
    private int tickCooldown = 0;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(7) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    private LazyOptional<IItemHandler> itemHandlerLazyOptional = LazyOptional.empty();

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

        tickTimer++;
        if (tickTimer >= tickCooldown && canDoTrapping(level, blockPos)) {
            this.doTrapping();
            tickTimer = 0;
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

    private void doTrapping() {
        ItemStack baitItemStack = itemStackHandler.getStackInSlot(0);

        LootContext.Builder lootContext$builder = new LootContext.Builder((ServerLevel) level).withRandom(new SecureRandom());

        String lootTableType = "";
        LootTable lootTable;

        // Depending on the bait that was used, determines what gets caught.
        if (baitItemStack.getItem() == Items.WHEAT) {
            lootTableType = "wheat";
        }

        lootTable = getLootTable(lootTableType);

        List<ItemStack> lootItemStacks = lootTable.getRandomItems(lootContext$builder.create(LootContextParamSets.EMPTY));
        for (ItemStack itemStack : lootItemStacks) {
            if (itemStack.getItem() != Items.AIR) {
                for (int i = 1; i < itemStackHandler.getSlots(); i++) {
                    ItemStack storedItemStack = itemStackHandler.getStackInSlot(i);
                    if (itemStackHandler.getStackInSlot(i).isEmpty() || storedItemStack.getItem() == itemStack.getItem()) {
                        itemStackHandler.insertItem(i, itemStack, false);
                        break;
                    }
                }
            }
        }

        itemStackHandler.getStackInSlot(0).shrink(1);

    }

    private LootTable getLootTable(String tableType) {
        ResourceLocation lootTable;

        switch (tableType) {
            case "wheat":
                lootTable = Reference.LootTables.SPAWNEGGTRAP_WHEAT;
                break;
            default:
                lootTable = BuiltInLootTables.EMPTY;
                break;
        }

        return ServerLifecycleHooks.getCurrentServer().getLootTables().get(lootTable);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.serializeNBT();
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.load(tag);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        this.load(pkt.getTag());
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
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemStackHandler.serializeNBT());
        super.saveAdditional(nbt);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.growthcraft_trapper.spawneggtrap");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new SpawnEggTrapMenu(containerId, inventory, this);
    }

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return itemHandlerLazyOptional.cast();
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

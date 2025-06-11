package net.reconhalcyon.reconsilkspawners.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;
import net.reconhalcyon.reconsilkspawners.Config;
import net.reconhalcyon.reconsilkspawners.ReconSilkSpawners;
import net.reconhalcyon.reconsilkspawners.util.SpawnerValidator;

public class SpawnerBreakHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Player player = event.getPlayer();

        // Skip if Silk Touch feature is disabled
        if (!Config.enableSilkTouchSpawners) return;

        // Only handle spawners
        if (state.getBlock() != Blocks.SPAWNER) return;

        ItemStack tool = player.getMainHandItem();

        // Registry-based Silk Touch check (1.21.1-compliant)
        RegistryAccess registryAccess = level.registryAccess();
        var enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
        Holder<Enchantment> silkTouch = enchantmentRegistry.getHolderOrThrow(Enchantments.SILK_TOUCH);

        if (tool.getEnchantmentLevel(silkTouch) <= 0) return;

        // Retrieve the spawner block entity
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof SpawnerBlockEntity spawnerBE)) return;

        ItemStack tempStack = new ItemStack(Blocks.SPAWNER);
        spawnerBE.saveToItem(tempStack, registryAccess);

        CustomData blockEntityData = tempStack.get(DataComponents.BLOCK_ENTITY_DATA);
        if (blockEntityData == null) {
            ReconSilkSpawners.LOGGER.warn("No BlockEntityTag found on spawner item.");
            return;
        }

        CompoundTag blockEntityTag = blockEntityData.copyTag();
        CompoundTag spawnData = blockEntityTag.getCompound("SpawnData");
        String entityId = spawnData.getString("id");

        // Whitelist/blacklist filtering
        if (!SpawnerValidator.isEntityAllowed(entityId)) return;

        // Optional debug log
        if (Config.logSpawnerData) {
            ReconSilkSpawners.LOGGER.info("Harvesting spawner for entity: {}", entityId);
        }

        // Cancel default drop and manually drop spawner item
        event.setCanceled(true);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

        // Drop actual spawner item preserving NBT
        ItemStack spawnerDrop = new ItemStack(Blocks.SPAWNER);
        spawnerBE.saveToItem(spawnerDrop, registryAccess);

        level.addFreshEntity(new ItemEntity(
                level,
                pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                spawnerDrop
        ));
    }
}

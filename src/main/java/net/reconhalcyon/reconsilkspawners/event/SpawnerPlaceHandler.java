package net.reconhalcyon.reconsilkspawners.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
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

public class SpawnerPlaceHandler {

    @SubscribeEvent
    public static void onPlaceSpawner(BlockEvent.EntityPlaceEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getPlacedBlock();

        // Only care about spawner blocks
        if (state.getBlock() != Blocks.SPAWNER) return;

        // Skip if placement is disabled
        if (!Config.enableSpawnerPlacement) return;

        // Verify placer is a player (ignore Endermen, dispensers, etc)
        if (!(event.getEntity() instanceof Player placer)) return;

        // Get placed BlockEntity
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof SpawnerBlockEntity)) return;

        ItemStack stack = placer.getMainHandItem();
        CustomData blockEntityData = stack.get(DataComponents.BLOCK_ENTITY_DATA);

        if (blockEntityData == null) {
            ReconSilkSpawners.LOGGER.warn("No BlockEntityTag found on placed spawner item.");
            return;
        }

        CompoundTag blockEntityTag = blockEntityData.copyTag();
        CompoundTag spawnData = blockEntityTag.getCompound("SpawnData");
        String entityId = spawnData.getString("id");

        // Dimension placement restriction
        String dimensionId = level.dimension().location().toString();
        if (Config.restrictedPlacementDimensions.contains(dimensionId)) {
            ReconSilkSpawners.LOGGER.warn("Spawner placement blocked in restricted dimension: {}", dimensionId);
            return;
        }

        // Whitelist/blacklist filtering
        if (!SpawnerValidator.isEntityAllowed(entityId)) return;

        if (Config.logSpawnerData) {
            ReconSilkSpawners.LOGGER.info("Placing spawner for entity: {}", entityId);
        }

        // ✅ This is the key part: re-apply full NBT using loadStatic()
        BlockEntity loaded = SpawnerBlockEntity.loadStatic(pos, state, blockEntityTag, level.registryAccess());
        if (loaded instanceof SpawnerBlockEntity newSpawnerBE) {
            level.setBlockEntity(newSpawnerBE);
            newSpawnerBE.setChanged();
        }
    }
}

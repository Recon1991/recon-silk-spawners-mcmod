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

public class SpawnerPlaceHandler {

    @SubscribeEvent
    public static void onPlaceSpawner(BlockEvent.EntityPlaceEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getPlacedBlock();

        if (!level.isClientSide && state.getBlock() == Blocks.SPAWNER) {
            if (!(event.getEntity() instanceof Player placer)) return;

            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof SpawnerBlockEntity)) return;

            ItemStack stack = placer.getMainHandItem();
            CustomData customData = stack.get(DataComponents.BLOCK_ENTITY_DATA);
            if (customData != null) {
                CompoundTag tag = customData.copyTag();

                BlockEntity loaded = SpawnerBlockEntity.loadStatic(pos, state, tag, level.registryAccess());
                if (loaded instanceof SpawnerBlockEntity newSpawnerBE) {
                    level.setBlockEntity(newSpawnerBE);
                    newSpawnerBE.setChanged();
                }
            }
        }
    }
}

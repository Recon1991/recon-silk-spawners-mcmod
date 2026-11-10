package net.reconhalcyon.reconsilkspawners.event;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public final class SpawnerEggHandler {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onUseSpawnerEgg(PlayerInteractEvent.RightClickBlock event) {
        var level = event.getLevel();
        var player = event.getEntity();
        var stack = event.getItemStack();

        if (level.isClientSide || player.isSpectator() || !player.mayBuild()
                || player.isSecondaryUseActive()
                || event.getUseItem() == TriState.FALSE
                || event.getUseBlock() == TriState.FALSE
                || !(stack.getItem() instanceof SpawnEggItem egg)
                || !(level.getBlockEntity(event.getPos())
                instanceof SpawnerBlockEntity spawner)) return;

        spawner.setEntityId(egg.getType(stack), level.getRandom());

        CompoundTag tag = spawner.saveWithoutMetadata(level.registryAccess());

        CompoundTag potential = new CompoundTag();
        potential.putInt("weight", 1);
        potential.put("data", tag.getCompound("SpawnData").copy());

        ListTag potentials = new ListTag();
        potentials.add(potential);
        tag.put("SpawnPotentials", potentials);

        spawner.loadWithComponents(tag, level.registryAccess());
        spawner.setChanged();

        level.sendBlockUpdated(
                event.getPos(),
                spawner.getBlockState(),
                spawner.getBlockState(),
                3
        );

        stack.shrink(1);
        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.SUCCESS);
    }
}


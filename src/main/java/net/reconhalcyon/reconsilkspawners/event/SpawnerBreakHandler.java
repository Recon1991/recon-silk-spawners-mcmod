package net.reconhalcyon.reconsilkspawners.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class SpawnerBreakHandler {

    @SubscribeEvent
    public static void onBlockBreak(BlockEvent.BreakEvent event) {
        Level level = (Level) event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getState();
        Player player = event.getPlayer();

        if (!level.isClientSide && state.getBlock() == Blocks.SPAWNER && player != null) {
            ItemStack tool = player.getMainHandItem();

            // NeoForge 1.21.1 registry access for enchantments
            RegistryAccess registryAccess = level.registryAccess();
            var enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
            Holder<Enchantment> silkTouch = enchantmentRegistry.getHolderOrThrow(Enchantments.SILK_TOUCH);

            if (tool.getEnchantmentLevel(silkTouch) > 0) {
                // Cancel the normal drop
                event.setCanceled(true);

                // Destroy the block manually
                level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                // Drop the spawner item
                ItemStack spawnerDrop = new ItemStack(Blocks.SPAWNER);
                level.addFreshEntity(new ItemEntity(
                        level,
                        pos.getX() + 0.5,
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5,
                        spawnerDrop
                ));
            }
        }
    }
}

package net.reconhalcyon.reconsilkspawners.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
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

        // Ensure server-side execution and correct block/player
        if (!level.isClientSide && state.getBlock() == Blocks.SPAWNER) {
            ItemStack tool = player.getMainHandItem();

            // Get registry and Silk Touch enchantment holder
            RegistryAccess registryAccess = level.registryAccess();
            var enchantmentRegistry = registryAccess.registryOrThrow(Registries.ENCHANTMENT);
            Holder<Enchantment> silkTouch = enchantmentRegistry.getHolderOrThrow(Enchantments.SILK_TOUCH);

            // Check if tool has Silk Touch
            if (tool.getEnchantmentLevel(silkTouch) > 0) {
                BlockEntity blockEntity = level.getBlockEntity(pos);

                if (blockEntity instanceof SpawnerBlockEntity spawnerBE) {
                    // Cancel default block break behavior
                    event.setCanceled(true);

                    // Remove block from world
                    level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);

                    // Create a new ItemStack for the spawner and embed NBT
                    ItemStack spawnerDrop = new ItemStack(Blocks.SPAWNER);
                    spawnerBE.saveToItem(spawnerDrop, registryAccess); // ✔️ Saves SpawnData correctly

                    // Drop the customized spawner into the world
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
}

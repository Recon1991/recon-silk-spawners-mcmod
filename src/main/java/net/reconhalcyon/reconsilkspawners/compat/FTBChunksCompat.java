package net.reconhalcyon.reconsilkspawners.compat;

import dev.ftb.mods.ftbchunks.api.ClaimedChunkManager;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class FTBChunksCompat {

    public static boolean canHarvestSpawner(Level level, ServerPlayer player, BlockPos pos) {
        if (!(FTBChunksAPI.api() != null && FTBChunksAPI.api().isLoaded())) {
            // FTBChunks not loaded or active – allow
            return true;
        }

        ClaimedChunkManager manager = FTBChunksAPI.api().getManager();
        return manager.getClaimedChunk(level.dimension(), pos)
                .map(claim -> claim.getPlayerId().equals(player.getUUID()) || claim.hasPermission(player, "edit"))
                .orElse(true); // allow if unclaimed
    }
}

package net.reconhalcyon.reconsilkspawners.util;

import net.reconhalcyon.reconsilkspawners.Config;
import net.reconhalcyon.reconsilkspawners.ReconSilkSpawners;

public class SpawnerValidator {

    public static boolean isEntityAllowed(String entityId) {
        if (Config.enableWhitelist && !Config.whitelistEntityIds.contains(entityId)) {
            ReconSilkSpawners.LOGGER.info("Blocked entity {}: not in whitelist", entityId);
            return false;
        }
        if (Config.enableBlacklist && Config.blacklistEntityIds.contains(entityId)) {
            ReconSilkSpawners.LOGGER.info("Blocked entity {}: blacklisted", entityId);
            return false;
        }
        return true;
    }
}
package net.reconhalcyon.reconsilkspawners;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = ReconSilkSpawners.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    // Core toggles
    private static final ModConfigSpec.BooleanValue ENABLE_SILK_TOUCH_SPAWNERS = BUILDER
            .comment("Allows Silk Touch harvesting of spawners")
            .define("enableSilkTouchSpawners", true);

    private static final ModConfigSpec.BooleanValue ENABLE_SPAWNER_PLACEMENT = BUILDER
            .comment("Allows placing spawners with NBT data")
            .define("enableSpawnerPlacement", true);

    private static final ModConfigSpec.BooleanValue LOG_SPAWNER_DATA = BUILDER
            .comment("Logs spawner data for debugging")
            .define("logSpawnerData", false);

    // Dimension restrictions
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RESTRICTED_PLACEMENT_DIMENSIONS = BUILDER
            .comment("List of dimensions where spawner placement is restricted")
            .defineList("restrictedPlacementDimensions",
                    List.of("minecraft:the_nether", "minecraft:the_end"),
                    obj -> obj instanceof String);

    // Whitelist filtering
    private static final ModConfigSpec.BooleanValue ENABLE_WHITELIST = BUILDER
            .comment("Enable whitelist for allowed entities")
            .define("enableWhitelist", false);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> WHITELIST_ENTITY_IDS = BUILDER
            .comment("Entity IDs allowed for spawner harvesting and placement")
            .defineList("whitelistEntityIds", List.of("minecraft:zombie"), obj -> obj instanceof String);

    // Blacklist filtering
    private static final ModConfigSpec.BooleanValue ENABLE_BLACKLIST = BUILDER
            .comment("Enable blacklist for blocked entities")
            .define("enableBlacklist", false);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLIST_ENTITY_IDS = BUILDER
            .comment("Entity IDs blocked from spawner harvesting and placement")
            .defineList("blacklistEntityIds", List.of("minecraft:warden"), obj -> obj instanceof String);

    public static final ModConfigSpec SPEC = BUILDER.build();

    // Loaded values
    public static boolean enableSilkTouchSpawners;
    public static boolean enableSpawnerPlacement;
    public static boolean logSpawnerData;
    public static Set<String> restrictedPlacementDimensions;
    public static boolean enableWhitelist;
    public static Set<String> whitelistEntityIds;
    public static boolean enableBlacklist;
    public static Set<String> blacklistEntityIds;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        enableSilkTouchSpawners = ENABLE_SILK_TOUCH_SPAWNERS.get();
        enableSpawnerPlacement = ENABLE_SPAWNER_PLACEMENT.get();
        logSpawnerData = LOG_SPAWNER_DATA.get();

        restrictedPlacementDimensions = RESTRICTED_PLACEMENT_DIMENSIONS.get().stream().map(String::valueOf).collect(Collectors.toSet());
        enableWhitelist = ENABLE_WHITELIST.get();
        whitelistEntityIds = WHITELIST_ENTITY_IDS.get().stream().map(String::valueOf).collect(Collectors.toSet());
        enableBlacklist = ENABLE_BLACKLIST.get();
        blacklistEntityIds = BLACKLIST_ENTITY_IDS.get().stream().map(String::valueOf).collect(Collectors.toSet());
    }
}

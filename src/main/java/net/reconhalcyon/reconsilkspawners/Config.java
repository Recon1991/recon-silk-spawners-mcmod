package net.reconhalcyon.reconsilkspawners;

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

    // Config header - informational only
    private static final ModConfigSpec.ConfigValue<String> CONFIG_HEADER = BUILDER
            .comment(
                    "╭─────────────────────────────────────SLC-01───╮",
                    "│       Silk-Touchable Spawners - Config File  │",
                    "│                                              │",
                    "│  Allows harvesting and placement of Monster  │",
                    "│  Spawners with Silk Touch and full NBT data  │",
                    "│  retention. Includes whitelist/blacklist,    │",
                    "│  dimension restrictions, and debug logging.  │",
                    "│                                              │",
                    "│       'The right tool for the right job.'    │",
                    "╰──────────────────────────────────────────────╯"
            )
            .define("___CONFIG_HEADER_DO_NOT_EDIT", "Do not edit");

    // Core toggles
    private static final ModConfigSpec.BooleanValue ENABLE_SILK_TOUCH_SPAWNERS = BUILDER
            .comment(
                    "If true, allows harvesting of Monster Spawners with Silk Touch.",
                    "Preserves NBT data (including the spawned entity type)."
            )
            .define("enableSilkTouchSpawners", true);

    private static final ModConfigSpec.BooleanValue ENABLE_SPAWNER_PLACEMENT = BUILDER
            .comment(
                    "If true, allows placement of spawners with NBT data restored.",
                    "Spawner data (SpawnData, potentials, delays) will be applied."
            )
            .define("enableSpawnerPlacement", true);

    private static final ModConfigSpec.BooleanValue LOG_SPAWNER_DATA = BUILDER
            .comment(
                    "Logs spawner entity IDs during harvesting or placement.",
                    "Enable for debugging or development purposes."
            )
            .define("logSpawnerData", false);

    // Dimension restrictions
    private static final ModConfigSpec.ConfigValue<List<? extends String>> RESTRICTED_PLACEMENT_DIMENSIONS = BUILDER
            .comment(
                    "List of dimensions (by ID) where placement of spawners is prevented.",
                    "Example: 'minecraft:the_nether', 'minecraft:the_end'"
            )
            .defineList("restrictedPlacementDimensions",
                    List.of("minecraft:the_nether", "minecraft:the_end"),
                    obj -> obj instanceof String);

    // Whitelist filtering
    private static final ModConfigSpec.BooleanValue ENABLE_WHITELIST = BUILDER
            .comment("Enable whitelist entity filtering for harvesting and placement.")
            .define("enableWhitelist", false);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> WHITELIST_ENTITY_IDS = BUILDER
            .comment(
                    "List of entity IDs allowed for harvesting and placement.",
                    "Example: 'minecraft:zombie', 'minecraft:skeleton'"
            )
            .defineList("whitelistEntityIds", List.of("minecraft:zombie"), obj -> obj instanceof String);

    // Blacklist filtering
    private static final ModConfigSpec.BooleanValue ENABLE_BLACKLIST = BUILDER
            .comment("Enable blacklist entity filtering for harvesting and placement.")
            .define("enableBlacklist", false);

    private static final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLIST_ENTITY_IDS = BUILDER
            .comment(
                    "List of entity IDs that are blocked from harvesting or placement.",
                    "Example: 'minecraft:warden', 'minecraft:blaze'"
            )
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

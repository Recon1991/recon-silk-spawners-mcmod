package net.reconhalcyon.reconsilkspawners;

import net.reconhalcyon.reconsilkspawners.event.SpawnerBreakHandler;
import net.reconhalcyon.reconsilkspawners.event.SpawnerPlaceHandler;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(ReconSilkSpawners.MOD_ID)
public class ReconSilkSpawners
{
    public static final String MOD_ID = "reconsilkspawners";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ReconSilkSpawners(IEventBus modEventBus, ModContainer modContainer)
    {
        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);
        //modEventBus.addListener(this::addCreative);

        // ═══╬═══ Register the SpawnerBreakHandler to handle spawner breaking events ═══╬═══
        NeoForge.EVENT_BUS.register(SpawnerBreakHandler.class);
        // ═══╬═══ Register the SpawnerPlaceHandler to handle spawner placement events ═══╬═══
        NeoForge.EVENT_BUS.register(SpawnerPlaceHandler.class);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        //if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS)
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
        LOGGER.info("Spoons and Silk Spawners are ready to go!");
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
            LOGGER.info("HELLO FROM CLIENT SPOON, I mean SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}

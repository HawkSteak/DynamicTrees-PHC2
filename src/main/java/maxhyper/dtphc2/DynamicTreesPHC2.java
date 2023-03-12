package maxhyper.dtphc2;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.pam.pamhc2trees.config.EnableConfig;
import maxhyper.dtphc2.compat.DTPConfig;
import maxhyper.dtphc2.compat.DTPConfigProxy;
import maxhyper.dtphc2.compat.DefaultConfig;
import maxhyper.dtphc2.init.DTPHC2Client;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DynamicTreesPHC2.MOD_ID)
public class DynamicTreesPHC2 {
    public static final String MOD_ID = "dtphc2";

    public static DTPConfigProxy DTPlusConfig;

    public DynamicTreesPHC2() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        bus.addListener(DynamicTreesPHC2::commonSetup);
        bus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        DTPHC2Registries.setup();
        DTPHC2Registries.SOUNDS.register(bus);

        if (ModList.get().isLoaded("dynamictreesplus"))
            DTPlusConfig = new DTPConfig();
        else
            DTPlusConfig = new DefaultConfig();
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    static void commonSetup(final FMLCommonSetupEvent event) {
        if (DTConfigs.WORLD_GEN.get()) {
            ForgeConfigSpec.BooleanValue[] test = {
                    EnableConfig.apple_worldgen,
                    EnableConfig.avocado_worldgen,
                    EnableConfig.candlenut_worldgen,
                    EnableConfig.cherry_worldgen,
                    EnableConfig.chestnut_worldgen,
                    EnableConfig.gooseberry_worldgen,
                    EnableConfig.lemon_worldgen,
                    EnableConfig.nutmeg_worldgen,
                    EnableConfig.orange_worldgen,
                    EnableConfig.peach_worldgen,
                    EnableConfig.pear_worldgen,
                    EnableConfig.plum_worldgen,
                    EnableConfig.walnut_worldgen,
                    EnableConfig.spiderweb_worldgen,
                    EnableConfig.hazelnut_worldgen,
                    EnableConfig.pawpaw_worldgen,
                    EnableConfig.soursop_worldgen,
                    EnableConfig.almond_worldgen,
                    EnableConfig.apricot_worldgen,
                    EnableConfig.banana_worldgen,
                    EnableConfig.cashew_worldgen,
                    EnableConfig.cinnamon_worldgen,
                    EnableConfig.coconut_worldgen,
                    EnableConfig.date_worldgen,
                    EnableConfig.dragonfruit_worldgen,
                    EnableConfig.durian_worldgen,
                    EnableConfig.fig_worldgen,
                    EnableConfig.grapefruit_worldgen,
                    EnableConfig.lime_worldgen,
                    EnableConfig.mango_worldgen,
                    EnableConfig.olive_worldgen,
                    EnableConfig.papaya_worldgen,
                    EnableConfig.paperbark_worldgen,
                    EnableConfig.pecan_worldgen,
                    EnableConfig.peppercorn_worldgen,
                    EnableConfig.persimmon_worldgen,
                    EnableConfig.pistachio_worldgen,
                    EnableConfig.pomegranate_worldgen,
                    EnableConfig.starfruit_worldgen,
                    EnableConfig.vanillabean_worldgen,
                    EnableConfig.breadfruit_worldgen,
                    EnableConfig.guava_worldgen,
                    EnableConfig.jackfruit_worldgen,
                    EnableConfig.lychee_worldgen,
                    EnableConfig.passionfruit_worldgen,
                    EnableConfig.rambutan_worldgen,
                    EnableConfig.tamarind_worldgen,
                    EnableConfig.maple_worldgen,
                    EnableConfig.pinenut_worldgen,
            };

            for (ForgeConfigSpec.BooleanValue v : test) {
                v.set(false);
                //v.save();
            }

        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DTPHC2Client.setup();
    }

    public static ResourceLocation resLoc(final String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}

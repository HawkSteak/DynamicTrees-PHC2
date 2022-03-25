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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DynamicTreesPHC2.MOD_ID)
public class DynamicTreesPHC2
{
    public static final String MOD_ID = "dtphc2";

    public static DTPConfigProxy DTPlusConfig;

    public DynamicTreesPHC2() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        DTPHC2Registries.setup();

        if (ModList.get().isLoaded("dynamictreesplus"))
            DTPlusConfig = new DTPConfig();
        else
            DTPlusConfig = new DefaultConfig();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        if (DTConfigs.WORLD_GEN.get()){
            EnableConfig.apple_worldgen.set(false);
            EnableConfig.avocado_worldgen.set(false);
            EnableConfig.candlenut_worldgen.set(false);
            EnableConfig.cherry_worldgen.set(false);
            EnableConfig.chestnut_worldgen.set(false);
            EnableConfig.gooseberry_worldgen.set(false);
            EnableConfig.lemon_worldgen.set(false);
            EnableConfig.nutmeg_worldgen.set(false);
            EnableConfig.orange_worldgen.set(false);
            EnableConfig.peach_worldgen.set(false);
            EnableConfig.pear_worldgen.set(false);
            EnableConfig.plum_worldgen.set(false);
            EnableConfig.walnut_worldgen.set(false);
            EnableConfig.spiderweb_worldgen.set(false);
            EnableConfig.hazelnut_worldgen.set(false);
            EnableConfig.pawpaw_worldgen.set(false);
            EnableConfig.soursop_worldgen.set(false);
            EnableConfig.almond_worldgen.set(false);
            EnableConfig.apricot_worldgen.set(false);
            EnableConfig.banana_worldgen.set(false);
            EnableConfig.cashew_worldgen.set(false);
            EnableConfig.cinnamon_worldgen.set(false);
            EnableConfig.coconut_worldgen.set(false);
            EnableConfig.date_worldgen.set(false);
            EnableConfig.dragonfruit_worldgen.set(false);
            EnableConfig.durian_worldgen.set(false);
            EnableConfig.fig_worldgen.set(false);
            EnableConfig.grapefruit_worldgen.set(false);
            EnableConfig.lime_worldgen.set(false);
            EnableConfig.mango_worldgen.set(false);
            EnableConfig.olive_worldgen.set(false);
            EnableConfig.papaya_worldgen.set(false);
            EnableConfig.paperbark_worldgen.set(false);
            EnableConfig.pecan_worldgen.set(false);
            EnableConfig.peppercorn_worldgen.set(false);
            EnableConfig.persimmon_worldgen.set(false);
            EnableConfig.pistachio_worldgen.set(false);
            EnableConfig.pomegranate_worldgen.set(false);
            EnableConfig.starfruit_worldgen.set(false);
            EnableConfig.vanillabean_worldgen.set(false);
            EnableConfig.breadfruit_worldgen.set(false);
            EnableConfig.guava_worldgen.set(false);
            EnableConfig.jackfruit_worldgen.set(false);
            EnableConfig.lychee_worldgen.set(false);
            EnableConfig.passionfruit_worldgen.set(false);
            EnableConfig.rambutan_worldgen.set(false);
            EnableConfig.tamarind_worldgen.set(false);
            EnableConfig.maple_worldgen.set(false);
            EnableConfig.pinenut_worldgen.set(false);
        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DTPHC2Client.setup();
    }

    public static ResourceLocation resLoc (final String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}

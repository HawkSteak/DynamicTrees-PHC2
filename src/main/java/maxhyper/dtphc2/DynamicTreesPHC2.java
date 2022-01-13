package maxhyper.dtphc2;

import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import maxhyper.dtphc2.init.DTPHC2Client;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DynamicTreesPHC2.MOD_ID)
public class DynamicTreesPHC2
{
    public static final String MOD_ID = "dtphc2";

    public DynamicTreesPHC2() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);

        RegistryHandler.setup(MOD_ID);

        DTPHC2Registries.setup();
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
//        if (DTConfigs.WORLD_GEN.get()){
//            BYG.ENABLE_OVERWORLD_TREES = false;
//            if (ModList.get().isLoaded("dynamictreesplus")) {
//                BYG.ENABLE_CACTI = false;
//            }
//        }
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        DTPHC2Client.setup();
    }

    public static ResourceLocation resLoc (final String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}

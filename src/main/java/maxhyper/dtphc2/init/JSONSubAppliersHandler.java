package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.treepacks.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.google.gson.JsonElement;
import maxhyper.dtphc2.DynamicTreesPHC2;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesPHC2.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class JSONSubAppliersHandler {

    @SubscribeEvent
    public static void registerAppliers(final ApplierRegistryEvent.Reload<LeavesProperties, JsonElement> event) {
        registerScruffyLeavesAppliers(event.getAppliers());
    }

    @SubscribeEvent
    public static void registerAppliers(final ApplierRegistryEvent.GatherData<LeavesProperties, JsonElement> event) {
        registerScruffyLeavesAppliers(event.getAppliers());
    }

    public static void registerScruffyLeavesAppliers(PropertyAppliers<LeavesProperties, JsonElement> appliers) {
//        appliers.register("scruffy_leaf_chance", ScruffyLeavesProperties.class, Float.class, ScruffyLeavesProperties::setLeafChance)
//                .register("scruffy_max_hydro", ScruffyLeavesProperties.class, Integer.class, ScruffyLeavesProperties::setMaxHydro);
    }

}

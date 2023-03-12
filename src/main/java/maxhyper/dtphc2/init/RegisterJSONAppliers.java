package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.treepacks.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import com.google.gson.JsonElement;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.fruits.PalmPod;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesPHC2.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliersPod(final ApplierRegistryEvent.Reload<Pod, JsonElement> event) {
        registerPodAppliers(event.getAppliers());
    }

    public static void registerPodAppliers(PropertyAppliers<Pod, JsonElement> appliers) {
        appliers.register("can_fall", PalmPod.class, Boolean.class,
                        PalmPod::setDoesFall);
    }

    @SubscribeEvent public static void registerAppliersPod(final ApplierRegistryEvent.GatherData<Pod, JsonElement> event) { registerPodAppliers(event.getAppliers()); }

}

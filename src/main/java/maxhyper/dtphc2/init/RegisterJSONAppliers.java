package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.treepacks.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.google.gson.JsonElement;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.trees.GenOnExtraSoilSpecies;
import net.minecraft.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesPHC2.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    public static void registerSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
        appliers.register("extra_soil_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
                GenOnExtraSoilSpecies::setExtraSoil);
    }

    @SubscribeEvent public static void registerAppliersSpecies(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) { registerSpeciesAppliers(event.getAppliers()); }

}
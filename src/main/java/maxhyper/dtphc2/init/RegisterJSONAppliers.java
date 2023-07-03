package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.api.applier.ApplierRegistryEvent;
import com.ferreusveritas.dynamictrees.deserialisation.PropertyAppliers;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.google.gson.JsonElement;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.trees.FruitLogSpecies;
import maxhyper.dtphc2.trees.GenOnExtraSoilSpecies;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DynamicTreesPHC2.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegisterJSONAppliers {

    @SubscribeEvent
    public static void registerAppliersSpecies(final ApplierRegistryEvent.Reload<Species, JsonElement> event) {
        registerSpeciesAppliers(event.getAppliers());
    }

    public static void registerSpeciesAppliers(PropertyAppliers<Species, JsonElement> appliers) {
        appliers.register("extra_soil_for_worldgen", GenOnExtraSoilSpecies.class, Block.class,
                GenOnExtraSoilSpecies::setExtraSoil)
                .register("log_drop_item", FruitLogSpecies.class, Item.class, FruitLogSpecies::setDropItem)
                //.register("log_drop_item", FruitLogSpecies.class, ResourceLocation.class, FruitLogSpecies::setDropItem)
                .register("log_drop_multiplier", FruitLogSpecies.class, Float.class, FruitLogSpecies::setMultiplier)
                .register("log_drop_fake_log", FruitLogSpecies.class, Item.class, FruitLogSpecies::setFakeLog);
    }

    @SubscribeEvent public static void registerAppliersSpecies(final ApplierRegistryEvent.GatherData<Species, JsonElement> event) { registerSpeciesAppliers(event.getAppliers()); }

}
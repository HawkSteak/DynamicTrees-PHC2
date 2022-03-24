package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.init.DTRegistries;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.pam.pamhc2trees.init.ItemRegistry;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.blocks.DragonfruitLeavesProperties;
import maxhyper.dtphc2.blocks.FruitVineBlock;
import maxhyper.dtphc2.cells.DTPHC2CellKits;
import maxhyper.dtphc2.fruits.OffsetFruit;
import maxhyper.dtphc2.fruits.PalmPod;
import maxhyper.dtphc2.genfeatures.DTPHC2GenFeatures;
import maxhyper.dtphc2.growthlogic.DTPHC2GrowthLogicKits;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTPHC2Registries {

    public static final FruitVineBlock PASSIONFRUIT_VINE = new FruitVineBlock(ItemRegistry.passionfruititem);
    public static final Item PASSIONFRUIT_VINE_ITEM = new BlockItem(PASSIONFRUIT_VINE, new Item.Properties().tab(DTRegistries.ITEM_GROUP));

    public static final FruitVineBlock VANILLA_VINE = new FruitVineBlock(ItemRegistry.vanillabeanitem);
    public static final Item VANILLA_VINE_ITEM = new BlockItem(VANILLA_VINE, new Item.Properties().tab(DTRegistries.ITEM_GROUP));

    public static final FruitVineBlock PEPPERCORN_VINE = new FruitVineBlock(ItemRegistry.peppercornitem);
    public static final Item PEPPERCORN_VINE_ITEM = new BlockItem(PEPPERCORN_VINE, new Item.Properties().tab(DTRegistries.ITEM_GROUP));

    public static void setup() {
        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("passionfruit_vine"), PASSIONFRUIT_VINE);
        RegistryHandler.addItem(DynamicTreesPHC2.resLoc("passionfruit_vine"), PASSIONFRUIT_VINE_ITEM);

        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("vanilla_vine"), VANILLA_VINE);
        RegistryHandler.addItem(DynamicTreesPHC2.resLoc("vanilla_vine"), VANILLA_VINE_ITEM);

        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("peppercorn_vine"), PEPPERCORN_VINE);
        RegistryHandler.addItem(DynamicTreesPHC2.resLoc("peppercorn_vine"), PEPPERCORN_VINE_ITEM);
    }

    @SubscribeEvent
    public static void registerFruitType(final TypeRegistryEvent<Fruit> event) {
        event.registerType(DynamicTreesPHC2.resLoc("offset_down"), OffsetFruit.TYPE);
    }

    @SubscribeEvent
    public static void registerPodType(final TypeRegistryEvent<Pod> event) {
        event.registerType(DynamicTreesPHC2.resLoc("palm"), PalmPod.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesType(final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(DynamicTreesPHC2.resLoc("dragonfruit"), DragonfruitLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        DTPHC2GenFeatures.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onCellKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<CellKit> event) {
        DTPHC2CellKits.register(event.getRegistry());
    }

    @SubscribeEvent
    public static void onGrowthLogicKitRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GrowthLogicKit> event) {
        DTPHC2GrowthLogicKits.register(event.getRegistry());
    }



    @SubscribeEvent
    public static void registerSpeciesTypes (final TypeRegistryEvent<Species> event) {
//        event.registerType(DynamicTreesPHC2.resLoc("poplar"), PoplarSpecies.TYPE);
//        event.registerType(DynamicTreesPHC2.resLoc("ether"), EtherSpecies.TYPE);
//        event.registerType(DynamicTreesPHC2.resLoc("twiglet"), TwigletSpecies.TYPE);
//        event.registerType(DynamicTreesPHC2.resLoc("generates_underwater"), GenUnderwaterSpecies.TYPE);
//        event.registerType(DynamicTreesPHC2.resLoc("generates_on_mossy_stone"), GenOnMossyStoneSpecies.TYPE);
//        event.registerType(DynamicTreesPHC2.resLoc("mangrove"), MangroveSpecies.TYPE);
    }
    
    @SubscribeEvent
    public static void registerFamilyTypes (final TypeRegistryEvent<Family> event) {
//        event.registerType(DynamicTreesPHC2.resLoc("diagonal_palm"), DiagonalPalmFamily.TYPE);
//        event.registerType(DynamicTreesPHC2.resLoc("sythian_fungus"), SythianFungusFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesTypes (final TypeRegistryEvent<LeavesProperties> event) {
//        event.registerType(DynamicTreesPHC2.resLoc("scruffy"), ScruffyLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerSoilPropertiesTypes (final TypeRegistryEvent<SoilProperties> event) {
//        event.registerType(DynamicTreesPHC2.resLoc( "offset_tint"), BYGTintedSoilProperties.TYPE);
    }

    @SubscribeEvent
    public static void onItemsRegistry (final RegistryEvent.Register<Item> event) {

    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

    }

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {

    }

}

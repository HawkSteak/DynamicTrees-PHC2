package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.DynamicTrees;
import com.ferreusveritas.dynamictrees.api.registry.RegistryEvent;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.block.PottedSaplingBlock;
import com.ferreusveritas.dynamictrees.block.branch.TrunkShellBlock;
import com.ferreusveritas.dynamictrees.block.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.init.DTRegistries;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeature;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import com.ferreusveritas.dynamictrees.tree.family.Family;
import com.ferreusveritas.dynamictrees.tree.species.Species;
import com.ferreusveritas.dynamictrees.util.CommonVoxelShapes;
import com.google.common.base.Suppliers;
import com.pam.pamhc2trees.worldgen.*;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.blocks.*;
import maxhyper.dtphc2.canceller.DTPHC2TreeFeatureCanceller;
import maxhyper.dtphc2.fruits.*;
import maxhyper.dtphc2.genfeatures.DTPHC2GenFeatures;
import maxhyper.dtphc2.items.FruitVineItem;
import maxhyper.dtphc2.items.RipePeppercornItem;
import maxhyper.dtphc2.trees.FruitLogSpecies;
import maxhyper.dtphc2.trees.GenOnExtraSoilSpecies;
import maxhyper.dtphc2.trees.PaperbarkFamily;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import net.minecraft.core.registries.Registries;

import java.util.function.Supplier;


@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTPHC2Registries {

    public static final TagKey<Block> CAN_BE_SPILED = TagKey.create(Registries.BLOCK, DynamicTreesPHC2.location("can_be_spiled"));

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DynamicTreesPHC2.MOD_ID);

    public static final VoxelShape DRAGON_FRUIT_CACTUS_SAPLING_SHAPE = Shapes.box(
                    0.375f, 0.0f, 0.375f,
                    0.625f, 0.5f, 0.625f);

    public static final VoxelShape BANANA_SAPLING_SHAPE = Shapes.box(
                    0.375f, 0.0f, 0.375f,
                    0.625f, 0.9375f, 0.625f);


    public static final RegistryObject<SoundEvent> FRUIT_BONK = registerSound("falling_fruit.bonk");

    public static final Supplier<BananaSuckerBlock> BANANA_SUCKER_BLOCK = Suppliers.memoize(BananaSuckerBlock::new);
    public static final Supplier<FruitVineBlock> PASSION_FRUIT_VINE = Suppliers.memoize(FruitVineBlock::new);
    public static final Supplier<FruitVineBlock> VANILLA_VINE = Suppliers.memoize(FruitVineBlock::new);
    public static final Supplier<FruitVineBlock> PEPPERCORN_VINE = Suppliers.memoize(FruitVineBlock::new);
    public static final Supplier<MapleSpileBlock> MAPLE_SPILE_BLOCK = Suppliers.memoize(MapleSpileBlock::new);
    public static final Supplier<MapleSpileBucketBlock> MAPLE_SPILE_BUCKET_BLOCK = Suppliers.memoize(MapleSpileBucketBlock::new);

    public static void setup() {
        CommonVoxelShapes.SHAPES.put(DynamicTreesPHC2.location("dragon_fruit_cactus").toString(), DRAGON_FRUIT_CACTUS_SAPLING_SHAPE);
        CommonVoxelShapes.SHAPES.put(DynamicTreesPHC2.location("banana_sapling").toString(), BANANA_SAPLING_SHAPE);

        setupBlocks();
        setupItems();

    }
    private static void setupBlocks() {
        RegistryHandler.addBlock(DynamicTreesPHC2.location("banana_sucker"), BANANA_SUCKER_BLOCK);
        RegistryHandler.addBlock(DynamicTreesPHC2.location("passion_fruit_vine"), PASSION_FRUIT_VINE);
        RegistryHandler.addBlock(DynamicTreesPHC2.location("vanilla_vine"), VANILLA_VINE);
        RegistryHandler.addBlock(DynamicTreesPHC2.location("peppercorn_vine"), PEPPERCORN_VINE);
        RegistryHandler.addBlock(DynamicTreesPHC2.location("maple_spile"), MAPLE_SPILE_BLOCK);
        RegistryHandler.addBlock(DynamicTreesPHC2.location("maple_spile_bucket"), MAPLE_SPILE_BUCKET_BLOCK);
    }



    public static final Supplier<RipePeppercornItem> RIPE_PEPPERCORN_ITEM = Suppliers.memoize(() -> new RipePeppercornItem(new Item.Properties()));
    public static final Supplier<FruitVineItem> VANILLA_VINE_ITEM = Suppliers.memoize(() -> new FruitVineItem(VANILLA_VINE.get(), new Item.Properties()));
    public static final Supplier<FruitVineItem> PASSION_FRUIT_VINE_ITEM = Suppliers.memoize(() -> new FruitVineItem(PASSION_FRUIT_VINE.get(), new Item.Properties()));
    public static final Supplier<FruitVineItem> PEPPERCORN_VINE_ITEM = Suppliers.memoize(() -> new FruitVineItem(PEPPERCORN_VINE.get(), new Item.Properties()));



    private static void setupItems() {
        RegistryHandler.addItem(DynamicTreesPHC2.location("ripe_peppercorn_item"), RIPE_PEPPERCORN_ITEM);
        RegistryHandler.addItem(DynamicTreesPHC2.location("vanilla_vine"), VANILLA_VINE_ITEM);
        RegistryHandler.addItem(DynamicTreesPHC2.location("passion_fruit_vine"), PASSION_FRUIT_VINE_ITEM);
        RegistryHandler.addItem(DynamicTreesPHC2.location("peppercorn_vine"), PEPPERCORN_VINE_ITEM);
    }

    public static RegistryObject<SoundEvent> registerSound (String name){
        return SOUNDS.register(name, ()-> SoundEvent.createVariableRangeEvent(DynamicTreesPHC2.location(name)));
    }

    @SubscribeEvent
    public static void registerFruitType(final TypeRegistryEvent<Fruit> event) {
        event.registerType(DynamicTreesPHC2.location("offset_down"), OffsetFruit.TYPE);
        event.registerType(DynamicTreesPHC2.location("falling_fruit"), FallingFruit.TYPE);
        event.registerType(DynamicTreesPHC2.location("cobweb"), CobwebFruit.TYPE);
    }

    @SubscribeEvent
    public static void registerPodType(final TypeRegistryEvent<Pod> event) {
        event.registerType(DynamicTreesPHC2.location("palm"), PalmPod.TYPE);
        event.registerType(DynamicTreesPHC2.location("falling_palm"), FallingPalmPod.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesType(final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(DynamicTreesPHC2.location("dragon_fruit"), DragonFruitLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerFamilyType(final TypeRegistryEvent<Family> event) {
        event.registerType(DynamicTreesPHC2.location("paperbark"), PaperbarkFamily.TYPE);
    }

    @SubscribeEvent
    public static void registerSpeciesType(final TypeRegistryEvent<Species> event) {
        event.registerType(DynamicTreesPHC2.location("fruit_log"), FruitLogSpecies.TYPE);
        event.registerType(DynamicTreesPHC2.location("generates_on_extra_soil"), GenOnExtraSoilSpecies.TYPE);
    }

    @SubscribeEvent
    public static void onGenFeatureRegistry (final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<GenFeature> event) {
        DTPHC2GenFeatures.register(event.getRegistry());
    }

    public static final FeatureCanceller TREE_CANCELLER = new DTPHC2TreeFeatureCanceller(DynamicTreesPHC2.location("tree"),
            new ResourceLocation[]{});

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final RegistryEvent<FeatureCanceller> event) {
        event.getRegistry().registerAll(TREE_CANCELLER);
    }

}

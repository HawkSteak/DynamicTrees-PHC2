package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.init.DTRegistries;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.util.CommonVoxelShapes;
import com.pam.pamhc2trees.init.ItemRegistry;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.blocks.*;
import maxhyper.dtphc2.cells.DTPHC2CellKits;
import maxhyper.dtphc2.fruits.*;
import maxhyper.dtphc2.genfeatures.DTPHC2GenFeatures;
import maxhyper.dtphc2.trees.PaperbarkFamily;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTPHC2Registries {

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, DynamicTreesPHC2.MOD_ID);

    public static final FruitVineBlock PASSION_FRUIT_VINE = new FruitVineBlock().setSeasonOffset(0f); //summer
    public static final Item PASSION_FRUIT_VINE_ITEM = new BlockItem(PASSION_FRUIT_VINE, new Item.Properties().tab(DTRegistries.ITEM_GROUP));

    public static final FruitVineBlock VANILLA_VINE = new FruitVineBlock().setSeasonOffset(2f); //winter
    public static final Item VANILLA_VINE_ITEM = new BlockItem(VANILLA_VINE, new Item.Properties().tab(DTRegistries.ITEM_GROUP));

    public static final FruitVineBlock PEPPERCORN_VINE = new FruitVineBlock().setSeasonOffset(0f); //summer
    public static final Item PEPPERCORN_VINE_ITEM = new BlockItem(PEPPERCORN_VINE, new Item.Properties().tab(DTRegistries.ITEM_GROUP));

    public static final Item RIPE_PEPPERCORN_ITEM = new Item(new Item.Properties().tab(DTRegistries.ITEM_GROUP));

    public static final MapleSpileBlock MAPLE_SPILE_BLOCK = new MapleSpileBlock();
    //public static final Item MAPLE_SPILE_ITEM = new BlockItem(MAPLE_SPILE_BLOCK, new Item.Properties().tab(DTRegistries.ITEM_GROUP));
    public static final MapleSpileBucketBlock MAPLE_SPILE_BUCKET_BLOCK = new MapleSpileBucketBlock();
    //public static final Item MAPLE_SPILE_BUCKET_ITEM = new BlockItem(MAPLE_SPILE_BUCKET_BLOCK, new Item.Properties().tab(DTRegistries.ITEM_GROUP));

    public static final BananaSuckerBlock BANANA_SUCKER_BLOCK = new BananaSuckerBlock();

    public static final VoxelShape DRAGON_FRUIT_CACTUS_SAPLING_SHAPE = VoxelShapes.create(
            new AxisAlignedBB(
                    0.375f, 0.0f, 0.375f,
                    0.625f, 0.5f, 0.625f));

    public static final VoxelShape BANANA_SAPLING_SHAPE = VoxelShapes.create(
            new AxisAlignedBB(
                    0.375f, 0.0f, 0.375f,
                    0.625f, 0.9375f, 0.625f));


    public static final RegistryObject<SoundEvent> FRUIT_BONK = registerSound("falling_fruit.bonk");

    public static void setup() {
        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("passion_fruit_vine"), PASSION_FRUIT_VINE);
        RegistryHandler.addItem(DynamicTreesPHC2.resLoc("passion_fruit_vine"), PASSION_FRUIT_VINE_ITEM);

        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("vanilla_vine"), VANILLA_VINE);
        RegistryHandler.addItem(DynamicTreesPHC2.resLoc("vanilla_vine"), VANILLA_VINE_ITEM);

        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("peppercorn_vine"), PEPPERCORN_VINE);
        RegistryHandler.addItem(DynamicTreesPHC2.resLoc("peppercorn_vine"), PEPPERCORN_VINE_ITEM);

        RegistryHandler.addItem(DynamicTreesPHC2.resLoc("ripe_peppercorn_item"), RIPE_PEPPERCORN_ITEM);

        CommonVoxelShapes.SHAPES.put(DynamicTreesPHC2.resLoc("dragon_fruit_cactus").toString(), DRAGON_FRUIT_CACTUS_SAPLING_SHAPE);
        CommonVoxelShapes.SHAPES.put(DynamicTreesPHC2.resLoc("banana_sapling").toString(), BANANA_SAPLING_SHAPE);

        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("maple_spile"), MAPLE_SPILE_BLOCK);

        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("maple_spile_bucket"), MAPLE_SPILE_BUCKET_BLOCK);

        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("banana_sucker"), BANANA_SUCKER_BLOCK);
    }

    public static RegistryObject<SoundEvent> registerSound (String name){
        return SOUNDS.register(name, ()-> new SoundEvent(DynamicTreesPHC2.resLoc(name)));
    }

    @SubscribeEvent
    public static void registerFruitType(final TypeRegistryEvent<Fruit> event) {
        event.registerType(DynamicTreesPHC2.resLoc("offset_down"), OffsetFruit.TYPE);
        event.registerType(DynamicTreesPHC2.resLoc("falling_fruit"), FallingFruit.TYPE);
        event.registerType(DynamicTreesPHC2.resLoc("cobweb"), CobwebFruit.TYPE);
    }

    @SubscribeEvent
    public static void registerPodType(final TypeRegistryEvent<Pod> event) {
        event.registerType(DynamicTreesPHC2.resLoc("palm"), PalmPod.TYPE);
        event.registerType(DynamicTreesPHC2.resLoc("falling_palm"), FallingPalmPod.TYPE);
    }

    @SubscribeEvent
    public static void registerLeavesPropertiesType(final TypeRegistryEvent<LeavesProperties> event) {
        event.registerType(DynamicTreesPHC2.resLoc("dragon_fruit"), DragonFruitLeavesProperties.TYPE);
    }

    @SubscribeEvent
    public static void registerFamilyType(final TypeRegistryEvent<Family> event) {
        event.registerType(DynamicTreesPHC2.resLoc("paperbark"), PaperbarkFamily.TYPE);
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
    public static void onItemsRegistry (final RegistryEvent.Register<Item> event) {
        PASSION_FRUIT_VINE
                .setFruitStack(new ItemStack(ItemRegistry.passionfruititem));
                //.setFruitingOffset(null);
        VANILLA_VINE
                .setFruitStack(new ItemStack(ItemRegistry.vanillabeanitem));
                //.setFruitingOffset(null);
        PEPPERCORN_VINE
                .setFruitStack(new ItemStack(ItemRegistry.peppercornitem))
                .setOverripeFruitStack(new ItemStack(RIPE_PEPPERCORN_ITEM))
                //.setFruitingOffset(null)
                .setMatureAge(3)
                .setFruitOverripenChance(0.01f);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {

    }

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {

    }

}

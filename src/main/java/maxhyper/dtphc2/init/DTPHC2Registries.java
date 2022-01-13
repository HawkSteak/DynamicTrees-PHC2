package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.cells.CellKit;
import com.ferreusveritas.dynamictrees.api.registry.RegistryHandler;
import com.ferreusveritas.dynamictrees.api.registry.TypeRegistryEvent;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.SoilProperties;
import com.ferreusveritas.dynamictrees.growthlogic.GrowthLogicKit;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.trees.Family;
import com.ferreusveritas.dynamictrees.trees.Species;
import com.ferreusveritas.dynamictrees.util.CommonVoxelShapes;
import com.ferreusveritas.dynamictrees.util.ShapeUtils;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.cells.DTPHC2CellKits;
import maxhyper.dtphc2.genfeatures.DTPHC2GenFeatures;
import maxhyper.dtphc2.growthlogic.DTPHC2GrowthLogicKits;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class DTPHC2Registries {

    public static FruitBlock GREEN_APPLE_FRUIT = new FruitBlock()
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
    public static FruitBlock JOSHUA_FRUIT = new FruitBlock()
            .setShape(1, ShapeUtils.createFruitShape(2,3,0))
            .setShape(2, ShapeUtils.createFruitShape(2.5f,4,2))
            .setShape(3, ShapeUtils.createFruitShape(3.5f,4,3))
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
//    public static FruitBlock ETHER_BULBS_FRUIT = new EtherBulbsFruitBlock()
//            .setShape(1, ShapeUtils.createFruitShape(2,3,0))
//            .setShape(2, ShapeUtils.createFruitShape(2.5f,4,2))
//            .setShape(3, ShapeUtils.createFruitShape(3.5f,4,3))
//            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
    public static FruitBlock HOLLY_BERRIES_FRUIT = new FruitBlock()
            .setShape(1, ShapeUtils.createFruitShape(2,3,0))
            .setShape(2, ShapeUtils.createFruitShape(2.5f,4,2))
            .setShape(3, ShapeUtils.createFruitShape(3.5f,4,3))
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);
    public static FruitBlock BAOBAB_FRUIT = new FruitBlock()
            .setShape(1, ShapeUtils.createFruitShape(2,3,0))
            .setShape(2, ShapeUtils.createFruitShape(2.5f,4,2))
            .setShape(3, ShapeUtils.createFruitShape(3.5f,4,3))
            .setCanBoneMeal(DTConfigs.CAN_BONE_MEAL_APPLE::get);

    public static final VoxelShape MUSHROOM_STEM_LONG = Block.box(7D, 0D, 7D, 9D, 10D, 9D);
    public static final VoxelShape SYTHIAN_CAP_A = Block.box(4D, 6D, 4D, 12D, 8D, 12D);
    public static final VoxelShape SYTHIAN_CAP_B = Block.box(5D, 3D, 5D, 11D, 5D, 11D);
    public static final VoxelShape SYTHIAN_CAP_C = Block.box(5D, 9D, 5D, 11D, 11D, 11D);

    public static final VoxelShape SYTHIAN_MUSHROOM = VoxelShapes.or(MUSHROOM_STEM_LONG, SYTHIAN_CAP_A, SYTHIAN_CAP_B, SYTHIAN_CAP_C);

    public static void setup() {
        //RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("ether_bulbs_fruit"), ETHER_BULBS_FRUIT);
        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("green_apple_fruit"), GREEN_APPLE_FRUIT);
        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("joshua_fruit"), JOSHUA_FRUIT);
        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("holly_berries_fruit"), HOLLY_BERRIES_FRUIT);
        RegistryHandler.addBlock(DynamicTreesPHC2.resLoc("baobab_fruit"), BAOBAB_FRUIT);

        CommonVoxelShapes.SHAPES.put(DynamicTreesPHC2.resLoc("sythian").toString(), SYTHIAN_MUSHROOM);
    }

    public static void setupBlocks() {
        setUpSoils();
        setupConnectables();
    }

    private static void setUpSoils() {

    }

    private static void setupConnectables() {
//        BranchConnectables.makeBlockConnectable(BYGBlocks.PURPLE_SHROOMLIGHT, (state, world, pos, side) -> {
//            if (side == Direction.DOWN) {
//                BlockState branchState = world.getBlockState(pos.relative(Direction.UP));
//                BranchBlock branch = TreeHelper.getBranch(branchState);
//                if (branch != null)
//                    return MathHelper.clamp(branch.getRadius(branchState) - 1, 1, 8);
//                 else return 8;
//            }
//            return 0;
//        });

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
//        Item etherBulbs = ForgeRegistries.ITEMS.getValue(new ResourceLocation("byg","ether_bulbs"));
//        Species etherSpecies = Species.REGISTRY.get(new ResourceLocation("dtbyg","ether"));
//        ETHER_BULBS_FRUIT.setDroppedItem(new ItemStack(etherBulbs));
//        if (etherSpecies.isValid()) ETHER_BULBS_FRUIT.setSpecies(etherSpecies);

        Item greenApple = ForgeRegistries.ITEMS.getValue(new ResourceLocation("byg","green_apple"));
        Species skyrisSpecies = Species.REGISTRY.get(new ResourceLocation("dtbyg","skyris"));
        GREEN_APPLE_FRUIT.setDroppedItem(new ItemStack(greenApple));
        if (skyrisSpecies.isValid()) GREEN_APPLE_FRUIT.setSpecies(skyrisSpecies);

        Item joshuaFruit = ForgeRegistries.ITEMS.getValue(new ResourceLocation("byg","joshua_fruit"));
        Species joshuaSpecies = Species.REGISTRY.get(new ResourceLocation("dtbyg","joshua"));
        JOSHUA_FRUIT.setDroppedItem(new ItemStack(joshuaFruit));
        if (joshuaSpecies.isValid()) JOSHUA_FRUIT.setSpecies(joshuaSpecies);

        Item hollyBerries = ForgeRegistries.ITEMS.getValue(new ResourceLocation("byg","holly_berries"));
        Species hollySpecies = Species.REGISTRY.get(new ResourceLocation("dtbyg","holly"));
        HOLLY_BERRIES_FRUIT.setDroppedItem(new ItemStack(hollyBerries));
        if (hollySpecies.isValid()) HOLLY_BERRIES_FRUIT.setSpecies(hollySpecies);

        Item baobabFruit = ForgeRegistries.ITEMS.getValue(new ResourceLocation("byg","baobab_fruit"));
        Species baobabSpecies = Species.REGISTRY.get(new ResourceLocation("dtbyg","baobab"));
        BAOBAB_FRUIT.setDroppedItem(new ItemStack(baobabFruit));
        if (baobabSpecies.isValid()) BAOBAB_FRUIT.setSpecies(baobabSpecies);
    }

    @SubscribeEvent
    public static void onBlocksRegistry(final RegistryEvent.Register<Block> event) {
        setupBlocks();
    }

    @SubscribeEvent
    public static void onFeatureCancellerRegistry(final com.ferreusveritas.dynamictrees.api.registry.RegistryEvent<FeatureCanceller> event) {

    }

}

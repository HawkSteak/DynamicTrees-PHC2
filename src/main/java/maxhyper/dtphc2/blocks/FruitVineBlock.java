package maxhyper.dtphc2.blocks;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;

public class FruitVineBlock extends VineBlock {

    private static final float baseFruitingChance = 0.002f;
    private static final float fruitGrowChance = 0.2f;
    private static final int maxLength = 3;
    private final Item fruitItem;

    private static final IntegerProperty ageProperty = IntegerProperty.create("age", 0, 4);

    public FruitVineBlock(Item fruitItem) {
        super(AbstractBlock.Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().strength(0.2F).sound(SoundType.VINE));
        registerDefaultState(defaultBlockState().setValue(ageProperty, 0));
        this.fruitItem = fruitItem;
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ageProperty);
    }

//    private boolean canSupportAtFace(IBlockReader pBlockReader, BlockPos pPos, Direction pDirection) {
//        if (pDirection == Direction.DOWN) {
//            return false;
//        } else {
//            BlockPos blockpos = pPos.relative(pDirection);
//            if (isAcceptableNeighbour(pBlockReader, blockpos, pDirection)) {
//                return true;
//            } else if (pDirection.getAxis() == Direction.Axis.Y) {
//                return false;
//            } else {
//                BooleanProperty booleanproperty = PROPERTY_BY_DIRECTION.get(pDirection);
//                BlockState blockstate = pBlockReader.getBlockState(pPos.above());
//                return blockstate.is(this) && blockstate.getValue(booleanproperty);
//            }
//        }
//    }
//    private int countFaces(BlockState pState) {
//        int i = 0;
//        for(BooleanProperty booleanproperty : PROPERTY_BY_DIRECTION.values())
//            if (pState.getValue(booleanproperty)) ++i;
//        return i;
//    }
//
//    private float getFruitOffset (){
//        return 0;
//    }
//
//    private float getFruitingChance (World world, BlockPos pos){
//        float fruitFactor = SeasonHelper.globalSeasonalFruitProductionFactor(WorldContext.create(world), pos, getFruitOffset(), true);
//        return baseFruitingChance * Math.max((fruitFactor + 0.25f), 1);
//    }
//
//    private void changeVineWithProperties(World world, BlockPos pos, BlockState stateWithWantedProperties){
//        changeVineWithProperties(world, pos, getStateFromAge(0), stateWithWantedProperties);
//    }
//    private void changeVineWithProperties(World world, BlockPos pos, BlockState baseState, BlockState stateWithWantedProperties){
//        BlockState state = baseState.setValue(UP, stateWithWantedProperties.getValue(UP))
//                .setValue(NORTH, stateWithWantedProperties.getValue(NORTH))
//                .setValue(WEST, stateWithWantedProperties.getValue(WEST))
//                .setValue(SOUTH, stateWithWantedProperties.getValue(SOUTH))
//                .setValue(EAST, stateWithWantedProperties.getValue(EAST));
//        world.setBlock(pos, state, 2);
//    }
//
//    private boolean recheckGrownSides(World worldIn, BlockPos pos, BlockState state) {
//        BlockState iblockstate = state;
//
//        for (Direction dir : Direction.Plane.HORIZONTAL) {
//            BooleanProperty sideProperty = getPropertyForFace(dir);
//
//            if (state.getValue(sideProperty) && !canSupportAtFace(worldIn, pos, dir.getOpposite())) {
//                BlockState iblockstate1 = worldIn.getBlockState(pos.above());
//
//                if (iblockstate1.getBlock() != this || !iblockstate1.getValue(sideProperty)) {
//                    state = state.setValue(sideProperty, false);
//                }
//            }
//        }
//        if (countFaces(state) == 0) {
//            return false;
//        }
//        else {
//            if (iblockstate != state) {
//                worldIn.setBlock(pos, state, 2);
//            }
//            return true;
//        }
//    }
//
//    protected void growFruit(World worldIn, BlockPos pos, BlockState state, Random rand){
//        Integer age = getAgeFromState(state);
//        if (age == null) return;
//        if ((age == 0 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextFloat() <= getFruitingChance(worldIn, pos))) ||
//                (age > 0 && age < 4 && net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, rand.nextFloat() <= fruitGrowChance))) {
//
//            //We look for fruit blocks around. If there is more than two we cancel the fruit growth
//            int fruitFoundAround = 0;
//            for (Direction dir : Direction.values()){
//                Integer sideAge = getAgeFromState(worldIn.getBlockState(pos.offset(dir.getNormal())));
//                if (sideAge != null && sideAge > 0){
//                    fruitFoundAround++;
//                }
//            }
//            if (fruitFoundAround >= 2){
//                changeVineWithProperties(worldIn, pos, getStateFromAge(0), state);
//                return;
//            }
//            changeVineWithProperties(worldIn, pos, getStateFromAge(age + 1), state);
//            net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
//        }
//    }
//
//    private Integer getAgeFromState (BlockState state){
//        if (!state.hasProperty(ageProperty)) return null;
//        return state.getValue(ageProperty);
//    }
//
//    @Nonnull
//    private BlockState getStateFromAge (int age){
//        return defaultBlockState().setValue(ageProperty, age);
//    }
//
//    private ItemStack getFruit(){
//        return fruitStack;
//    }
//
//    private boolean spawnItemFruitIfRipe(World world, BlockPos pos){
//        return spawnItemFruitIfRipe(world, pos, world.getBlockState(pos));
//    }
//    private boolean spawnItemFruitIfRipe(World world, BlockPos pos, BlockState state){
//        Integer age = getAgeFromState(state);
//        if (!world.isClientSide() && age != null && age == 4){
//            world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, getFruit()));
//            return true;
//        }
//        return false;
//    }


}

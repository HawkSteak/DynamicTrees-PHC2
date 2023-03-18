package maxhyper.dtphc2.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGenerationContext;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import maxhyper.dtphc2.blocks.BananaSuckerBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PlantSuckerGenFeature extends GenFeature {

    public static final ConfigurationProperty<Block> SUCKER_BLOCK =
            ConfigurationProperty.block("sucker_block");
    public static final ConfigurationProperty<Float> PLACE_CHANCE_WORLDGEN = ConfigurationProperty.floatProperty("place_chance_worldgen");

    public PlantSuckerGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(SUCKER_BLOCK, PLACE_CHANCE, PLACE_CHANCE_WORLDGEN);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return new GenFeatureConfiguration(this)
                .with(SUCKER_BLOCK, Blocks.AIR)
                .with(PLACE_CHANCE, 0.06f)
                .with(PLACE_CHANCE_WORLDGEN, 0.6f);
    }

    @Override
    protected boolean postGenerate(GenFeatureConfiguration configuration, PostGenerationContext context) {
        IWorld world = context.world();
        boolean placed = false;
            for (Direction dir : Direction.Plane.HORIZONTAL){
                if(context.random().nextFloat() < configuration.get(PLACE_CHANCE_WORLDGEN)) {
                    if (addSucker(world, context.pos(), true, configuration.get(SUCKER_BLOCK), dir))
                        placed = true;
                }
            }
        return placed;
    }

    @Override
    protected boolean postGrow(GenFeatureConfiguration configuration, PostGrowContext context) {
        if(context.random().nextFloat() < configuration.get(PLACE_CHANCE)) {
            Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(context.random());
            return addSucker(context.world(), context.treePos(), false, configuration.get(SUCKER_BLOCK), dir);
        }
        return false;
    }

    private BlockPos getGroundPos (IWorld world, BlockPos pos){
        for (int i=0; i < 3; i++){
            BlockPos testPos = pos.offset(0, i,0);
            if (world.isAreaLoaded(testPos, 16) &&
                    world.getBlockState(testPos).getMaterial().isReplaceable() &&
                    world.getBlockState(testPos.below()).isFaceSturdy(world, pos, Direction.UP)){
                return testPos;
            }
        }
        return null;
    }

    private boolean addSucker(IWorld world, BlockPos rootPos, boolean worldGen, Block sucker, Direction dir) {
        BlockPos ground = getGroundPos(world, rootPos.offset(dir.getNormal()));
        if (ground == null){
            return false;
        }
        world.setBlock(ground, sucker.defaultBlockState().setValue(BananaSuckerBlock.FACING, dir), worldGen?0:2);
        return true;
    }

}

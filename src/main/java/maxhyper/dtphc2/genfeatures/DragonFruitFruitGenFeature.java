package maxhyper.dtphc2.genfeatures;

import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class DragonFruitFruitGenFeature extends BananaFruitGenFeature {

    public DragonFruitFruitGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void addFruit(GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()) return;
        CoordUtils.Surround placeDir = CoordUtils.Surround.values()[world.getRandom().nextInt(8)];
        BlockPos pos = expandRandom(configuration, world, leavesPos.offset(placeDir.getOffset()));
        if (world.getBlockState(pos).getMaterial().isReplaceable()) {
            Float seasonValue = SeasonHelper.getSeasonValue(world, rootPos);
            Fruit fruit = configuration.get(FRUIT);
            if (worldGen) {
                fruit.placeDuringWorldGen(world, pos, seasonValue);
            } else {
                fruit.place(world, pos, seasonValue);
            }
        }
    }

}
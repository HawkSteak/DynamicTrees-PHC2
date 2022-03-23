package maxhyper.dtphc2.genfeatures;

import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class BananaFruitGenFeature extends PalmFruitGenFeature {

    public static final ConfigurationProperty<Fruit> FRUIT =
            ConfigurationProperty.property("fruit", Fruit.class);

    public BananaFruitGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(FRUIT, QUANTITY, FRUITING_RADIUS, PLACE_CHANCE, EXPAND_UP_FRUIT_HEIGHT, EXPAND_DOWN_FRUIT_HEIGHT);
    }

    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return new GenFeatureConfiguration(this)
                .with(FRUIT, Fruit.NULL)
                .with(QUANTITY, 8)
                .with(FRUITING_RADIUS, 6)
                .with(PLACE_CHANCE, 0.25f)
                .with(EXPAND_UP_FRUIT_HEIGHT, 0)
                .with(EXPAND_DOWN_FRUIT_HEIGHT, 0);
    }

    @Override
    protected void addFruit(GenFeatureConfiguration configuration, IWorld world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()) return;
        Direction placeDir = CoordUtils.HORIZONTALS[world.getRandom().nextInt(4)];
        BlockPos pos = expandRandom(configuration, world, leavesPos.offset(placeDir.getNormal()));
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
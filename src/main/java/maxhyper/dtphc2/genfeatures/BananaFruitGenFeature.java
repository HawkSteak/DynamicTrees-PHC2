package maxhyper.dtphc2.genfeatures;

import com.ferreusveritas.dynamictrees.api.configuration.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.compat.season.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import com.ferreusveritas.dynamictrees.systems.genfeature.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.util.CoordUtils;
import com.ferreusveritas.dynamictrees.util.LevelContext;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelAccessor;

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
    protected void addFruit(GenFeatureConfiguration configuration, LevelContext world, BlockPos rootPos, BlockPos leavesPos, boolean worldGen) {
        if (rootPos.getY() == leavesPos.getY()) return;
        LevelAccessor acc = world.accessor();
        Direction placeDir = CoordUtils.HORIZONTALS[acc.getRandom().nextInt(4)];
        BlockPos pos = expandRandom(configuration, acc, leavesPos.offset(placeDir.getNormal()));
        if (acc.getBlockState(pos).isAir()) {
            Float seasonValue = SeasonHelper.getSeasonValue(world, rootPos);
            Fruit fruit = configuration.get(FRUIT);
            if (worldGen) {
                fruit.placeDuringWorldGen(acc, pos, seasonValue);
            } else {
                fruit.place(acc, pos, seasonValue);
            }
        }
    }

}
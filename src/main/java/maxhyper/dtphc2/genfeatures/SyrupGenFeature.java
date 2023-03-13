package maxhyper.dtphc2.genfeatures;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.configurations.ConfigurationProperty;
import com.ferreusveritas.dynamictrees.api.network.MapSignal;
import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeatureConfiguration;
import com.ferreusveritas.dynamictrees.systems.genfeatures.context.PostGrowContext;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nonnull;

import static maxhyper.dtphc2.DynamicTreesPHC2.MOD_ID;

public class SyrupGenFeature extends GenFeature {

    private final ConfigurationProperty<Float> BASE_SYRUP_CHANCE = ConfigurationProperty.floatProperty("base_syrup_chance");
    private final ConfigurationProperty<Float> OUT_OF_SEASON_SYRUP_CHANCE = ConfigurationProperty.floatProperty("out_of_season_syrup_chance");

    public SyrupGenFeature(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected void registerProperties() {
        this.register(BASE_SYRUP_CHANCE, OUT_OF_SEASON_SYRUP_CHANCE);
    }

    @Nonnull
    @Override
    public GenFeatureConfiguration createDefaultConfiguration() {
        return super.createDefaultConfiguration()
                .with(BASE_SYRUP_CHANCE, 0.05F)
                .with(OUT_OF_SEASON_SYRUP_CHANCE, 0.001F);
    }

    @Override
    public boolean postGrow(@Nonnull GenFeatureConfiguration configuration, @Nonnull PostGrowContext context) {
        assert Minecraft.getInstance().player != null;
        Minecraft.getInstance().player.chat("syrup");
        World world = context.worldContext().level();
        boolean natural = context.natural();
        final BlockPos rootPos = context.pos();
        if (natural && (TreeHelper.getRadius(world, context.treePos()) >= 7) && (world.getRandom().nextFloat() <= getSyrupChance(world, rootPos, configuration))) {
            dripSyrup(world, rootPos);
        } else {
            LogManager.getLogger(MOD_ID).info("POST GROW BUT NO DRIP!");
        }
        return true;
    }

    //Update syrup extract rate depending on seasons
    public double getSyrupChance(World world, BlockPos pos, GenFeatureConfiguration config) {
        Float season = SeasonHelper.getSeasonValue(world, pos);
        ConfigurationProperty<Float> chanceProp = (season == null || SeasonHelper.isSeasonBetween(season, SeasonHelper.WINTER + 0.5f, SeasonHelper.SPRING + 0.5f)) ? BASE_SYRUP_CHANCE : OUT_OF_SEASON_SYRUP_CHANCE;
        float chance = config.get(chanceProp);
        return MathHelper.clamp(chance, 0.0f, 1.0f);
    }

    private void dripSyrup(World world, BlockPos rootPos) {
        LogManager.getLogger(MOD_ID).info("DRIPPING SYRUP!");
        TreeHelper.startAnalysisFromRoot(world, rootPos, new MapSignal(new DripSyrupNode()));
    }

}
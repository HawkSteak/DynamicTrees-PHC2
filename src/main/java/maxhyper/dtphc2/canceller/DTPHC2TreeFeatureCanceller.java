package maxhyper.dtphc2.canceller;

import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Objects;
import java.util.Set;

public class DTPHC2TreeFeatureCanceller extends FeatureCanceller {

    private final Class<?>[] treeFeatureClasses;

    public DTPHC2TreeFeatureCanceller(final ResourceLocation registryName, Class<?>... treeFeatureClass) {
        super(registryName);
        this.treeFeatureClasses = treeFeatureClass;
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, Set<String> namespaces) {
        Feature<?> feature = configuredFeature.feature();
        if (namespaces.contains(Objects.requireNonNull(feature.getRegistryName()).getNamespace())) {
            for (Class<?> treeFeatureClass : treeFeatureClasses){
                if (treeFeatureClass.isInstance(feature))
                    return true;
            }
        }
        return false;
    }

}

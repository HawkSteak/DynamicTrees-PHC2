package maxhyper.dtphc2.canceller;

import com.ferreusveritas.dynamictrees.api.worldgen.BiomePropertySelectors;
import com.ferreusveritas.dynamictrees.api.worldgen.FeatureCanceller;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.ForgeRegistries;

public class DTPHC2TreeFeatureCanceller extends FeatureCanceller {

    private final ResourceLocation[] treeFeatureClasses;

    public DTPHC2TreeFeatureCanceller(final ResourceLocation registryName, ResourceLocation[] treeFeatureClass) {
        super(registryName);
        this.treeFeatureClasses = treeFeatureClass;
    }

    @Override
    public boolean shouldCancel(ConfiguredFeature<?, ?> configuredFeature, BiomePropertySelectors.NormalFeatureCancellation featureCancellations) {
        Feature<?> feature = configuredFeature.feature();
        ResourceLocation resource = ForgeRegistries.FEATURES.getKey(feature);
        if (resource == null)
            return false;
        String namespace = resource.getNamespace();
        var namespaces = featureCancellations.getNamespaces();
        if (namespaces.contains(namespace)) {
            for (var treeFeatureClass : treeFeatureClasses){
                if (treeFeatureClass.compareTo(resource) == 0)
                    return true;
            }
        }
        return false;
    }

}

package maxhyper.dtphc2.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import maxhyper.dtphc2.DynamicTreesPHC2;

public class DTPHC2GenFeatures {

    public static final GenFeature BANANA_FRUIT = new BananaFruitGenFeature(DynamicTreesPHC2.resLoc("banana_fruit"));
    public static final GenFeature DRAGONFRUIT_FRUIT = new DragonfruitFruitGenFeature(DynamicTreesPHC2.resLoc("dragonfruit_fruit"));
    public static final GenFeature PALM_FRUIT = new PalmFruitGenFeature(DynamicTreesPHC2.resLoc("palm_fruit"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(BANANA_FRUIT, DRAGONFRUIT_FRUIT, PALM_FRUIT);
    }

}

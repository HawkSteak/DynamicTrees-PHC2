package maxhyper.dtphc2.genfeatures;

import com.ferreusveritas.dynamictrees.api.registry.Registry;
import com.ferreusveritas.dynamictrees.systems.genfeatures.GenFeature;
import maxhyper.dtphc2.DynamicTreesPHC2;

public class DTPHC2GenFeatures {

    public static final GenFeature BANANA_FRUIT = new BananaFruitGenFeature(DynamicTreesPHC2.resLoc("banana_fruit"));
    public static final GenFeature DRAGON_FRUIT_FRUIT = new DragonFruitFruitGenFeature(DynamicTreesPHC2.resLoc("dragon_fruit_fruit"));
    public static final GenFeature PALM_FRUIT = new PalmFruitGenFeature(DynamicTreesPHC2.resLoc("palm_fruit"));
    public static final GenFeature SYRUP_GEN = new SyrupGenFeature(DynamicTreesPHC2.resLoc("syrup_gen"));

    public static void register(final Registry<GenFeature> registry) {
        registry.registerAll(BANANA_FRUIT, DRAGON_FRUIT_FRUIT, PALM_FRUIT, SYRUP_GEN);
    }

}

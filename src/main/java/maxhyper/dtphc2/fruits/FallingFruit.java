package maxhyper.dtphc2.fruits;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import maxhyper.dtphc2.blocks.FallingFruitBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.ResourceLocation;

public class FallingFruit extends Fruit {

    public static final TypedRegistry.EntryType<Fruit> TYPE = TypedRegistry.newType(FallingFruit::new);

    public FallingFruit(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected FruitBlock createBlock(AbstractBlock.Properties properties) {
        return new FallingFruitBlock(properties, this);
    }
}

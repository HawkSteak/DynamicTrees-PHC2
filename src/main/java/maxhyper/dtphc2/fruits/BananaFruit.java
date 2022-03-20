package maxhyper.dtphc2.fruits;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class BananaFruit extends Fruit {

    public static final TypedRegistry.EntryType<Fruit> TYPE = TypedRegistry.newType(BananaFruit::new);

    public BananaFruit(ResourceLocation registryName) {
        super(registryName);
    }

    protected FruitBlock createBlock(Block.Properties properties) {
        return new FruitBlock(properties, this){
            @Override
            public boolean isSupported(IBlockReader world, BlockPos pos, BlockState state) {
                return world.getBlockState(pos.above(2)).getBlock() instanceof LeavesBlock;
            }
        };
    }

}

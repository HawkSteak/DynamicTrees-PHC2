package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FallingFruitBlock extends FruitBlock implements IFallingFruit {

    public FallingFruitBlock(Properties properties, Fruit fruit) {
        super(properties, fruit);
    }

    @Override
    public void doTick(BlockState state, World world, BlockPos pos, Random random) {
        if (checkToFall()){
            doFall(state, world, pos, random);
        } else
            super.doTick(state, world, pos, random);
    }
}

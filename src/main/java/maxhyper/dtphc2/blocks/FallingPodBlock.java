package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.blocks.PodBlock;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class FallingPodBlock extends PodBlock implements IFallingFruit {

    public FallingPodBlock(Properties properties, Pod pod) {
        super(properties, pod);
    }

    @Override
    public void doTick(BlockState state, World world, BlockPos pos, Random random) {
        if (checkToFall()){
            doFall(state, world, pos, random);
        } else
            super.doTick(state, world, pos, random);
    }
}

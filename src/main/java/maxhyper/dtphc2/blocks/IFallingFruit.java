package maxhyper.dtphc2.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public interface IFallingFruit {

    default boolean checkToFall(){
        return false;
    }

    default void doFall(BlockState state, World world, BlockPos pos, Random random){

    }

}

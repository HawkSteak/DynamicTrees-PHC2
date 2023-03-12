package maxhyper.dtphc2.fruits;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.PodBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import maxhyper.dtphc2.blocks.FallingPodBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import java.util.Random;

public class PalmPod extends Pod {

    public static final TypedRegistry.EntryType<Pod> TYPE = TypedRegistry.newType(PalmPod::new);

    public boolean doesFall = false;

    public void setDoesFall (boolean drops){
        System.out.println("aaaaaaaaaaaaaa");
        doesFall = drops;
    }

    public PalmPod(ResourceLocation registryName) {
        super(registryName);
    }

    protected PodBlock createBlock(Block.Properties properties) {
        if (doesFall){
            return new FallingPodBlock(properties, this){
                @Override public boolean isSupported(IBlockReader world, BlockPos pos, BlockState state) {
                    if (Minecraft.getInstance().player != null)
                        Minecraft.getInstance().player.chat("asafasa");
                    return PalmPod.this.isSupported(world, pos, state);
                }
            };
        } else {
            return new PodBlock(properties, this){
                @Override public boolean isSupported(IBlockReader world, BlockPos pos, BlockState state) {
                    return PalmPod.this.isSupported(world, pos, state);
                }
            };
        }
    }

    public boolean isSupported(IBlockReader world, BlockPos pos, BlockState state) {
        final BlockState branchState = world.getBlockState(pos.relative(state.getValue(HorizontalBlock.FACING)));
        final BranchBlock branch = TreeHelper.getBranch(branchState);
        return branch != null && branch.getRadius(branchState) >= 2;
    }

}

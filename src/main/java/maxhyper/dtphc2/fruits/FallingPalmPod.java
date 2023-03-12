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

public class FallingPalmPod extends Pod {

    public static final TypedRegistry.EntryType<Pod> TYPE = TypedRegistry.newType(FallingPalmPod::new);

    public FallingPalmPod(ResourceLocation registryName) {
        super(registryName);
    }

    protected PodBlock createBlock(Block.Properties properties) {
        return new FallingPodBlock(properties, this);
    }

}

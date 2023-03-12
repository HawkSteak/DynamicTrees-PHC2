package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.blocks.PodBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.pod.Pod;
import maxhyper.dtphc2.DynamicTreesPHC2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.Random;

public class FallingPodBlock extends PodBlock implements IFallingFruit {

    DamageSource damageSource;

    public static float randomFruitFallChance = 0.005f;
    public static float playerDistanceToFall = 10f;

    public FallingPodBlock(Properties properties, Pod pod) {
        super(properties, pod);
        damageSource = new DamageSource(DynamicTreesPHC2.MOD_ID+".falling_fruit."+ pod.getRegistryName().getPath());
    }

    @Override
    public boolean isSupported(IBlockReader world, BlockPos pos, BlockState state) {
        final BlockState branchState = world.getBlockState(pos.relative(state.getValue(HorizontalBlock.FACING)));
        final BranchBlock branch = TreeHelper.getBranch(branchState);
        return branch != null && branch.getRadius(branchState) == 3;
    }

    @Override
    public void doTick(BlockState state, World world, BlockPos pos, Random random) {
        if (checkToFall(state, world, pos, random)){
            System.out.println(this.asItem());
            doFall(state, world, pos);
        } else
            super.doTick(state, world, pos, random);
    }

    @Override
    public void neighborChanged(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        if (!isSupported(world, pos, state)) {
            if (!doFall(state, world, pos))
                super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        }
        DebugPacketSender.sendNeighborsUpdatePacket(world, pos);
    }

    @Override
    public DamageSource getDamageSource() {
        return damageSource;
    }

    @Override
    public int getRootY(BlockState state, World world, BlockPos pos) {
        Direction dir = state.getValue(FallingPodBlock.FACING);
        for (int i=0;i<20;i++){
            BlockPos pos2 = pos.offset(dir.getNormal()).below(i);
            if (world.getBlockState(pos2).getBlock() instanceof RootyBlock){
                return pos2.getY();
            }
        }
        return pos.getY();
    }

    @Override
    public ItemStack getDropOnFallItems(IItemProvider item, @Nonnull FallingBlockEntity entity) {
        if (entity.getServer() == null) return ItemStack.EMPTY;
        ServerWorld world = entity.getServer().getLevel(entity.level.dimension());
        if (world == null) return ItemStack.EMPTY;
        return getDrops(entity.getBlockState(), world, entity.blockPosition(), null).get(0);
    }

    public float getRandomFruitFallChance() {
        return randomFruitFallChance;
    }

    @Override
    public float getPlayerDistanceToFall() {
        return playerDistanceToFall;
    }
}

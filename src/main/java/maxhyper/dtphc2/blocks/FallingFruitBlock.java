package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import maxhyper.dtphc2.DynamicTreesPHC2;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.DebugPacketSender;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

public class FallingFruitBlock extends FruitBlock implements IFallingFruit {

    DamageSource damageSource;

    public static float randomFruitFallChance = 0.005f;
    public static float playerDistanceToFall = 10f;

    public FallingFruitBlock(Properties properties, Fruit fruit) {
        super(properties, fruit);
        damageSource = new DamageSource(DynamicTreesPHC2.MOD_ID+".falling_fruit."+ fruit.getRegistryName().getPath());
    }

    @Override
    public void doTick(BlockState state, World world, BlockPos pos, Random random) {
        if (checkToFall(state, world, pos, random)){
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
    public ItemStack getDropOnFallItems(IItemProvider item, @Nonnull FallingBlockEntity entity) {
        if (entity.getServer() == null) return ItemStack.EMPTY;
        ServerWorld world = entity.getServer().getLevel(entity.level.dimension());
        if (world == null) return ItemStack.EMPTY;
        List<ItemStack> items = getDrops(entity.getBlockState(), world, entity.blockPosition(), null);
        return items.isEmpty() ? ItemStack.EMPTY : items.get(0);
    }

    @Override
    public DamageSource getDamageSource() {
        return damageSource;
    }

    @Override
    public int getRootY(BlockState state, World world, BlockPos pos) {
        for (int i=0;i<20;i++){
            BlockPos pos2 = pos.below(i);
            if (world.getBlockState(pos2).getBlock() instanceof RootyBlock){
                return pos2.getY();
            }
        }
        return pos.getY();
    }

    @Override
    public float getRandomFruitFallChance() {
        return randomFruitFallChance;
    }

    @Override
    public float getPlayerDistanceToFall() {
        return playerDistanceToFall;
    }
}

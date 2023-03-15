package maxhyper.dtphc2.trees;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.branches.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.ThickBranchBlock;
import com.ferreusveritas.dynamictrees.init.DTConfigs;
import com.ferreusveritas.dynamictrees.trees.Family;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class PaperbarkFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(PaperbarkFamily::new);

    ItemStack paperStack;
    float barkRegrowChance = 0.01f;

    public PaperbarkFamily(ResourceLocation name) {
        super(name);
        paperStack = new ItemStack(Items.PAPER);
    }

    public ItemStack getPaperStack(World world) {
        ItemStack stack = paperStack.copy();
        stack.setCount(1 + world.random.nextInt(3));
        return stack;
    }

    @Override
    protected BranchBlock createBranchBlock(ResourceLocation name) {
        final BasicBranchBlock branch = new BasicBranchBlock(name, this.getProperties().randomTicks()){
            @Override
            public void stripBranch(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack heldItem) {
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), getPaperStack(world)));
                super.stripBranch(state,world,pos, getRadius(state));
            }
            @SuppressWarnings("deprecation")
            @Override
            public void tick(BlockState state, ServerWorld world, BlockPos pos, Random rand) {
                if (rand.nextFloat() < barkRegrowChance && isStrippedBranch()){
                    int radius = TreeHelper.getRadius(world, pos);
                    int radiusDown = TreeHelper.isBranch(world.getBlockState(pos.below())) ? TreeHelper.getRadius(world, pos.below()) : getMaxRadius();
                    this.getFamily().getBranch().ifPresent(branch -> branch.setRadius(world, pos,
                                    Math.min(radiusDown, radius + (DTConfigs.ENABLE_STRIP_RADIUS_REDUCTION.get() ? 1 : 0)),
                                    null
                            )
                    );
                }
                super.tick(state, world, pos, rand);
            }
        };
        if (this.isFireProof()) branch.setFireSpreadSpeed(0).setFlammability(0);
        return branch;
    }

}

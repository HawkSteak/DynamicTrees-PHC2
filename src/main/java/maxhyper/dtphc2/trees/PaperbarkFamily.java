package maxhyper.dtphc2.trees;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.branches.BasicBranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.ThickBranchBlock;
import com.ferreusveritas.dynamictrees.trees.Family;
import net.minecraft.block.BlockState;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class PaperbarkFamily extends Family {

    public static final TypedRegistry.EntryType<Family> TYPE = TypedRegistry.newType(PaperbarkFamily::new);

    ItemStack paperStack;

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
        final BasicBranchBlock branch = new BasicBranchBlock(name, this.getProperties()){
            @Override
            public void stripBranch(BlockState state, World world, BlockPos pos, PlayerEntity player, ItemStack heldItem) {
                world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), getPaperStack(world)));
                super.stripBranch(state,world,pos, getRadius(state));
            }
        };
        if (this.isFireProof()) branch.setFireSpreadSpeed(0).setFlammability(0);
        return branch;
    }

}

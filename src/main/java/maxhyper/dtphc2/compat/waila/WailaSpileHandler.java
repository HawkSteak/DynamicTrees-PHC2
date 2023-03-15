package maxhyper.dtphc2.compat.waila;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import com.ferreusveritas.dynamictrees.api.TreeRegistry;
import com.ferreusveritas.dynamictrees.compat.waila.WailaOther;
import com.ferreusveritas.dynamictrees.systems.nodemappers.NetVolumeNode;
import com.ferreusveritas.dynamictrees.trees.Species;
import maxhyper.dtphc2.blocks.MapleSpileBlock;
import maxhyper.dtphc2.blocks.MapleSpileBucketBlock;
import maxhyper.dtphc2.blocks.MapleSpileCommon;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import mcp.mobius.waila.api.IServerDataProvider;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class WailaSpileHandler implements IComponentProvider {

    private BlockPos lastPos = BlockPos.ZERO;
    private Species lastSpecies = Species.NULL_SPECIES;

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getBlockState().hasProperty(MapleSpileBlock.FILLED)) {
            boolean filled = accessor.getBlockState().getValue(MapleSpileBlock.FILLED);
            ITextComponent filledText = filled
                    ? new TranslationTextComponent("tooltip.dtphc2.maple_spile_filled")
                    : new TranslationTextComponent("tooltip.dtphc2.maple_spile_not_filled") ;
            tooltip.add(filledText);
        }
        if (accessor.getBlockState().hasProperty(MapleSpileBucketBlock.FILLING)) {
            int filling = accessor.getBlockState().getValue(MapleSpileBucketBlock.FILLING);
            float percent = filling / 3.0F * 100;
            tooltip.add(new TranslationTextComponent("tooltip.dtphc2.maple_spile_bucket_filling", String.format("%.0f%%", percent)));
        }
    }

    @Override
    public ItemStack getStack(IDataAccessor accessor, IPluginConfig config) {
        if (WailaOther.invalid) {
            lastPos = BlockPos.ZERO;
            lastSpecies = Species.NULL_SPECIES;

            WailaOther.invalid = false;
        }

        BlockPos pos = accessor.getPosition();
        Species species = Species.NULL_SPECIES;
//        CompoundNBT nbtData = accessor.getServerData();
//
//        //Attempt to get species from server via NBT data
//        if (nbtData.contains("species")) {
//            species = TreeRegistry.findSpecies(new ResourceLocation(nbtData.getString("species")));
//        }

        //Attempt to get species by checking if we're still looking at the same block
        if (lastPos.equals(pos)) {
            species = lastSpecies;
        }

        //Attempt to get species from the world as a last resort as the operation can be rather expensive
        BlockState state = accessor.getWorld().getBlockState(accessor.getPosition());
        if (species == Species.NULL_SPECIES){
            if (!state.hasProperty(MapleSpileCommon.FACING)) return ItemStack.EMPTY;
            Direction dir = state.getValue(MapleSpileCommon.FACING);
            species = TreeHelper.getExactSpecies(accessor.getWorld(), accessor.getPosition().offset(dir.getOpposite().getNormal()));
        }

        //If everything fails just show an iron ingot, womp womp
        if (species == Species.NULL_SPECIES) return new ItemStack(Items.IRON_INGOT);

        //Update the cached species and position
        lastSpecies = species;
        lastPos = pos;

        int count = 0;
        if (state.hasProperty(MapleSpileBlock.FILLED)) {
            count = state.getValue(MapleSpileBlock.FILLED) ? 1 : 0;
        } else if (state.hasProperty(MapleSpileBucketBlock.FILLING)) {
            count = state.getValue(MapleSpileBucketBlock.FILLING);
            count += count == 3 ? 1 : 0;
        }
        return new ItemStack(MapleSpileCommon.getSyrupItem(species), count);
        //return IComponentProvider.super.getStack(accessor, config);
    }
}
package maxhyper.dtphc2.compat.waila;

import java.util.List;

import maxhyper.dtphc2.blocks.FruitVineBlock;
import maxhyper.dtphc2.blocks.MapleSpileBlock;
import maxhyper.dtphc2.blocks.MapleSpileBucketBlock;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;

public class WailaSpileHandler implements IComponentProvider {

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
        //ItemStack syrup = new ItemStack(FruitRegistry.getLog(FruitRegistry.MAPLE).getFruitItem());
        //TODO: make dynamic
        ResourceLocation mapleSyrupRes = new ResourceLocation("pamhc2trees", "maplesyrupitem");
        Item mapleSyrup = ForgeRegistries.ITEMS.getValue(mapleSyrupRes);
        BlockState state = accessor.getWorld().getBlockState(accessor.getPosition());
        int count = 0;
        if (state.hasProperty(MapleSpileBlock.FILLED)) {
            count = state.getValue(MapleSpileBlock.FILLED) ? 1 : 0;
        } else if (state.hasProperty(MapleSpileBucketBlock.FILLING)) {
            count = state.getValue(MapleSpileBucketBlock.FILLING);
            count += count == 3 ? 1 : 0;
        }
        return new ItemStack(mapleSyrup, count);
        //return IComponentProvider.super.getStack(accessor, config);
    }
}
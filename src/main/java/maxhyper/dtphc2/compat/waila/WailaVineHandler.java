package maxhyper.dtphc2.compat.waila;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class WailaVineHandler implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        FruitBlock fruitBlock = (FruitBlock) accessor.getBlock();
        float growthValue = (fruitBlock.getAge(accessor.getBlockState()) * 100.0F) / fruitBlock.getMaxAge();
        if (growthValue < 100.0F) {
            tooltip.add(new TranslationTextComponent("tooltip.waila.crop_growth", String.format("%.0f%%", growthValue)));
        } else {
            tooltip.add(new TranslationTextComponent("tooltip.waila.crop_growth", new TranslationTextComponent("tooltip.waila.crop_mature")));
        }
    }
}

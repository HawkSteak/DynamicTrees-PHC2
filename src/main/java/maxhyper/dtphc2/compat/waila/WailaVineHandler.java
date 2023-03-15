package maxhyper.dtphc2.compat.waila;

import maxhyper.dtphc2.blocks.FruitVineBlock;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.IDataAccessor;
import mcp.mobius.waila.api.IPluginConfig;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.List;

public class WailaVineHandler implements IComponentProvider {

    @Override
    public void appendBody(List<ITextComponent> tooltip, IDataAccessor accessor, IPluginConfig config) {
        if (accessor.getBlock() instanceof FruitVineBlock) {

            FruitVineBlock fruitBlock = (FruitVineBlock) accessor.getBlock();
            float ageAsPercentage = fruitBlock.getAge(accessor.getBlockState()) * 100F / fruitBlock.getMatureAge();
            tooltip.add(new TranslationTextComponent(
                    "tooltip.waila.crop_growth",
                    ageAsPercentage < 100F ? String.format("%.0f%%", ageAsPercentage) :
                            new TranslationTextComponent("tooltip.waila.crop_mature").withStyle(TextFormatting.GREEN)
            ));
        }
    }
}

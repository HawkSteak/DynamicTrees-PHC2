package maxhyper.dtphc2.compat.waila;

import maxhyper.dtphc2.blocks.FruitVineBlock;
import mcp.mobius.waila.api.BlockAccessor;
import mcp.mobius.waila.api.IComponentProvider;
import mcp.mobius.waila.api.ITooltip;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.config.IPluginConfig;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.List;

public class WailaVineHandler implements IComponentProvider {

    public static WailaVineHandler INSTANCE = new WailaVineHandler();
    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig iPluginConfig) {
        if (accessor.getTooltipPosition() != TooltipPosition.BODY) return;
        if (accessor.getBlock() instanceof FruitVineBlock) {
            FruitVineBlock fruitBlock = (FruitVineBlock) accessor.getBlock();
            float ageAsPercentage = fruitBlock.getAge(accessor.getBlockState()) * 100F / fruitBlock.getMatureAge();
            tooltip.add(new TranslatableComponent(
                    "tooltip.waila.crop_growth",
                    ageAsPercentage < 100F ? String.format("%.0f%%", ageAsPercentage) :
                            new TranslatableComponent("tooltip.waila.crop_mature").withStyle(ChatFormatting.GREEN)
            ));
        }
    }
}

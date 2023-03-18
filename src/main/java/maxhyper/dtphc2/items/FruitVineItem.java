package maxhyper.dtphc2.items;

import com.ferreusveritas.dynamictrees.compat.seasons.SeasonHelper;
import com.ferreusveritas.dynamictrees.util.WorldContext;
import maxhyper.dtphc2.blocks.FruitVineBlock;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public class FruitVineItem extends BlockItem {

    FruitVineBlock vineBlock;

    public FruitVineItem(FruitVineBlock pBlock, Properties pProperties) {
        super(pBlock, pProperties);
        vineBlock = pBlock;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nullable World world, @Nonnull List<ITextComponent> tooltip, @Nonnull ITooltipFlag flagIn) {
        super.appendHoverText(stack, world, tooltip, flagIn);
        if (world == null) return;
        int flags = getSeasonalTooltipFlags(world);

        if (flags != 0) {
            tooltip.add(new TranslationTextComponent("desc.sereneseasons.fertile_seasons").append(":"));

            if ((flags & 15) == 15) {
                tooltip.add(new StringTextComponent(" ").append(new TranslationTextComponent("desc.sereneseasons.year_round").withStyle(TextFormatting.LIGHT_PURPLE)));
            } else {
                if ((flags & 1) != 0) {
                    tooltip.add(new StringTextComponent(" ").append(new TranslationTextComponent("desc.sereneseasons.spring").withStyle(TextFormatting.GREEN)));
                }
                if ((flags & 2) != 0) {
                    tooltip.add(new StringTextComponent(" ").append(new TranslationTextComponent("desc.sereneseasons.summer").withStyle(TextFormatting.YELLOW)));
                }
                if ((flags & 4) != 0) {
                    tooltip.add(new StringTextComponent(" ").append(new TranslationTextComponent("desc.sereneseasons.autumn").withStyle(TextFormatting.GOLD)));
                }
                if ((flags & 8) != 0) {
                    tooltip.add(new StringTextComponent(" ").append(new TranslationTextComponent("desc.sereneseasons.winter").withStyle(TextFormatting.AQUA)));
                }
            }
        }
    }

    public int getSeasonalTooltipFlags(final World world) {
        final float seasonStart = 1f / 6;
        final float seasonEnd = 1 - 1f / 6;
        final float threshold = 0.75f;

        Float seasonOffset = vineBlock.getSeasonOffset();

        int seasonFlags = 0;
        for (int i = 0; i < 4; i++) {
            boolean isValidSeason = false;
            if (seasonOffset != null) {
                final WorldContext worldContext = WorldContext.create(world);
                final float prod1 = SeasonHelper.globalSeasonalFruitProductionFactor(worldContext,
                        new BlockPos(0, (int) ((i + seasonStart - seasonOffset) * 64.0f), 0), true);
                final float prod2 = SeasonHelper.globalSeasonalFruitProductionFactor(worldContext,
                        new BlockPos(0, (int) ((i + seasonEnd - seasonOffset) * 64.0f), 0), true);
                if (Math.min(prod1, prod2) > threshold) {
                    isValidSeason = true;
                }

            } else {
                isValidSeason = true;
            }

            if (isValidSeason) {
                seasonFlags |= 1 << i;
            }

        }
        return seasonFlags;
    }

}

package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class DTPHC2Client {

    public static void setup (){
        registerRenderLayers();
        registerColorHandlers();

    }

    private static void registerRenderLayers() {
        RenderTypeLookup.setRenderLayer(DTPHC2Registries.PASSION_FRUIT_VINE, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(DTPHC2Registries.VANILLA_VINE, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(DTPHC2Registries.PEPPERCORN_VINE, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(DTPHC2Registries.MAPLE_SPILE_BUCKET_BLOCK, RenderType.cutout());
    }

    private static void registerColorHandlers() {

        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        final ItemColors itemColors = Minecraft.getInstance().getItemColors();

        Block[] vines = new Block[]{DTPHC2Registries.PASSION_FRUIT_VINE, DTPHC2Registries.VANILLA_VINE, DTPHC2Registries.PEPPERCORN_VINE};

        Item[] vineItems = new Item[]{DTPHC2Registries.PASSION_FRUIT_VINE_ITEM, DTPHC2Registries.VANILLA_VINE_ITEM, DTPHC2Registries.PEPPERCORN_VINE_ITEM};

        for (Block vine : vines){
            ModelHelper.regColorHandler(vine, (state, worldIn, pos, tintIndex) ->
                    blockColors.getColor(Blocks.VINE.defaultBlockState(), worldIn, pos, tintIndex)
            );
        }
        for (Item vineItem : vineItems){
            ModelHelper.regColorHandler(vineItem, (itemStack, tintIndex) ->
                    itemColors.getColor(new ItemStack(Items.VINE), tintIndex)
            );
        }

    }

}

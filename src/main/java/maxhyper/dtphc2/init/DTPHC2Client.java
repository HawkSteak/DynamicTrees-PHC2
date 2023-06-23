package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

public class DTPHC2Client {

    public static void setup (){
        registerRenderLayers();
        registerColorHandlers();

    }

    private static void registerRenderLayers() {
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Blocks.PASSION_FRUIT_VINE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Blocks.VANILLA_VINE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Blocks.PEPPERCORN_VINE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Blocks.MAPLE_SPILE_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Blocks.MAPLE_SPILE_BUCKET_BLOCK.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Blocks.BANANA_SUCKER_BLOCK.get(), RenderType.cutout());
    }

    private static void registerColorHandlers() {

        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        final ItemColors itemColors = Minecraft.getInstance().getItemColors();

        Block[] vines = new Block[]{DTPHC2Blocks.PASSION_FRUIT_VINE.get(), DTPHC2Blocks.VANILLA_VINE.get(), DTPHC2Blocks.PEPPERCORN_VINE.get()};

        Item[] vineItems = new Item[]{DTPHC2Items.PASSION_FRUIT_VINE_ITEM.get(), DTPHC2Items.VANILLA_VINE_ITEM.get(), DTPHC2Items.PEPPERCORN_VINE_ITEM.get()};

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

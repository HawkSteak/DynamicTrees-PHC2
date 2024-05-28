package maxhyper.dtphc2.init;

import com.ferreusveritas.dynamictrees.api.client.ModelHelper;
import maxhyper.dtphc2.blocks.MapleSpileCommon;
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
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Registries.PASSION_FRUIT_VINE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Registries.VANILLA_VINE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Registries.PEPPERCORN_VINE.get(), RenderType.cutout());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Registries.MAPLE_SPILE_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Registries.MAPLE_SPILE_BUCKET_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(DTPHC2Registries.BANANA_SUCKER_BLOCK.get(), RenderType.cutout());
    }

    private static void registerColorHandlers() {

        final BlockColors blockColors = Minecraft.getInstance().getBlockColors();
        final ItemColors itemColors = Minecraft.getInstance().getItemColors();

        Block[] vines = new Block[]{DTPHC2Registries.PASSION_FRUIT_VINE.get(), DTPHC2Registries.VANILLA_VINE.get(), DTPHC2Registries.PEPPERCORN_VINE.get()};

        Item[] vineItems = new Item[]{DTPHC2Registries.PASSION_FRUIT_VINE_ITEM.get(), DTPHC2Registries.VANILLA_VINE_ITEM.get(), DTPHC2Registries.PEPPERCORN_VINE_ITEM.get()};

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

        Block[] spiles = new Block[]{DTPHC2Registries.MAPLE_SPILE_BLOCK.get(), DTPHC2Registries.MAPLE_SPILE_BUCKET_BLOCK.get()};

        for (Block spile : spiles){
            ModelHelper.regColorHandler(spile, ((MapleSpileCommon)spile)::colorMultiplier);
        }

    }

}

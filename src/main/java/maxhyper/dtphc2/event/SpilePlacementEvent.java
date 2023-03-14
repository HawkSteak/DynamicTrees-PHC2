package maxhyper.dtphc2.event;

import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;

import java.util.Objects;

import static maxhyper.dtphc2.DynamicTreesPHC2.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class SpilePlacementEvent {

    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack heldItem = player.getItemInHand(hand);
        //TODO: make dynamic
        ResourceLocation mapleLogRes = new ResourceLocation("dtphc2", "maple_branch");
        Block mapleLog = ForgeRegistries.BLOCKS.getValue(mapleLogRes);
        if (state.getBlock() == mapleLog && heldItem.getItem() == Items.IRON_INGOT) {
            // Remove one item from the player's hand
            if (!player.isCreative()) {
                heldItem.shrink(1);
            }

            // Place the MapleSpileBlock at the clicked block's position
            BlockPos spilePos = pos.relative(Objects.requireNonNull(event.getFace()));
            if (world.getBlockState(spilePos).getMaterial().isReplaceable()) {
                BlockItemUseContext context = new BlockItemUseContext(player, hand, heldItem, new BlockRayTraceResult(player.getEyePosition(1.0f), event.getFace(), pos, false));
                BlockState placeState = DTPHC2Registries.MAPLE_SPILE_BLOCK.getStateForPlacement(context);
                world.setBlock(spilePos, placeState, 3);
                event.setCancellationResult(ActionResultType.SUCCESS);
                event.setCanceled(true);
            }
        }
    }
}
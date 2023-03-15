package maxhyper.dtphc2.event;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.genfeatures.DTPHC2GenFeatures;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.*;
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

        PlayerEntity player = event.getPlayer();
        Hand hand = event.getHand();
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.getItem() != Items.IRON_INGOT) return;

        World world = event.getWorld();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);

        if (!TreeHelper.isBranch(state) || TreeHelper.getRadius(world, pos) < 7) return;

        BlockPos spilePos = pos.relative(Objects.requireNonNull(event.getFace()));
        if (!world.getBlockState(spilePos).getMaterial().isReplaceable()) return;

        //Make sure the tree species has the syrup gen feature
        if (BlockTags.getAllTags().getTagOrEmpty(DynamicTreesPHC2.resLoc("can_be_spiled")).contains(state.getBlock())) {
            // Remove one item from the player's hand
            if (!player.isCreative()) heldItem.shrink(1);
            // Play a sound
            world.playSound(null, pos, SoundEvents.AXE_STRIP, SoundCategory.BLOCKS, 1,1);
            // Place the MapleSpileBlock at the clicked block's position
            BlockItemUseContext context = new BlockItemUseContext(player, hand, heldItem, new BlockRayTraceResult(player.getEyePosition(1.0f), event.getFace(), pos, false));
            BlockState placeState = DTPHC2Registries.MAPLE_SPILE_BLOCK.getStateForPlacement(context);
            if (placeState == null) return;
            world.setBlock(spilePos, placeState, 3);
            event.setCancellationResult(ActionResultType.SUCCESS);
            event.setCanceled(true);

        }
    }
}
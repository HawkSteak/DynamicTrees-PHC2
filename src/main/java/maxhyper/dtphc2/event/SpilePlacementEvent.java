package maxhyper.dtphc2.event;

import com.ferreusveritas.dynamictrees.api.TreeHelper;
import maxhyper.dtphc2.init.DTPHC2Blocks;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

import static maxhyper.dtphc2.DynamicTreesPHC2.MOD_ID;

@Mod.EventBusSubscriber(modid = MOD_ID)
public class SpilePlacementEvent {


    @SubscribeEvent
    public static void onPlayerRightClickBlock(PlayerInteractEvent.RightClickBlock event) {

        Player player = event.getEntity();
        InteractionHand hand = event.getHand();
        ItemStack heldItem = player.getItemInHand(hand);

        if (heldItem.getItem() != Items.IRON_INGOT) return;

        Level world = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = world.getBlockState(pos);

        if (!TreeHelper.isBranch(state) || TreeHelper.getRadius(world, pos) < 7) return;

        BlockPos spilePos = pos.relative(Objects.requireNonNull(event.getFace()));
        if (!world.getBlockState(spilePos).isAir()) return;
        //Make sure the tree species has the syrup gen feature
        //TODO
        if(state.is(DTPHC2Registries.CAN_BE_SPILED)){
            // Remove one item from the player's hand
            if (!player.isCreative()) heldItem.shrink(1);
            // Play a sound
            world.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1,1);
            // Place the MapleSpileBlock at the clicked block's position
            BlockPlaceContext context = new BlockPlaceContext(player, hand, heldItem, new BlockHitResult(player.getEyePosition(1.0f), event.getFace(), pos, false));
            BlockState placeState = DTPHC2Registries.MAPLE_SPILE_BLOCK.get().getStateForPlacement(context);
            if (placeState == null) return;
            world.setBlock(spilePos, placeState, 3);
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);

        }
    }
}
package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.google.common.collect.Lists;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.block.BlockState;
import net.minecraft.block.FallingBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public interface IFallingFruit {

    int fallDamageMax = 40;
    float fallDamageAmount = 2.0F;

    DamageSource getDamageSource();

    default boolean checkToFall(BlockState state, World world, BlockPos pos, Random random){
        if (getAge(state) < getMaxAge()) return false;
        return random.nextFloat() <= getRandomFruitFallChance();
    }

    int getRootY (BlockState state, World world, BlockPos pos);

    default boolean doFall(BlockState state, World world, BlockPos pos){
        if (getPlayerDistanceToFall() <= 0) return false; //if distance is 0 block falling is disabled.
        int rootY = getRootY(state, world, pos);
        //if a player is not nearby don't bother, this is a gimmick to damage the player >:)
        if (!world.hasNearbyAlivePlayer(pos.getX(), rootY, pos.getZ(), getPlayerDistanceToFall())){
            return false;
        }
        if (pos.getY() >= 0 && FallingBlock.isFree(world.getBlockState(pos.below()))) {
            if (world.isAreaLoaded(pos, 32)) {
                if (!world.isClientSide()) {
                    FallingBlockEntity fallingBlockEntity = getFallingEntity(world, pos, state);
                    world.addFreshEntity(fallingBlockEntity);
                    return true;
                }
            }
        }
        return false;
    }

    default FallingBlockEntity getFallingEntity (World world, BlockPos pos, BlockState state){
        return new FallingBlockEntity(world, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, state){
            @Override
            public boolean causeFallDamage(float pFallDistance, float pDamageMultiplier) {
                int i = MathHelper.ceil(pFallDistance - 1.0F);
                if (i > 0) {
                    List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox()));
                    for(Entity entity : list) {
                        entity.hurt(getDamageSource(),
                                (float)Math.min(MathHelper.floor((float)i * IFallingFruit.fallDamageAmount),
                                        IFallingFruit.fallDamageMax)
                        );
                        level.playSound(null, pos,
                                DTPHC2Registries.FRUIT_BONK.get(), SoundCategory.BLOCKS,
                                1.0F, 1.0F);
                    }
                }
                return false;
            }

            @Nullable
            @Override
            public ItemEntity spawnAtLocation(@Nonnull IItemProvider pItem) {
                return this.spawnAtLocation(getDropOnFallItems(pItem, this), 0);
            }
        };
    }

    ItemStack getDropOnFallItems(IItemProvider item, FallingBlockEntity entity);
    float getRandomFruitFallChance ();
    float getPlayerDistanceToFall();
    int getAge(BlockState state);
    int getMaxAge();
}

package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.util.MathHelper;
import com.google.common.collect.Lists;
import maxhyper.dtphc2.init.DTPHC2Registries;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public interface IFallingFruit {

    int fallDamageMax = 40;
    float fallDamageAmount = 2.0F;

    DamageSource getDamageSource();

    default boolean checkToFall(BlockState state, Level world, BlockPos pos, Random random){
        if (getAge(state) < getMaxAge()) return false;
        return random.nextFloat() <= getRandomFruitFallChance();
    }

    int getRootY (BlockState state, Level world, BlockPos pos);

    default boolean doFall(BlockState state, Level world, BlockPos pos){
        if (getPlayerDistanceToFall() <= 0) return false; //if distance is 0 block falling is disabled.
        int rootY = getRootY(state, world, pos);
        //if a player is not nearby don't bother, this is a gimmick to damage the player >:)
        if (!world.hasNearbyAlivePlayer(pos.getX(), rootY, pos.getZ(), getPlayerDistanceToFall())){
            return false;
        }
        if (pos.getY() >= 0 && FallingBlock.isFree(world.getBlockState(pos.below()))) {
            if (world.isAreaLoaded(pos, 32)) {
                if (!world.isClientSide()) {
                    //FallingBlockEntity fallingBlockEntity = getFallingEntity(world, pos, state);
                    //world.addFreshEntity(fallingBlockEntity);
                    return true;
                }
            }
        }
        return false;
    }

    //TODO
//    default FallingBlockEntity getFallingEntity (Level world, BlockPos pos, BlockState state){
//        //TODO
//        return new FallingBlockEntity(world, (double)pos.getX() + 0.5D, pos.getY(), (double)pos.getZ() + 0.5D, state){
//            @Override
//            public boolean causeFallDamage(float pFallDistance, float pDamageMultiplier, DamageSource source) {
//                int i = Mth.ceil(pFallDistance - 1.0F);
//                if (i > 0) {
//                    List<Entity> list = Lists.newArrayList(this.level.getEntities(this, this.getBoundingBox()));
//                    for(Entity entity : list) {
//                        if (entity instanceof LivingEntity){
//                            entity.hurt(getDamageSource(),
//                                    (float)Math.min(Mth.floor((float)i * IFallingFruit.fallDamageAmount), IFallingFruit.fallDamageMax));
//                            level.playSound(null, pos,
//                                    DTPHC2Registries.FRUIT_BONK.get(), SoundSource.BLOCKS,
//                                    1.0F, 1.0F);
//                        }
//                    }
//                }
//                return false;
//            }
//
//            @Nullable
//            @Override
//            public ItemEntity spawnAtLocation(@Nonnull ItemLike pItem) {
//                return this.spawnAtLocation(getDropOnFallItems(pItem, this), 0);
//            }
//        };
//    }

    ItemStack getDropOnFallItems(ItemLike item, FallingBlockEntity entity);
    float getRandomFruitFallChance ();
    float getPlayerDistanceToFall();
    int getAge(BlockState state);
    int getMaxAge();
}

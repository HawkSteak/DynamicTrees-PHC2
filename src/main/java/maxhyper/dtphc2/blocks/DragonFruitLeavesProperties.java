package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.PalmLeavesProperties;
import maxhyper.dtphc2.DynamicTreesPHC2;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DragonFruitLeavesProperties extends PalmLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(DragonFruitLeavesProperties::new);

    public DragonFruitLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    public Material getDefaultMaterial() {
        return Material.CACTUS;
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(AbstractBlock.Properties properties) {
        return new DynamicDragonfruitLeavesBlock(this, properties);
    }

    public static class DynamicDragonfruitLeavesBlock extends DynamicPalmLeavesBlock {

        public DynamicDragonfruitLeavesBlock(LeavesProperties leavesProperties, Properties properties) {
            super(leavesProperties, properties.strength(0.4F).sound(SoundType.WOOL));
        }

        private static final double hurtMovementDelta = 0.003;
        @Override
        public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entity) {
            boolean damage = false;
            if (DynamicTreesPHC2.DTPlusConfig.cactusPrickleOnMoveOnlyConfig() && entity instanceof LivingEntity) {
                boolean falling = entity.getDeltaMovement().y < 0;
                entity.setDeltaMovement(entity.getDeltaMovement().x * 0.25, entity.getDeltaMovement().y * (falling?0.5:1), entity.getDeltaMovement().z  * 0.25);
                if (!worldIn.isClientSide && (entity.xOld != entity.getX() || entity.yOld != entity.getY() || entity.zOld != entity.getZ())) {
                    double xMovement = Math.abs(entity.getX() - entity.xOld);
                    double yMovement = Math.abs(entity.getY() - entity.yOld);
                    double zMovement = Math.abs(entity.getZ() - entity.zOld);
                    if (xMovement >= hurtMovementDelta || yMovement >= hurtMovementDelta || zMovement >= hurtMovementDelta) {
                        damage = true;
                    }
                }
            } else if (!(entity instanceof ItemEntity) || DynamicTreesPHC2.DTPlusConfig.cactusKillItemsConfig()) {
                damage = true;
            }

            if (damage) entity.hurt(DamageSource.CACTUS, 1.0F);
        }


    }

}

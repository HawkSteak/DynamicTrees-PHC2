package maxhyper.dtphc2.blocks;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.leaves.DynamicLeavesBlock;
import com.ferreusveritas.dynamictrees.blocks.leaves.LeavesProperties;
import com.ferreusveritas.dynamictrees.blocks.leaves.PalmLeavesProperties;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.ResourceLocation;

public class DragonfruitLeavesProperties extends PalmLeavesProperties {

    public static final TypedRegistry.EntryType<LeavesProperties> TYPE = TypedRegistry.newType(DragonfruitLeavesProperties::new);

    public DragonfruitLeavesProperties(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected DynamicLeavesBlock createDynamicLeaves(AbstractBlock.Properties properties) {
        return new DynamicDragonfruitLeavesBlock(this, properties);
    }

    public static class DynamicDragonfruitLeavesBlock extends DynamicPalmLeavesBlock {

        public DynamicDragonfruitLeavesBlock(LeavesProperties leavesProperties, Properties properties) {
            super(leavesProperties, properties);
        }

    }

}

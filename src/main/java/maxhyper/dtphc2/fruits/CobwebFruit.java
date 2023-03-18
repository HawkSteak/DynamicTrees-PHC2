package maxhyper.dtphc2.fruits;

import com.ferreusveritas.dynamictrees.api.registry.TypedRegistry;
import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.systems.fruit.Fruit;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class CobwebFruit extends Fruit {

    public static final TypedRegistry.EntryType<Fruit> TYPE = TypedRegistry.newType(CobwebFruit::new);

    public CobwebFruit(ResourceLocation registryName) {
        super(registryName);
    }

    @Override
    protected FruitBlock createBlock(Block.Properties properties) {
        return new FruitBlock(properties.noCollission(), this){
            @Override
            public void entityInside(BlockState pState, World pLevel, BlockPos pPos, Entity pEntity) {
                pEntity.makeStuckInBlock(pState, new Vector3d(0.25D, (double)0.05F, 0.25D));
            }
        };
    }

    public Material getDefaultMaterial() {
        return Material.WEB;
    }

}

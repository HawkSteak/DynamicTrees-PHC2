package maxhyper.dtphc2.models;

import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.models.baked_models.MediumPalmLeavesBakedModel;
import maxhyper.dtphc2.models.baked_models.LargePalmLeavesBakedModel;
import maxhyper.dtphc2.models.baked_models.SmallPalmLeavesBakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DynamicTreesPHC2.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModelBakeEventHandler {

    @SubscribeEvent
    public static void onModelRegistryEvent(ModelRegistryEvent event) {
        ModelLoaderRegistry.registerLoader(new ResourceLocation(DynamicTreesPHC2.MOD_ID, "large_palm_fronds"), new PalmLeavesModelLoader(0));
        ModelLoaderRegistry.registerLoader(new ResourceLocation(DynamicTreesPHC2.MOD_ID, "medium_palm_fronds"), new PalmLeavesModelLoader(1));
        ModelLoaderRegistry.registerLoader(new ResourceLocation(DynamicTreesPHC2.MOD_ID, "small_palm_fronds"), new PalmLeavesModelLoader(2));
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        // Setup fronds models
        MediumPalmLeavesBakedModel.INSTANCES.forEach(MediumPalmLeavesBakedModel::setupModels);
        LargePalmLeavesBakedModel.INSTANCES.forEach(LargePalmLeavesBakedModel::setupModels);
        SmallPalmLeavesBakedModel.INSTANCES.forEach(SmallPalmLeavesBakedModel::setupModels);
    }

}
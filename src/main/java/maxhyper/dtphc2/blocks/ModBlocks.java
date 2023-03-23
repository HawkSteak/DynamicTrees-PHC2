package maxhyper.dtphc2.blocks;

import com.pam.pamhc2trees.init.ItemRegistry;
import maxhyper.dtphc2.DynamicTreesPHC2;
import maxhyper.dtphc2.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static maxhyper.dtphc2.items.ModItems.RIPE_PEPPERCORN_ITEM;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, DynamicTreesPHC2.MOD_ID);


    public static final RegistryObject<Block> BANANA_SUCKER_BLOCK = registerBlock("banana_sucker", BananaSuckerBlock::new, CreativeModeTab.TAB_MISC);
    public static final RegistryObject<FruitVineBlock> PASSION_FRUIT_VINE = registerBlock("passion_fruit_vine",
            () -> new FruitVineBlock().setSeasonOffset(0f), CreativeModeTab.TAB_MISC); //summer
    public static final RegistryObject<FruitVineBlock> VANILLA_VINE = registerBlock("vanilla_vine",
            () -> new FruitVineBlock().setSeasonOffset(2f), CreativeModeTab.TAB_MISC); //winter
    public static final RegistryObject<FruitVineBlock> PEPPERCORN_VINE = registerBlock("peppercorn_vine",
            () -> new FruitVineBlock().setSeasonOffset(0f)
                    .setOverripeFruitStack(new ItemStack(RIPE_PEPPERCORN_ITEM.get()))
                    .setMatureAge(3)
                    .setFruitOverripenChance(0.01f), CreativeModeTab.TAB_MISC); //summer
    public static final RegistryObject<Block> MAPLE_SPILE_BLOCK = registerBlock("maple_spile", MapleSpileBlock::new, CreativeModeTab.TAB_MISC);
    public static final RegistryObject<Block> MAPLE_SPILE_BUCKET_BLOCK = registerBlock("maple_spile_bucket", MapleSpileBucketBlock::new, CreativeModeTab.TAB_MISC);


    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn, tab);
        return toReturn;
    }

    private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block,
                                                                            CreativeModeTab tab) {
        return ModItems.ITEMS.register(name, () -> new BlockItem(block.get(),
                new Item.Properties().tab(tab)));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

}

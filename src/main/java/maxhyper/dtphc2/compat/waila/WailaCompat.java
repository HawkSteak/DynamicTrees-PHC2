package maxhyper.dtphc2.compat.waila;

import com.ferreusveritas.dynamictrees.blocks.FruitBlock;
import com.ferreusveritas.dynamictrees.blocks.PodBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.BranchBlock;
import com.ferreusveritas.dynamictrees.blocks.branches.TrunkShellBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.RootyBlock;
import com.ferreusveritas.dynamictrees.blocks.rootyblocks.WaterSoilProperties;
import com.ferreusveritas.dynamictrees.compat.waila.*;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        WailaBranchHandler branchHandler = new WailaBranchHandler();
        WailaRootyHandler rootyHandler = new WailaRootyHandler();

        registrar.registerComponentProvider(new WailaPodHandler(), TooltipPosition.BODY, PodBlock.class);
    }

}

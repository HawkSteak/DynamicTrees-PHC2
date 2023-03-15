package maxhyper.dtphc2.compat.waila;

import maxhyper.dtphc2.blocks.FruitVineBlock;
import maxhyper.dtphc2.blocks.MapleSpileCommon;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {

    @Override
    public void register(IRegistrar registrar) {
        //WailaBranchHandler branchHandler = new WailaBranchHandler();
        //WailaRootyHandler rootyHandler = new WailaRootyHandler();

        registrar.registerComponentProvider(new WailaVineHandler(), TooltipPosition.BODY, FruitVineBlock.class);
        registrar.registerComponentProvider(new WailaSpileHandler(), TooltipPosition.BODY, MapleSpileCommon.class);
        registrar.registerStackProvider(new WailaSpileHandler(), MapleSpileCommon.class);
    }

}

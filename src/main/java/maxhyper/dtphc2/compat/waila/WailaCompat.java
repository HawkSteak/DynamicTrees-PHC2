package maxhyper.dtphc2.compat.waila;

import maxhyper.dtphc2.blocks.FruitVineBlock;
import maxhyper.dtphc2.blocks.MapleSpileCommon;
import mcp.mobius.waila.api.*;

@WailaPlugin
public class WailaCompat implements IWailaPlugin {

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerComponentProvider(WailaVineHandler.INSTANCE, TooltipPosition.BODY, FruitVineBlock.class);
        registration.registerComponentProvider(WailaSpileHandler.INSTANCE, TooltipPosition.BODY, MapleSpileCommon.class);
        registration.registerIconProvider(WailaSpileHandler.INSTANCE, MapleSpileCommon.class);
    }

    @SuppressWarnings("removal")
    @Override
    public void register(IRegistrar registrar) {
        //registrar.registerStackProvider(new WailaSpileHandler(), MapleSpileCommon.class);
    }

}

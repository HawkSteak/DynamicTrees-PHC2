package maxhyper.dtphc2.compat;

public class DefaultConfig implements DTPConfigProxy {

    @Override
    public boolean cactusPrickleOnMoveOnlyConfig() {
        return false;
    }

    @Override
    public boolean cactusKillItemsConfig() {
        return true;
    }

    @Override
    public boolean canBoneMealCactusConfig() {
        return false;
    }

}

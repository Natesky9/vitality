package com.natesky9.Hitsplats;

import net.runelite.api.Hitsplat;
import net.runelite.api.HitsplatID;

public class TaxSplat implements Hitsplat {
    int amount;
    int cycle;

    public TaxSplat(int amount, int cycle)
    {
        this.amount = amount;
        this.cycle = cycle;
    }
    @Override
    public int getHitsplatType() {
        return HitsplatID.DAMAGE_ME_POISE;
    }

    @Override
    public int getAmount() {
        return amount;
    }

    @Override
    public int getDisappearsOnGameCycle() {
        return cycle;
    }
}

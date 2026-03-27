package com.natesky9.Hitsplats;

import net.runelite.api.Hitsplat;
import net.runelite.api.HitsplatID;

public class RestoreSplat implements Hitsplat {
    int amount;
    int cycle;

    public RestoreSplat(int amount, int cycle)
    {
        this.amount = amount;
        this.cycle = cycle;
    }
    @Override
    public int getHitsplatType() {
        return HitsplatID.CYAN_UP;
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

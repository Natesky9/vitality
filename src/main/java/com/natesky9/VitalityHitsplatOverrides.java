package com.natesky9;


import net.runelite.client.game.SpriteManager;
import net.runelite.client.game.SpriteOverride;

import javax.inject.Inject;


public class VitalityHitsplatOverrides {
    @Inject
    private SpriteManager spriteManager;
    private static final SpriteOverride[] overrides = new SpriteOverride[Sprites.ALL_SPRITES.length];

    public void replaceHitsplats()
    {
        for (int i=0; i < Sprites.ALL_SPRITES.length;i++)
        {
            int id = Sprites.ALL_SPRITES[i];
            overrides[i] = new SpriteOverride()
            {
                @Override
                public int getSpriteId() {
                    return id;
                }
                @Override
                public String getFileName() {
                    return "/blank.png";
                }
            };
        }
        spriteManager.addSpriteOverrides(overrides);
    }
    public void revertHitsplats()
    {
        spriteManager.removeSpriteOverrides(overrides);
    }
}

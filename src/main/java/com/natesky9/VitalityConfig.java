package com.natesky9;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Range;

@ConfigGroup("Vitality")
public interface VitalityConfig extends Config
{
	@ConfigItem(
			keyName = "ignoreRegen",
			name = "Ignore Regeneration",
			description = "Shows regeneration healing"
	)
    default boolean ignoreRegen()
	{
		return true;
	}
	@ConfigItem(
			keyName = "healRise",
			name = "Fancy Heal",
			description = "Heal splat does a gentle rise"
	)
	default boolean healRise()
	{
		return true;
	}
	@ConfigItem(
			keyName = "healScaling",
			name = "Scales with heal",
			description = "Size scales with bigger heals"
	)
	default boolean healScaling()
	{
		return false;
	}
	
	@Range(min = Integer.MIN_VALUE)
	@ConfigItem(
			keyName = "zOffset",
			name = "Hitsplat offset",
			description = "Offsets the hitsplat on the Z axis"
	)
	default int zOffset()
	{
		return 180;
	}
}

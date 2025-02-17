package com.natesky9;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("Vitality")
public interface VitalityConfig extends Config
{
	@ConfigItem(
			keyName = "ignoreRegen",
			name = "Ignore Regeneration",
			description = "Shows regeneration healing"
	)
    static boolean ignoreRegen()
	{
		return true;
	}
}

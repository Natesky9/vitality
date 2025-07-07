package com.natesky9;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("Vitality")
public interface VitalityConfig extends Config
{
	@ConfigSection(
			name = "General settings",
			description = "Basic toggles and options",
			position = 0,
			closedByDefault = false
	)
	String generalSettings = "General settings";
	@ConfigSection(
			name = "Fun settings",
			description = "Cosmetic and playful options",
			position = 1,
			closedByDefault = true
	)
	String funSettings = "Fun settings";
	//-----------------//
	@ConfigItem(
			keyName = "anchorPoint",
			name = "Anchor Point",
			description = "Location to draw heal splats",
			section = generalSettings
	)
	default AnchorPoints anchorPoints()
	{
		return AnchorPoints.CHEST;
	}

	enum AnchorPoints
	{
		HEAD,
		CHEST,
		FEET
	}
	@ConfigItem(
			keyName = "ignoreRegen",
			name = "Ignore Regeneration",
			description = "Shows regeneration healing",
			section = generalSettings
	)
    default boolean ignoreRegen()
	{
		return true;
	}

	@ConfigItem(
			keyName = "excludeFood",
			name = "Exclude Food",
			description = "Ignore food/potion healing",
			section = generalSettings
	)
	default boolean excludeFood()
	{
		return false;
	}
	@ConfigItem(
			keyName = "displayTickEat",
			name = "Display Tickeats",
			description = "Show when an attack was tick ate",
			section = generalSettings
	)
	default boolean displayTickEat()
	{
		return true;
	}

	@ConfigItem(
			keyName = "healRise",
			name = "Fancy Heal",
			description = "Heal splat does a gentle rise",
			section = funSettings
	)
	default boolean healRise()
	{
		return true;
	}

	@ConfigItem(
			keyName = "healScaling",
			name = "Scales with heal",
			description = "Size scales with bigger heals",
			section = funSettings
	)
	default boolean healScaling()
	{
		return false;
	}
	@ConfigItem(
			keyName = "aprilFools",
			name = "April fools",
			description = "enables fun",
			section = funSettings
	)
	default boolean aprilFools()
	{
		return true;
	}
}

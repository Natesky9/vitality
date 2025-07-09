package com.natesky9;

import net.runelite.client.config.*;

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
			position = 2,
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
			position = 3,
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
			position = 4,
			description = "Ignore food/potion healing",
			section = generalSettings
	)
	default boolean excludeFood()
	{
		return false;
	}
	@ConfigItem(
			keyName = "displayTickEat",
			name = "Display Tickeats (experimental)",
			position = 5,
			description = "Show when an attack was tick ate",
			section = generalSettings
	)
	default boolean displayTickEat()
	{
		return true;
	}
	@ConfigItem(
			keyName = "TickEatSound",
			name = "Sound ID (experimental)",
			position = 6,
			description = "Sound id for when an attack is successfully tick eaten. May return false positives. set to -1 to disable",
			section = generalSettings
	)
	default int tickEatSound()
	{
		return 5190;
	}
	//fun settings
	@ConfigItem(
			keyName = "healRise",
			name = "Fancy Heal",
			position = 0,
			description = "Heal splat does a gentle rise",
			section = funSettings
	)
	default boolean healRise()
	{
		return true;
	}
	@ConfigItem(
			keyName = "riseSpeed",
			name = "Rise Speed",
			position = 1,
			description = "How quickly the heal rises, or drops!",
			section = funSettings
	)
	@Range(
			min = -20,
			max = 20
	)
	default int riseSpeed()
	{
		return 2;
	}

	@ConfigItem(
			keyName = "healScaling",
			name = "Scales with heal",
			position = 2,
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
			position = 3,
			description = "enables fun (only active one day a year)",
			section = funSettings
	)
	default boolean aprilFools()
	{
		return true;
	}
}

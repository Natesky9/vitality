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
	//region anchorPoint
	@ConfigItem(
			keyName = "anchorPoint",
			name = "Anchor Point",
			position = 2,
			description = "Depricated, will come back soon!",
			section = generalSettings
	)
	default AnchorPoints anchorPoints()
	{
		return AnchorPoints.BELOW;
	}
	enum AnchorPoints
	{
		ABOVE,
		BELOW,
		LEFT,
		RIGHT
	}
	//endregion anchorPoint
	//region ignoreRegen
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
	//endregion ignoreRegen
	//region excludeFood
	@ConfigItem(
			keyName = "excludeFood",
			name = "Exclude Food",
			position = 4,
			description = "Ignore food healing",
			section = generalSettings
	)
	default boolean excludeFood()
	{
		return false;
	}
	//endregion excludeFood
	//region excludeDrink
	@ConfigItem(
			keyName = "excludeDrink",
			name = "Exclude Drink",
			position = 5,
			description = "Ignore prayer restore",
			section = generalSettings
	)
	default boolean excludeDrink()
	{
		return false;
	}
	//endregion excludeDrink
	@ConfigItem(
			keyName = "displayTickEat",
			name = "Display Tickeats (experimental)",
			position = 6,
			description = "Show when an attack was tick ate",
			section = generalSettings
	)
	default boolean displayTickEat()
	{
		return true;
	}
	//region tickEatSound
	@ConfigItem(
			keyName = "TickEatSound",
			name = "Sound ID (experimental)",
			position = 7,
			description = "Sound id for when an attack is successfully tick eaten. May return false positives. set to -1 to disable",
			section = generalSettings
	)
	default int tickEatSound()
	{
		return 5190;
	}
	//endregion tickEatSound
	//region healScaling
	@ConfigItem(
			keyName = "healScaling",
			name = "Scales with heal",
			position = 0,
			description = "Size scales with bigger heals",
			section = generalSettings
	)
	default boolean healScaling()
	{
		return false;
	}
	//endregion healScaling
	//fun settings
	//@ConfigItem(
	//		keyName = "healRise",
	//		name = "Fancy Heal",
	//		position = 0,
	//		description = "Heal splat does a gentle rise",
	//		section = funSettings
	//)
	//default boolean healRise()
	//{
	//	return true;
	//}
	//@ConfigItem(
	//		keyName = "riseSpeed",
	//		name = "Rise Speed",
	//		position = 1,
	//		description = "How quickly the heal rises, or drops!",
	//		section = funSettings
	//)
	//@Range(
	//		min = 0,
	//		max = 20
	//)
	//default int riseSpeed()
	//{
	//	return 2;
	//}

	//region aprilFools1
	@ConfigItem(
			keyName = "aprilFools1",
			name = "Enable April Fools",
			position = 1,
			description = "Enables the april fools feature (read your chatbox after unchecking)",
			section = funSettings
	)
	default boolean aprilFools1()
	{
		return true;
	}
	//region aprilFools1
	//region aprilFools2
	@ConfigItem(
			keyName = "aprilFools2",
			name = "Pet the dog for luck",
			position = 2,
			description = "This doesn't actually give luck unfortunately. But if it did, amazing!",
			section = funSettings
	)
	default boolean aprilFools2()
	{
		return true;
	}
	//region aprilFools2
	//region aprilFools3
	@ConfigItem(
			keyName = "aprilFools3",
			name = "Refill Vials",
			position = 3,
			description = "Fun fact: Phials, Niles, Miles, and Giles are the Certer Brothers. Giles is shorter than Miles, who is shorter than Niles",
			section = funSettings
	)
	default boolean aprilFools3()
	{
		return true;
	}
	//region aprilFools3
	//region aprilFools4
	@ConfigItem(
			keyName = "aprilFools4",
			name = "Burn Sharks",
			position = 4,
			description = "Did you know: reaching 0 hitpoints will cause you to die",
			section = funSettings
	)
	default boolean aprilFools4()
	{
		return true;
	}
	//region aprilFools4
	//region aprilFools5
	@ConfigItem(
			keyName = "aprilFools5",
			name = "Consume Anglers",
			position = 5,
			description = "The average person eats 8 spiders a year. This is because Gary refuses to eat anything else, which throws off the average",
			section = funSettings
	)
	default boolean aprilFools5()
	{
		return true;
	}
	//region aprilFools1
}

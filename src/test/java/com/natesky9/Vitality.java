package com.natesky9;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class Vitality
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(VitalityPlugin.class);
		RuneLite.main(args);
	}
}
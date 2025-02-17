package com.natesky9;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.annotations.HitsplatType;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.HitsplatApplied;
import net.runelite.api.events.PlayerChanged;
import net.runelite.api.events.StatChanged;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.awt.*;

@Slf4j
@PluginDescriptor(
	name = "Vitality"
)
public class VitalityPlugin extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private VitalityConfig config;

	@Override
	protected void startUp() throws Exception
	{
		//log.info("Example started!");
	}
	@Getter @Setter
	private int health = 0;
	@Getter @Setter
	public Actor localPlayer = null;

	@Override
	protected void shutDown() throws Exception
	{
		//log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{

			health = client.getBoostedSkillLevel(Skill.HITPOINTS);
			localPlayer = client.getLocalPlayer();
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		Player player = client.getLocalPlayer();
		Skill skill = event.getSkill();
		boolean ignoreRegen = VitalityConfig.ignoreRegen();
		if (!(skill == Skill.HITPOINTS)) return;

		int last = health;
		int current = client.getBoostedSkillLevel(Skill.HITPOINTS);
		player.setOverheadText("health changed: " + String.valueOf(current-last));
		player.setOverheadCycle(120);

		if (current > last)
		{
			new Hitsplat(44,current-last,120);
		}
		System.out.println("last is: " + last);

		setHealth(current);
	}

	@Provides
	VitalityConfig provideConfig(ConfigManager configManager)
		{
		return configManager.getConfig(VitalityConfig.class);
		}
}

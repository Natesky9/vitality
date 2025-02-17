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
import net.runelite.client.ui.overlay.OverlayManager;

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
	private OverlayManager overlayManager;

	@Inject
	private VitalityOverlay vitalityOverlay;

	@Inject
	private VitalityConfig config;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(vitalityOverlay);
		//log.info("Example started!");
	}
	@Getter @Setter
	public int health = 0;
	@Getter @Setter
	public int difference = 0;
	@Getter @Setter
	public Actor localPlayer = null;
	@Getter @Setter
	public int timer;


	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(vitalityOverlay);
		//log.info("Example stopped!");
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			setHealth(client.getBoostedSkillLevel(Skill.HITPOINTS));
			setDifference(9);
			setTimer(1000);
			setLocalPlayer(client.getLocalPlayer());
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		Player player = client.getLocalPlayer();
		Skill skill = event.getSkill();
		if (!(skill == Skill.HITPOINTS)) return;

		int last = getHealth();
		int current = client.getBoostedSkillLevel(Skill.HITPOINTS);

		if (current > last)
		{
			//System.out.println("last is: " + last);
		}
		setDifference(current - last);
		setHealth(current);
		setTimer(0);
	}

	@Provides
	VitalityConfig provideConfig(ConfigManager configManager)
		{
		return configManager.getConfig(VitalityConfig.class);
		}
}

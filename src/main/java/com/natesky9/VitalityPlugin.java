package com.natesky9;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.annotations.HitsplatType;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.RuneLite;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.awt.*;
import java.awt.geom.Area;
import java.time.Clock;
import java.time.LocalDate;
import java.time.Month;
import java.util.*;

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
	}
	@Getter @Setter
	public int health = 255;
	@Getter @Setter
	public int difference = 0;
	@Getter @Setter
	public Actor localPlayer = null;
	@Getter @Setter
	public int timer;

	@Getter @Setter
	public int jokeTimer;
	@Getter @Setter
	WorldPoint tile;
	@Getter @Setter
	public Actor fool;


	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(vitalityOverlay);
	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			//System.out.println("health is: " + client.getBoostedSkillLevel(Skill.HITPOINTS));
			//setHealth(client.getBoostedSkillLevel(Skill.HITPOINTS));
			//setDifference(0);
			//setTimer(1000);
			setLocalPlayer(client.getLocalPlayer());
			setJokeTimer(0);
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		Skill skill = event.getSkill();
		if (!(skill == Skill.HITPOINTS)) return;

		int last = getHealth();
		int current = client.getBoostedSkillLevel(Skill.HITPOINTS);

		//region soulreaper edge case
		int weapon = client.getLocalPlayer().getPlayerComposition().getEquipmentId(KitType.WEAPON);
		if (weapon == 28338 && current - last == 8)//if current weapon is the Soulreaper axe
		{
			setHealth(current);
			return;
		}
		//endregion soulreaper edge case

		//region foodHealing
		if (config.excludeFood() && localPlayer.getAnimation() == AnimationID.CONSUMING)
		{
			//test if stats are changed before animation plays
			setHealth(current);
			return;
		}
		//endregion foodHealing

		setDifference(current - last);
		setHealth(current);
		setTimer(0);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (LocalDate.now().getDayOfMonth() == 1
				&& LocalDate.now().getMonth() == Month.APRIL
				&& config.aprilFools())
		{
			if (client.getLocalPlayer().getWorldLocation().getX() != getTile().getX()
			&& client.getLocalPlayer().getWorldLocation().getY() != getTile().getY())
			{
				setJokeTimer(0);
				setTile(client.getLocalPlayer().getWorldLocation());
			}

			setJokeTimer(Math.min(getJokeTimer()+1,60));
			//System.out.println(jokeTimer);
			if (getJokeTimer() >= 58)
			{
				int x = (int)((Math.random()-.5) * 256) * 8;
				int y = (int)((Math.random()-.5) * 256) * 8;
				Projectile projectile = client.getWorldView(-1).createProjectile(772,localPlayer.getWorldLocation().getPlane(),
				localPlayer.getLocalLocation().getX()+x,localPlayer.getLocalLocation().getY()+y, -500,
				client.getGameCycle(), client.getGameCycle()+60,
				-100,100,100,
				localPlayer,localPlayer.getWorldLocation().getX(),localPlayer.getWorldLocation().getY());
				//System.out.println("my cabbages!");
				client.getWorldView(-1).getProjectiles().addLast(projectile);
			}
		}
	}

	@Subscribe
	public void onPostObjectComposition(PostObjectComposition event)
	{
		//if (event.getObjectComposition().getId() == 1161)
		//{
		//	test stuff for april fools event
		//	int cabbage = event.getObjectComposition().getMapSceneId();
		//}
	}

	@Provides
	VitalityConfig provideConfig(ConfigManager configManager)
		{
		return configManager.getConfig(VitalityConfig.class);
		}
}

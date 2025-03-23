package com.natesky9;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Slf4j
@PluginDescriptor(
	name = "Vitality"
)
public class VitalityPlugin extends Plugin
{
	public Animation IDLE;
	public Animation ATTACK;
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
	public int timer = 0;

	@Getter @Setter
	public int jokeTimer = 0;
	@Getter @Setter
	WorldPoint tile;
	@Getter @Setter
	public SecretFeature secretFeature;
	@Getter @Setter
	public ArrayList<RuneLiteObject> fools;


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
			//System.out.println("initializing");
			setSecretFeature(new SecretFeature(client));
			IDLE = client.loadAnimation(AnimationID.IDLE);
			ATTACK = client.loadAnimation(AnimationID.CRAFTING_BATTLESTAVES);
			//setHealth(client.getBoostedSkillLevel(Skill.HITPOINTS));
			//setDifference(0);
			//setTimer(1000);
			setLocalPlayer(client.getLocalPlayer());
			setTile(client.getLocalPlayer().getWorldLocation());
			setFools(new ArrayList<>());
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
			//reset if player moves or has any animation
			if (client.getLocalPlayer().getWorldLocation().getX() != getTile().getX()
			|| client.getLocalPlayer().getWorldLocation().getY() != getTile().getY()
			|| getLocalPlayer().getAnimation() != -1)
			{
				setJokeTimer(0);
				setTile(client.getLocalPlayer().getWorldLocation());
			}

			setJokeTimer(getJokeTimer()+1);
			//System.out.println(jokeTimer);

			if (getJokeTimer() > 256 && getFools().size() < 5)
			{
				spawnFool();
			}

			if (!getFools().isEmpty())
			{
				spawnCabbage();
			}
		}
	}
	void spawnCabbage()
	{
		int index = (int) (Math.random()*getFools().size());
		RuneLiteObject actor = getFools().get(index);

		WorldPoint worldPoint = WorldPoint.fromLocal(client,actor.getLocation());
		boolean see = getLocalPlayer().getWorldArea().hasLineOfSightTo(client.getTopLevelWorldView(),worldPoint);
		if (!see)
			return;

		client.playSoundEffect(SoundEffectID.PICK_PLANT_BLOOP);

		int x = actor.getX();
		int y = actor.getY();

		Projectile projectile = client.getWorldView(-1).createProjectile(772,localPlayer.getWorldLocation().getPlane(),
				x,y, -500,
				client.getGameCycle(), client.getGameCycle()+60,
				-100,100,100,
				localPlayer,localPlayer.getWorldLocation().getX(),localPlayer.getWorldLocation().getY());
		//System.out.println("my cabbages!");
		client.getWorldView(-1).getProjectiles().addLast(projectile);

		//set animation
		for (RuneLiteObject object: getFools())
		{
			int angle = findAngle(object.getLocation());
			object.setOrientation(angle);
			object.setAnimation(IDLE);
		}
		actor.setAnimation(ATTACK);

		if (getLocalPlayer().getLocalLocation().distanceTo(actor.getLocation()) > 128*10)
		{//despawn mechanic
			actor.setActive(false);
			getFools().remove(actor);
		}
	}
	void spawnFool()
	{
		//System.out.println("spawning mage");
		SecretFeature fool = getSecretFeature();

		int randomx = (int) (Math.random()*21-10)*128;
		int randomy = (int) (Math.random()*21-10)*128;
		LocalPoint local = client.getLocalPlayer().getLocalLocation().plus(randomx,randomy);

		WorldPoint worldPoint = WorldPoint.fromLocal(client,local);
		boolean see = getLocalPlayer().getWorldArea().hasLineOfSightTo(client.getTopLevelWorldView(),worldPoint);
		boolean tooClose = getLocalPlayer().getLocalLocation().distanceTo(local) < 256;

		if (!see || tooClose)//if the potential location is not visible or too close
			return;

		//NPCComposition composition = client.getNpcDefinition(7310);
		//int[] modelIDS = new int[]{31794, 214, 250, 31805, 31797, 177, 31783, 181, 31911, 31889};

		RuneLiteObject r = client.createRuneLiteObject();
		r.setModel(fool.getBrassica());
		r.setAnimation(IDLE);
		r.setLocation(local,0);
		getLocalPlayer().setOverheadText("April Fools!");
		getLocalPlayer().setOverheadCycle(200);

		int angle = findAngle(r.getLocation());

		r.setOrientation(angle);
		r.setActive(true);
		getFools().add(r);
	}

	int findAngle(LocalPoint point)
	{

		int x1 = getLocalPlayer().getLocalLocation().getX();
		int y1 = getLocalPlayer().getLocalLocation().getY();
		int x2 = point.getX();
		int y2 = point.getY();
		double angle_radians = Math.atan2(y2 - y1, x2 - x1);
		double angle_degrees = (-Math.toDegrees(angle_radians) +360+90) % 360;
		//System.out.println("angle: " + angle_degrees);
		int angle = (int) (angle_degrees/360*2048);
		return angle;
	}


	@Provides
	VitalityConfig provideConfig(ConfigManager configManager)
		{
		return configManager.getConfig(VitalityConfig.class);
		}
}

package com.natesky9;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.*;
import net.runelite.api.kit.KitType;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.config.RuneLiteConfig;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.ItemClient;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDependency;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.itemstats.*;
import net.runelite.client.plugins.itemstats.stats.Stats;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.*;
import java.util.concurrent.ScheduledExecutorService;
import java.util.stream.Collectors;

import static net.runelite.api.gameval.InventoryID.*;

@Slf4j
@PluginDescriptor(
	name = "Vitality"
)
@PluginDependency(ItemStatPlugin.class)
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
	@Inject
	private SecretFeature secretFeature;
	@Inject
	private ItemStatChangesService service;
	@Inject
	private ClientThread clientThread;
	@Inject
	private ScheduledExecutorService executorService;
	@Inject
	private ItemClient itemClient;
	@Inject
	private RuneLiteConfig runeLiteConfig;
	@Inject
	public ItemManager itemManager;

	@Override
	protected void startUp() throws Exception
	{
		overlayManager.add(vitalityOverlay);
		setSecret(config.aprilFools1() && !config.aprilFools2() && config.aprilFools3()
				&& config.aprilFools4() && !config.aprilFools5());
	}
	@Getter @Setter
	public int previousHealth = 255;
	//check whether this is needed with the new system
	@Getter @Setter
	public int previousPrayer = 255;
	@Getter @Setter
	public boolean secret;
	@Getter @Setter
	public Actor localPlayer = null;

	@Getter @Setter
	public ArrayList<Hitsplat> hitsplats;
	@Getter @Setter
	public ArrayList<Hitsplat> healsplats;
	@Getter @Setter
	public ArrayList<Hitsplat> prayersplats;
	@Getter @Setter
	public ArrayList<Hitsplat> secretsplats;
	@Getter @Setter
	public Item[] items;



	@Override
	protected void shutDown() throws Exception
	{
		overlayManager.remove(vitalityOverlay);
	}
	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		System.out.println(event.getKey());
		if (event.getKey().equals("aprilFools1") && config.aprilFools2() && config.aprilFools3() && config.aprilFools4()
				&& config.aprilFools5() && Objects.equals(event.getNewValue(), "false"))
			clientThread.invoke(new ChatRunnable(client,"<col=ff0000>To disable the april fools feature, all boxes must be unchecked</col=ff0000>"));
		if (event.getKey().equals("aprilFools5") && !config.aprilFools2() && !config.aprilFools3() && !config.aprilFools4() && !config.aprilFools5())
			clientThread.invoke(new ChatRunnable(client,"Umm, actually maybe it was <col=ff0000>check all boxes except 2</col=ff0000>"));
		if (config.aprilFools1() && !config.aprilFools2() && config.aprilFools3() && config.aprilFools4()
				&& config.aprilFools5() && Objects.equals(event.getNewValue(), "true"))
			clientThread.invoke(new ChatRunnable(client,"Let me see, those two boxes are.... <col=ff0000>three and four</col=ff0000>"));
		if ((event.getKey().equals("aprilFools3") || event.getKey().equals("aprilFools4"))
				&& (!config.aprilFools3() && !config.aprilFools4()) && config.aprilFools1() && config.aprilFools2() && config.aprilFools5())
			clientThread.invoke(new ChatRunnable(client,"Oh dear, that didn't work. Maybe only <col=ff0000>check the even boxes</col=ff0000>"));
		if (!config.aprilFools1() && config.aprilFools2() && !config.aprilFools3() && config.aprilFools4()
				&& !config.aprilFools5())
			clientThread.invoke(new ChatRunnable(client,"What if you tried <col=ff0000>setting the anchor point to the left</col=ff0000>"));
		if (event.getKey().equals("anchorPoint") && config.anchorPoints().equals(VitalityConfig.AnchorPoints.LEFT) && !config.aprilFools1() && config.aprilFools2()
				&& !config.aprilFools3() && config.aprilFools4() && !config.aprilFools5())
			clientThread.invoke(new ChatRunnable(client,"Ok, this time it is definitely <col=ff0000>2 and 5 on</col=ff0000>. No doubt about it!"));
		if (((event.getKey().equals("aprilFools5") && Objects.equals(event.getNewValue(), "true"))
				|| (event.getKey().equals("aprilFools4") && Objects.equals(event.getNewValue(), "false") && config.aprilFools5()))
				&& !config.aprilFools1() && config.aprilFools2() && !config.aprilFools3() && !config.aprilFools4())
			clientThread.invoke(new ChatRunnable(client,"For the last step, try <col=ff0000>setting the SoundId to 3482</col=ff0000>"));
		if (event.getKey().equals("TickEatSound") && !config.aprilFools1() && config.aprilFools2()
				&& !config.aprilFools3() && !config.aprilFools4() && config.aprilFools5())
			clientThread.invoke(new ChatRunnable(client,"Have you tried turning <col=ff0000>two off and on</col=ff0000> again?"));
		if (event.getKey().equals("aprilFools2") && Objects.equals(event.getNewValue(), "true")
				&& !config.aprilFools1() && !config.aprilFools3() && !config.aprilFools4() && config.aprilFools5())
			clientThread.invoke(new ChatRunnable(client,"<col=ff0000>You have successfully disabled Vitality's april fools feature. Have a nice day!</col=ff0000>"));

		setSecret(config.aprilFools1() && !config.aprilFools2() && config.aprilFools3()
				&& config.aprilFools4() && !config.aprilFools5());
		if (!secret)
			secretFeature.fools.clear();
	}
	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		//only care about examine text
		if (!event.getMenuOption().equals("Examine")) return;
		if (!event.getMenuAction().equals(MenuAction.CC_OP_LOW_PRIORITY)) return;
		int item = event.getItemId();

		StringBuilder text = new StringBuilder();

		//edge case for surge pot
		switch (item)
		{
			case 30884:
			case 30881:
			case 30878:
			case 30875:
			{
				//every dose of the surge potion
				text.append("<col=ff0000>When consumed:</col>");
				text.append(" Special attack + 25");
				break;
			}
			default:
			{
				Effect effect = service.getItemStatChanges(item);
				if (effect == null) return;
				StatChange[] stats = effect.calculate(client).getStatChanges();
				text.append("<col=ff0000>When consumed:</col>");
				for (StatChange stat:stats)
				{
					text.append(" ").append(stat.getStat().getName()).append(" ");
					text.append(stat.getFormattedTheoretical());
				}
				break;
			}
		}
		client.addChatMessage(ChatMessageType.ENGINE,"Vitality",text.toString(),"");
	}
	@Subscribe
	public void onHitsplatApplied(HitsplatApplied applied)
	{
		if (!config.displayTickEat()) return;

		Hitsplat hit = applied.getHitsplat();
		if (!hit.isMine()) return;
		int amount = hit.getAmount();
		if (amount == 0) return;
		if (hit.getHitsplatType() != HitsplatID.DAMAGE_ME) return;

		int heals = 0;
		for (Hitsplat hitsplat: healsplats)
		{
			heals += hitsplat.getAmount();
		}
		int current = getPreviousHealth();
		if (current < heals)
		{
			Hitsplat hurt = new Hitsplat(HitsplatID.DAMAGE_ME,amount,client.getGameCycle()+32);
			hitsplats.add(hurt);
			int sound = config.tickEatSound();
			if (sound != -1)
				client.playSoundEffect(sound);
		}
	}
	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged changed)
	{
			setPreviousPrayer(client.getBoostedSkillLevel(Skill.PRAYER));
			setPreviousHealth(client.getBoostedSkillLevel(Skill.HITPOINTS));

		int inventory = changed.getContainerId();
		//early exit if not the player inventory
		if (inventory != INV) return;
		//find a way to initialize this
		if (items == null)
		{
			items = changed.getItemContainer().getItems();
			return;
		}

		ItemContainer container = changed.getItemContainer();

		Integer[] current = Arrays.stream(container.getItems()).map(Item::getId).toArray(Integer[]::new);
		List<Integer> modified = Arrays.stream(items).map(Item::getId).collect(Collectors.toList());
		Arrays.stream(current).forEach(modified::remove);

		//only process one heal per item, like in the case for antelope
		boolean dupeFlag = false;
		for (Integer item:modified)
		{
			Effect effect = service.getItemStatChanges(item);
			if (effect == null) continue;
			StatChange[] stats = effect.calculate(client).getStatChanges();
			for (StatChange change:stats)
			{
				int value = change.getRelative();

				if (change.getStat() == Stats.HITPOINTS)
				{
					if (dupeFlag) continue;
					System.out.println("setting hp to " + (client.getBoostedSkillLevel(Skill.HITPOINTS)));
					setPreviousHealth(client.getBoostedSkillLevel(Skill.HITPOINTS));
					Hitsplat fresh = new Hitsplat(HitsplatID.HEAL,value,client.getGameCycle()+32);
					if (!config.excludeFood())
						healsplats.add(fresh);
					//double check this triggers after stat changes
					dupeFlag = true;
				}
				if (change.getStat() == Stats.PRAYER)
				{
					//add to prayer splat
					Hitsplat fresh = new Hitsplat(HitsplatID.CYAN_UP,value,client.getGameCycle()+32);
					if (!config.excludeDrink())
						prayersplats.add(fresh);
				}
			}
		}

		//finish by setting the snapshot
		items = Arrays.stream(container.getItems()).filter(check -> check.getId() != -1).toArray(Item[]::new);
	}
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			setLocalPlayer(client.getLocalPlayer());
			setPreviousHealth(client.getBoostedSkillLevel(Skill.HITPOINTS));
			setPreviousPrayer(client.getBoostedSkillLevel(Skill.PRAYER));
			setHitsplats(new ArrayList<>());
			setHealsplats(new ArrayList<>());
			setPrayersplats(new ArrayList<>());
			setSecretsplats(new ArrayList<>());

			secretFeature.gameStateChanged();
		}
	}

	@Subscribe
	public void onStatChanged(StatChanged event)
	{
		Skill skill = event.getSkill();

		//because statChanged happens before containerChanged
		//we need to filter out eating
		if (localPlayer.getAnimation() == AnimationID.CONSUMING)
		{
			setPreviousPrayer(client.getBoostedSkillLevel(Skill.PRAYER));
			setPreviousHealth(client.getBoostedSkillLevel(Skill.HITPOINTS));
			return;
		}

		if (skill == Skill.PRAYER)
		{
			int currentPrayer = client.getBoostedSkillLevel(Skill.PRAYER);
			int difference = currentPrayer - getPreviousPrayer();

			boolean regen = difference == 1;
			if (regen && config.ignoreRegen())
			{
				setPreviousPrayer(currentPrayer);
				return;
			}

			//if (config.excludeFood() && localPlayer.getAnimation() == AnimationID.CONSUMING)
			//{
			//	//test if stats are changed before animation plays
			//	//depreciated, using containerChanged now
			//	setPreviousPrayer(currentPrayer);
			//	return;
			//}
			//add healsplat
			if (currentPrayer > getPreviousPrayer())
			{
				Hitsplat pray = new Hitsplat(HitsplatID.HEAL,currentPrayer-getPreviousPrayer(),client.getGameCycle()+40);
				prayersplats.add(pray);
			}
			setPreviousPrayer(currentPrayer);
			//prayer drain mechanics, blacklist netzipoli
			//as it is the only place in the game that does it
		}
		if (skill == Skill.HITPOINTS)
		{
			int currentHealth = client.getBoostedSkillLevel(Skill.HITPOINTS);

			//region regen config

			int gloves = client.getLocalPlayer().getPlayerComposition().getEquipmentId(KitType.HANDS);
			int difference = currentHealth - getPreviousHealth();
			boolean regen = (difference == 1) || (difference == 2 && gloves == 11133);
			if (regen && config.ignoreRegen())
			{
				setPreviousHealth(currentHealth);
				return;
			}
			//endregion regen config

			//region soulreaper edge case
			int weapon = client.getLocalPlayer().getPlayerComposition().getEquipmentId(KitType.WEAPON);
			boolean isSoulreaper = weapon == 28338;
			if (weapon == 28338 && (currentHealth - getPreviousHealth()) % 8 == 0
					&& localPlayer.getAnimation() != AnimationID.CONSUMING)
			{
				//if currentHealth weapon is the Soulreaper axe
				setPreviousHealth(currentHealth);
				return;
			}
			//endregion soulreaper edge case

			//region foodHealing
			//depreciated, moved to containerChanged
			//if (config.excludeFood() && localPlayer.getAnimation() == AnimationID.CONSUMING)
			//{
			//	//test if stats are changed before animation plays
			//	setPreviousHealth(currentHealth);
			//	return;
			//}
			//endregion foodHealing

			//add healsplat
			if (currentHealth > getPreviousHealth())
			{
				System.out.println("processing passive heal");
				//add a blacklist for existing heals
				//so that there isn't double heals
				//edit: not needed since we update the hp
				Hitsplat heal = new Hitsplat(HitsplatID.HEAL,currentHealth-getPreviousHealth(),client.getGameCycle()+40);

				if (!healsplats.isEmpty())
				{
					for (Hitsplat existing:healsplats)
					{
						healsplats.set(healsplats.indexOf(existing),new Hitsplat(existing.getHitsplatType(),
								existing.getAmount(),client.getGameCycle()+40));
					}
				}
				healsplats.add(heal);
			}
			setPreviousHealth(currentHealth);
		}
	}

	@Subscribe
	public void onClientTick(ClientTick event)
	{
		secretFeature.tick();
	}
	@Subscribe
	public void onGameTick(GameTick event)
	{
		//region debug code, comment out when done
		if (false)//client.getGameCycle() % 40 < 10)
		{
			//Hitsplat heal1 = new Hitsplat(HitsplatID.HEAL,10,client.getGameCycle()+32);
			//healsplats.add(heal1);
			//Hitsplat heal2 = new Hitsplat(HitsplatID.HEAL,20,client.getGameCycle()+32);
			//healsplats.add(heal2);
			//Hitsplat heal3 = new Hitsplat(HitsplatID.HEAL,30,client.getGameCycle()+32);
			//healsplats.add(heal3);
			//Hitsplat heal4 = new Hitsplat(HitsplatID.HEAL,40,client.getGameCycle()+32);
			//prayersplats.add(heal4);
			//Hitsplat heal5 = new Hitsplat(HitsplatID.HEAL,90,client.getGameCycle()+32);
			//prayersplats.add(heal5);
			//Hitsplat heal6 = new Hitsplat(HitsplatID.HEAL,4,client.getGameCycle()+32);
			//healsplats.add(heal6);
		}
		//endregion debug code

		//region clear expired hitsplats
		if (!hitsplats.isEmpty())
			for (int i = hitsplats.size()-1;i>=0;i--)
			{
				Hitsplat hurt = hitsplats.get(i);
				if (hurt.getDisappearsOnGameCycle() < client.getGameCycle())
				{
					hitsplats.remove(hurt);
				}
			}
		//
		if (!secretsplats.isEmpty())
			for (int i = secretsplats.size()-1;i>=0;i--)
			{
				Hitsplat hit = secretsplats.get(i);
				if (hit.getDisappearsOnGameCycle() < client.getGameCycle())
				{
					secretsplats.remove(hit);
				}
			}
		//
		if (!healsplats.isEmpty())
			for (int i = healsplats.size()-1;i>=0;i--)
			{
				Hitsplat hit = healsplats.get(i);
				if (hit.getDisappearsOnGameCycle() < client.getGameCycle())
				{
					healsplats.remove(hit);
				}
			}
		if (!prayersplats.isEmpty())
			for (int i = prayersplats.size()-1;i>=0;i--)
			{
				Hitsplat pray = prayersplats.get(i);
				if (pray.getDisappearsOnGameCycle() < client.getGameCycle())
				{
					prayersplats.remove(pray);
				}
			}
		//endregion clear expired hitsplats
		secretFeature.gameTick();
	}


	@Provides
	VitalityConfig provideConfig(ConfigManager configManager)
		{
		return configManager.getConfig(VitalityConfig.class);
		}
}

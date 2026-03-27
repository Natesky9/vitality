package com.natesky9;

import com.natesky9.Hitsplats.SecretSplat;
import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;

import javax.inject.Inject;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Collections;

public class SecretFeature {
    @Inject
    private Client client;
    @Inject
    private VitalityPlugin plugin;
    @Inject
    VitalityConfig config;

    private Animation IDLE;
    private Animation ATTACK;
    private Model model;

    @Getter @Setter
    public ArrayList<RuneLiteObject> fools;
    @Getter @Setter
    public int jokeTimer = 0;
    @Getter @Setter
    WorldPoint jokeTile;
    @Getter @Setter
    public RuneLiteObject lastMage;

    public void gameStateChanged()
    {
        IDLE = client.loadAnimation(AnimationID.IDLE);
        ATTACK = client.loadAnimation(AnimationID.CRAFTING_BATTLESTAVES);
        ModelData[] data = new ModelData[]
                {
                        client.loadModelData(31794),
                        client.loadModelData(214),
                        client.loadModelData(250),
                        client.loadModelData(31805),
                        client.loadModelData(31797),
                        client.loadModelData(177),
                        client.loadModelData(31783),
                        client.loadModelData(181),
                        client.loadModelData(31911),
                        client.loadModelData(31889),
                };
        ModelData modelData = client.mergeModels(data);
        model = modelData.light();
        setFools(new ArrayList<>());
        setJokeTile(client.getLocalPlayer().getWorldLocation());
        setJokeTimer(0);
    }
    public void tick()
    {
        //client might not exist yet
        if (client.getLocalPlayer() == null) return;
        for (RuneLiteObject brassica:fools)
        {
            int angle = findAngle(brassica.getLocation());
            brassica.setOrientation(angle);
        }
    }
    public void gameTick()
    {
        setJokeTile(client.getLocalPlayer().getWorldLocation());
        if (!plugin.secret && LocalDate.now().getDayOfMonth() == 1 && LocalDate.now().getMonth() == Month.APRIL)
        {
            //reset if player moves or has any animation
            if (client.getLocalPlayer().getWorldLocation().getX() != getJokeTile().getX()
                    || client.getLocalPlayer().getWorldLocation().getY() != getJokeTile().getY()
                    || client.getLocalPlayer().getAnimation() != -1)
            {
                setJokeTimer(0);
                setJokeTile(client.getLocalPlayer().getWorldLocation());
                //setLastMage(null);
            }
            setJokeTimer(getJokeTimer()+1);

            if (getJokeTimer() > 1 && getFools().size() < 5)
            {
                RuneLiteObject mage = spawnFool();
                if (mage != null)
                {
                    setLastMage(mage);
                }
            }

            Collections.shuffle(fools);
            spawnCabbage();
        }
        setJokeTile(client.getLocalPlayer().getWorldLocation());
    }
    RuneLiteObject spawnFool()
    {
        if (fools.isEmpty())
            client.addChatMessage(ChatMessageType.GAMEMESSAGE,"vitality","<col=42F527>April Fools! To disable brassica mages, " +
                "go to the settings of the plugin</col=42F527> <col=ff0000>Vitality</col=ff0000>","");
        int randomx = (int) (Math.random()*21-10)*128;
        int randomy = (int) (Math.random()*21-10)*128;
        LocalPoint local = client.getLocalPlayer().getLocalLocation().plus(randomx,randomy);

        WorldPoint worldPoint = WorldPoint.fromLocal(client,local);
        boolean see = client.getLocalPlayer().getWorldArea().hasLineOfSightTo(client.getTopLevelWorldView(),worldPoint);
        boolean tooClose = client.getLocalPlayer().getLocalLocation().distanceTo(local) < 256;

        //{31794, 214, 250, 31805, 31797, 177, 31783, 181, 31911, 31889};

        RuneLiteObject mage = client.createRuneLiteObject();
        mage.setModel(getBrassica());
        mage.setAnimation(IDLE);
        mage.setLocation(local,0);

        mage.setActive(true);
        getFools().add(mage);
        return mage;
    }
    void spawnCabbage()
    {
        RuneLiteObject actor = null;
        RuneLiteObject test;
        //pick a mage that hasn't attacked last
        for (int i=0;i<fools.size();i++)
        {
            test = fools.get(i);
            test.setStartCycle(test.getStartCycle()+1);
            if (test == lastMage)
            {
                continue;
            }
            if (!canSee(test))
            {
                if (test.getStartCycle() > 20)
                    despawnCabbage(test);
                continue;
            }
            actor = fools.get(i);
        }
        if (actor == null)
        {
            return;
        }

        client.playSoundEffect(SoundEffectID.PICK_PLANT_BLOOP);

        int x = actor.getX();
        int y = actor.getY();
        Projectile projectile = client.getWorldView(WorldView.TOPLEVEL).createProjectile(772,client.getLocalPlayer().getWorldLocation().getPlane(),
                x,y, 120,
                client.getGameCycle(), client.getGameCycle()+40,
                0,0,0,
                client.getLocalPlayer(), client.getLocalPlayer().getWorldLocation().getX(),client.getLocalPlayer().getWorldLocation().getY());
        //My cabbages!
        client.getProjectiles().addLast(projectile);
        if (getLastMage() != null && canSee(getLastMage()))
        {
            int value = (int) (Math.random()*50)+10;
            SecretSplat fresh = new SecretSplat(value,client.getGameCycle()+80);
            plugin.secretsplats.add(fresh);
        }

        //set animation
        for (RuneLiteObject object: getFools())
        {
            object.setAnimation(IDLE);
        }
        actor.setAnimation(ATTACK);

        if (client.getLocalPlayer().getLocalLocation().distanceTo(actor.getLocation()) > 128*11)
        {//despawn mechanic
            despawnCabbage(actor);
        }
        //finally, set the last actor
        setLastMage(actor);
    }
    void despawnCabbage(RuneLiteObject actor)
    {
        client.removeRuneLiteObject(actor);
        //actor.setActive(false);
        getFools().remove(actor);
    }
    boolean canSee(RuneLiteObject actor)
    {
        WorldPoint worldPoint = WorldPoint.fromLocal(client,actor.getLocation());
        return client.getLocalPlayer().getWorldArea().hasLineOfSightTo(client.getTopLevelWorldView(),worldPoint);
    }
    public Model getBrassica()
    {
        return model;
    }
    int findAngle(LocalPoint point)
    {

        int x1 = client.getLocalPlayer().getLocalLocation().getX();
        int y1 = client.getLocalPlayer().getLocalLocation().getY();
        int x2 = point.getX();
        int y2 = point.getY();
        double angle_radians = Math.atan2(y2 - y1, x2 - x1);
        double angle_degrees = (-Math.toDegrees(angle_radians) +360+90) % 360;
        return (int) (angle_degrees/360*2048);
    }
}

package com.natesky9;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.time.Month;

public class VitalityOverlay extends Overlay {
    private static final Class<?> PLUGIN_CLASS = VitalityPlugin.class;
    public static final ImageIcon HEALSPLAT = new ImageIcon(ImageUtil.loadImageResource(PLUGIN_CLASS,
            "/heal.png"));
    public static final ImageIcon DODGE = new ImageIcon(ImageUtil.loadImageResource(PLUGIN_CLASS,
            "/dodge.png"));
    public static final ImageIcon APRIL = new ImageIcon(ImageUtil.loadImageResource(PLUGIN_CLASS,
            "/cabbage.png"));

    @Inject
    private VitalityPlugin plugin;
    @Inject
    private VitalityConfig config;
    @Inject
    private Client client;

    @Override
    public Dimension render(Graphics2D graphics) {
        setLayer(OverlayLayer.ABOVE_SCENE);
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);
        Actor actor = plugin.getLocalPlayer();

        LocalPoint location = actor.getLocalLocation();

        if (LocalDate.now().getDayOfMonth() == 1
                && LocalDate.now().getMonth() == Month.APRIL
                && config.aprilFools() && !plugin.fools.isEmpty())
        {
            Hitsplat joke = new Hitsplat(HitsplatID.DAMAGE_ME_POISE,
                    (int) (Math.random()*99),client.getGameCycle()+16);
            BufferedImage fool = drawHitsplat(joke,APRIL);
            int offset = getAnchorPoint(actor);
            Point point = Perspective.getCanvasImageLocation(client,location,fool,offset);
            int x = (((client.getGameCycle() % 120) / 30)-1) % 2;
            x *= 16;
            int y = ((((client.getGameCycle()+30) % 120) / 30)-1) % 2;
            y *= 16;
            if (client.getGameCycle() % 30 < 15)
                OverlayUtil.renderImageLocation(graphics, new Point(point.getX()-x-4, point.getY()-y),fool);
        }


        if (!plugin.healsplats.isEmpty())
        {
            for (int i=0;i<plugin.healsplats.size();i++)
            {
                Hitsplat heal = plugin.healsplats.get(i);
                BufferedImage image = drawHitsplat(heal,HEALSPLAT);
                int offset = getAnchorPoint(actor);
                Point point = actor.getCanvasImageLocation(image,actor.getLogicalHeight());
                int x = 0;
                if (i == 1) x=20;
                if (i == 2) x=-20;
                int y = i > 0 ? 20:0;
                if (config.healRise())
                    y += (heal.getDisappearsOnGameCycle()-client.getGameCycle())/10;
                //shifting the offset to the last possible point in the stack should fix the visual bug?
                y-= offset;

                Point canvas = new Point(point.getX()-4-x, point.getY()-y);
                OverlayUtil.renderImageLocation(graphics, canvas,image);
            }
        }
        if (!plugin.hitsplats.isEmpty())
        {
            //int damage = 0;
            //for (Hitsplat hitsplat: plugin.healsplats)
            //{
            //    damage += hitsplat.getAmount();
            //}
            //if (client.getBoostedSkillLevel(Skill.HITPOINTS) < damage)
            //{
            Hitsplat dodge = plugin.hitsplats.get(0);
            BufferedImage image = drawHitsplat(dodge,DODGE);
            int offset = getAnchorPoint(actor);
            Point point = actor.getCanvasImageLocation(image, actor.getLogicalHeight()-offset);
            int x = dodge.getDisappearsOnGameCycle() -client.getGameCycle();
            Point canvas = new Point(point.getX()+16-x,point.getY());
            OverlayUtil.renderImageLocation(graphics,canvas,image);
            //}
        }
        return null;
    }
    enum cardinal
    {
        north,
        east,
        south,
        west
    }
    public int getAnchorPoint(Actor actor)
    {
        switch (config.anchorPoints())
        {
            case HEAD: return 0;
            case CHEST: return actor.getLogicalHeight()/5;
            default:return actor.getLogicalHeight();
        }
    }
    private BufferedImage drawHitsplat(Hitsplat hitsplat, ImageIcon imageIcon)
    {
        BufferedImage bi = iconToBuffered(hitsplat, imageIcon);
        Graphics g = bi.getGraphics();
        bi = drawCenteredDamageNumbers(g, hitsplat, bi);
        g.dispose();
        return bi;
    }
    public BufferedImage drawCenteredDamageNumbers(Graphics g, Hitsplat hitsplat, BufferedImage bi)
    {
        String text = String.valueOf(hitsplat.getAmount());
        int value = hitsplat.getAmount();
        Font font = FontManager.getRunescapeSmallFont();
        if (config.healScaling())
                font = FontManager.getRunescapeSmallFont()
                        .deriveFont(16f + value/8f);
        FontMetrics metrics = g.getFontMetrics(font);
        int x = (bi.getWidth() - metrics.stringWidth(text)) /2;
        int y = ((bi.getHeight() - metrics.getHeight())/2) + metrics.getAscent();
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString(text,x+1,y+1);
        g.setColor(Color.WHITE);
        g.drawString(text,x,y);
        return bi;
    }
    private BufferedImage iconToBuffered(Hitsplat hitsplat, ImageIcon icon)
    {
        int value = hitsplat.getAmount();
        Image image = icon.getImage();
        int height = icon.getIconHeight();
        int width = icon.getIconWidth();
        int scale = 1;
        if (config.healScaling())
            scale = 1+(int) (value*.02);

        Image tempImage;

        if (icon == APRIL)
            tempImage = image.getScaledInstance(width/3, height/3, Image.SCALE_SMOOTH);
        else
            tempImage = image.getScaledInstance(width*scale,height*scale,Image.SCALE_SMOOTH);
        ImageIcon sizedImageIcon = new ImageIcon(tempImage);

        BufferedImage bi = new BufferedImage(
                sizedImageIcon.getIconWidth(),
                sizedImageIcon.getIconHeight(),
                BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        sizedImageIcon.paintIcon(null, g, 0, 0);
        g.dispose();
        return bi;
    }
}

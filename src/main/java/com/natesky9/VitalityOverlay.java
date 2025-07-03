package com.natesky9;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;
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
        Actor actor = plugin.getLocalPlayer();

        LocalPoint playerLocation = actor.getLocalLocation();
        LocalPoint location = new LocalPoint(playerLocation.getX(),playerLocation.getY());

        //redundant check
        //if (actor == null) return null;

        if (LocalDate.now().getDayOfMonth() == 1
                && LocalDate.now().getMonth() == Month.APRIL
                && config.aprilFools() && !plugin.fools.isEmpty())
        {
            BufferedImage fool = drawJokeHitsplat();
            int offset = getAnchorPoint(actor);
            Point point = Perspective.getCanvasImageLocation(client,location,fool,offset);
            int x = (((client.getGameCycle() % 120) / 30)-1) % 2;
            x *= 16;
            int y = ((((client.getGameCycle()+30) % 120) / 30)-1) % 2;
            y *= 16;
            if (client.getGameCycle() % 30 < 15)
                OverlayUtil.renderImageLocation(graphics, new Point(point.getX()-x-4, point.getY()-y),fool);
        }

        int value = config.ignoreRegen() ? 1:0;

        if (plugin.getDifference() <= value) return null;
        if (plugin.getTimer() > 100) return null;
        plugin.setTimer(plugin.getTimer()+1);

        BufferedImage image = drawHitsplat(plugin.getDifference());
        int offset = getAnchorPoint(actor);
        //Point point = Perspective.getCanvasImageLocation(client,location,image,offset);
        //using a better perspective point
        Point point = actor.getCanvasImageLocation(image,actor.getLogicalHeight()-offset);
        int rise = 0;
        if (config.healRise())
            rise = (plugin.getTimer() /20);
        //not needed anymore?
        //rise -= 2;//offset it so it's not in the crotch
        //Point p = new Point(point.getX(), point.getY());
        OverlayUtil.renderImageLocation(graphics, new Point(point.getX()-4, point.getY()-rise),image);

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
    private BufferedImage drawHitsplat(int damage)
    {
        BufferedImage bi = iconToBuffered(HEALSPLAT);
        Graphics g = bi.getGraphics();
        bi = drawCenteredDamageNumbers(g, String.valueOf(damage), bi);
        g.dispose();
        return bi;
    }
    private BufferedImage drawJokeHitsplat()
    {
        BufferedImage bi = iconToBuffered(APRIL);
        Graphics g = bi.getGraphics();
        bi = drawCenteredDamageNumbers(g, "OOF", bi);
        g.dispose();
        return bi;
    }
    public BufferedImage drawCenteredDamageNumbers(Graphics g, String text, BufferedImage bi)
    {
        Font font = FontManager.getRunescapeSmallFont();
        if (config.healScaling())
                font = FontManager.getRunescapeSmallFont()
                        .deriveFont(16f + plugin.getDifference()/8f);
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
    private BufferedImage iconToBuffered(ImageIcon icon)
    {
        Image image = icon.getImage();
        int height = icon.getIconHeight();
        int width = icon.getIconWidth();
        int scale = 1;
        if (config.healScaling())
            scale = 1+(int) (plugin.difference*.02);

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

package com.natesky9;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.client.RuneLite;
import net.runelite.client.game.AlternateSprites;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class VitalityOverlay extends Overlay {
    private static final Class<?> PLUGIN_CLASS = VitalityPlugin.class;
    public static final ImageIcon HEALSPLAT = new ImageIcon(ImageUtil.loadImageResource(PLUGIN_CLASS,
            "/heal.png"));

    @Inject
    private VitalityPlugin plugin;
    @Inject
    private VitalityConfig config;
    @Inject
    private Client client;

    @Override
    public Dimension render(Graphics2D graphics) {
        Actor actor = plugin.getLocalPlayer();
        if (actor == null) return null;

        int value = config.ignoreRegen() ? 1:0;

        if (plugin.getDifference() <= value) return null;
        if (plugin.getTimer() > 100) return null;
        plugin.setTimer(plugin.getTimer()+1);

        BufferedImage image = drawHitsplat(plugin.getDifference());
        Point cPoint = actor.getCanvasImageLocation(image, config.zOffset());
        int rise = 0;
        if (config.healRise())
            rise = (plugin.getTimer() /20);
        rise -= 2;//offset it so it's not in the crotch
        Point p = new Point(cPoint.getX(), cPoint.getY());
        OverlayUtil.renderImageLocation(graphics, new Point(p.getX()-4, p.getY()-rise),image);

        return null;
    }
    private BufferedImage drawHitsplat(int damage)
    {
        BufferedImage bi = iconToBuffered(HEALSPLAT);
        Graphics g = bi.getGraphics();
        bi = drawCenteredDamageNumbers(g, String.valueOf(damage), bi);
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
        Image tempImage = image.getScaledInstance(width*scale,height*scale,Image.SCALE_SMOOTH);
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

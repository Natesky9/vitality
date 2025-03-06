package com.natesky9;

import net.runelite.api.*;
import net.runelite.api.Point;
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
    
        int value = config.ignoreRegen() ? 1 : 0;
    
        if (plugin.getDifference() <= value) return null;
        if (plugin.getTimer() > 100) return null;
        plugin.setTimer(plugin.getTimer() + 1);
    
        BufferedImage image = drawHitsplat(plugin.getDifference());
    
        Polygon poly = actor.getCanvasTilePoly();
        if (poly == null) return null;
        
        Rectangle bounds = poly.getBounds();
        int centerX = bounds.x + bounds.width / 2;
        int centerY = bounds.y + bounds.height / 2;
        
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        int centeredX = centerX - (imageWidth / 2);
        int centeredY = centerY - (imageHeight / 2);
        
        int rise = 0;
        if (config.healRise()) {
            rise = (plugin.getTimer() / 20);
        }
        rise -= 2; // Offset to avoid crotch-level positioning
        
        OverlayUtil.renderImageLocation(graphics, new Point(centeredX - 4, (centeredY - rise) + (config.yOffset() - 50)), image);
    
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

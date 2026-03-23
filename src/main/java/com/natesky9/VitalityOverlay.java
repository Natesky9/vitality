package com.natesky9;

import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.client.ui.FontManager;
import net.runelite.client.ui.overlay.*;
import net.runelite.client.util.ImageUtil;

import javax.inject.Inject;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;


public class VitalityOverlay extends Overlay {
    private static final Class<?> PLUGIN_CLASS = VitalityPlugin.class;
    public static final ImageIcon HEALSPLAT = new ImageIcon(ImageUtil.loadImageResource(PLUGIN_CLASS,
            "/heal.png"));
    public static final ImageIcon DODGE = new ImageIcon(ImageUtil.loadImageResource(PLUGIN_CLASS,
            "/dodge.png"));
    public static final ImageIcon APRIL = new ImageIcon(ImageUtil.loadImageResource(PLUGIN_CLASS,
            "/cabbage.png"));
    public static final ImageIcon PRAYER = new ImageIcon(ImageUtil.loadImageResource(PLUGIN_CLASS,
            "/prayer.png"));

    @Inject
    private VitalityPlugin plugin;
    @Inject
    private VitalityConfig config;
    @Inject
    private Client client;

    @Override
    public Dimension render(Graphics2D graphics) {
        setLayer(OverlayLayer.UNDER_WIDGETS);
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);
        Actor actor = plugin.getLocalPlayer();

        if (!plugin.healsplats.isEmpty())
        {
            for (int i=0;i<plugin.healsplats.size();i++)
            {
                //instead of drawing from the head down
                //we draw from the feet up now
                Hitsplat heal = plugin.healsplats.get(i);
                BufferedImage image = drawHitsplat(heal,HEALSPLAT);
                Point point = actor.getCanvasImageLocation(image, actor.getLogicalHeight()/2);
                Point canvas = new Point(point.getX()+xOffset(i)-6,point.getY()+yOffset(i));
                OverlayUtil.renderImageLocation(graphics, canvas,image);
            }
        }
        if (!plugin.hitsplats.isEmpty())
        {
            Hitsplat dodge = plugin.hitsplats.get(0);
            BufferedImage image = drawHitsplat(dodge,DODGE);
            Point point = Perspective.localToCanvas(client,location,client.getPlane(),64+32);
            int x = dodge.getDisappearsOnGameCycle() -client.getGameCycle();
            Point canvas = new Point(point.getX()+16-x,point.getY()-37);
            OverlayUtil.renderImageLocation(graphics,canvas,image);
            //}
        }
        if (!plugin.prayersplats.isEmpty())
        {
            Hitsplat prayer = plugin.prayersplats.get(0);
            BufferedImage image = drawPrayersplat(prayer,PRAYER);

            //don't know why this doesn't line up right
            Point point = actor.getCanvasImageLocation(image, actor.getLogicalHeight()/2);
            Point canvas = new Point(point.getX()-6,point.getY()-60);
            //draw the single prayer splat above the top hitsplat
            OverlayUtil.renderImageLocation(graphics,canvas,image);
            //}
        }
        if (!plugin.secretsplats.isEmpty())
        {
            for (int i=0;i<plugin.secretsplats.size();i++)
            {
                Hitsplat heal = plugin.secretsplats.get(i);
                BufferedImage image = drawHitsplat(heal,APRIL);
                Point point = actor.getCanvasImageLocation(image, actor.getLogicalHeight()/2);
                Point canvas = new Point(point.getX()+xOffset(i)-6,point.getY()+yOffset(i)-40);
                OverlayUtil.renderImageLocation(graphics, canvas,image);
            }
        }
        return null;
    }
    public int xOffset(int index)
    {
        switch (index)
        {
            case 0:
            case 5:
                return 0;
            case 1:
            case 3:
            case 6:
                return -20;
            case 2:
            case 4:
            case 7:
                return 20;
        }
        return 0;
    }
    public int yOffset(int index)
    {
        switch (index)
        {
            case 1:
            case 2:
                return 25;
            case 0:
                return 40;
            case 3:
            case 4:
                return 55;
            case 5:
                return 70;
            case 6:
            case 7:
                return 85;
        }
        return 10;
    }
    //depreciated
    //public int getAnchorPointX()
    //{
    //    switch (config.anchorPoints())
    //    {
    //        case LEFT: return -1;
    //        case RIGHT: return 1;
    //        default: return 0;
    //    }
    //}
    //depreciated
    //public int getAnchorPointY()
    //{
    //    switch (config.anchorPoints())
    //    {
    //        case ABOVE: return -1;
    //        case BELOW: return 1;
    //        default: return 0;
    //    }
    //}
    private BufferedImage drawHitsplat(Hitsplat hitsplat, ImageIcon imageIcon)
    {
        BufferedImage bi = iconToBuffered(hitsplat, imageIcon);
        Graphics g = bi.getGraphics();
        bi = drawCenteredDamageNumbers(g, hitsplat, bi, false);
        g.dispose();
        return bi;
    }
    private BufferedImage drawPrayersplat(Hitsplat hitsplat, ImageIcon imageIcon)
    {
        BufferedImage bi = prayerToBuffered(hitsplat, imageIcon);
        Graphics g = bi.getGraphics();
        bi = drawCenteredDamageNumbers(g, hitsplat, bi, true);
        g.dispose();
        return bi;
    }
    public BufferedImage drawCenteredDamageNumbers(Graphics g, Hitsplat hitsplat, BufferedImage bi, boolean inverted)
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

        //inverted flag for black text on white or white text on black
        g.setColor(!inverted ? Color.BLACK:Color.GRAY);
        //if (!inverted)
            g.drawString(text,x+1,y+1);
        g.setColor(!inverted ? Color.WHITE:Color.BLACK);
        g.drawString(text,x,y);
        return bi;
    }
    private BufferedImage prayerToBuffered(Hitsplat hitsplat, ImageIcon icon)
    {
        Image image = icon.getImage();
        int height = icon.getIconHeight();
        int width = icon.getIconWidth();
        Image tempImage;

        tempImage = image.getScaledInstance(width,height,Image.SCALE_SMOOTH);
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

        //if (icon == APRIL)
        //    tempImage = image.getScaledInstance(width/3, height/3, Image.SCALE_SMOOTH);
        //else
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

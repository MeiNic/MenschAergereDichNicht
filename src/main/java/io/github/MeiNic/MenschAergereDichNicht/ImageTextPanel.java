package io.github.MeiNic.MenschAergereDichNicht;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageTextPanel extends JPanel {
    private BufferedImage defaultImg;
    private String text;
    private Font font;
    private Color fontColor;

    public ImageTextPanel(String imgName, String textNew, Font fontNew, Color fontColorNew) {
        setImage(imgName);
        text = textNew;
        font = fontNew;
        fontColor = fontColorNew;
    }

    public ImageTextPanel(String imgName, String textNew) {
        setImage(imgName);
        text = textNew;
    }

    public void setText(String text) {
        this.text = text;
        repaint();
    }

    public void setImage(String newImgName){
        try {
            defaultImg = ImageIO.read(new File("res/" + newImgName + ".png"));
        } catch (IOException e){
            e.printStackTrace();
        }
        repaint();
    }

    @Override
    public void setForeground(Color fg) {
        fontColor = fg;
        repaint();
    }

    @Override
    public void setFont(Font font) {
        this.font = font;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(defaultImg, 0, 0, this);

        //calculations for center placement of text
        int CenterX = getWidth() / 2;
        int CenterY = getHeight() / 2;
        FontMetrics fm = g.getFontMetrics();
        int textWidth = fm.stringWidth(text);
        textWidth += textWidth / 2;
        int textHeight = fm.getHeight();
        int x = CenterX - textWidth / 2;
        int y = CenterY + textHeight / 3;

        g.setColor(fontColor);
        g.setFont(font);
        g.drawString(text, x, y);
    }
}

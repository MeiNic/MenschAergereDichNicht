// This file is part of MenschAergereDichNicht.
// Copyright (C) 2024-2025 MeiNic, TastingComb and contributors.

// This program is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.

// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.

// You should have received a copy of the GNU General Public License
// along with this program.  If not, see <https://www.gnu.org/licenses/>.

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
            defaultImg = ImageIO.read(new File("src/resources/images/" + newImgName + ".png"));
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

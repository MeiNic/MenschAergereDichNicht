// This file is part of MenschAergereDichNicht.
// Copyright (C) 2023-2025 MeiNic, TastingComb and contributors.

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

package io.github.MeiNic.MenschAergereDichNicht.gui;

import io.github.MeiNic.MenschAergereDichNicht.Main;
import io.github.MeiNic.MenschAergereDichNicht.logger.Logger;
import io.github.MeiNic.MenschAergereDichNicht.logger.LoggerFactory;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class WinWindow {
    private final JFrame frame;
    private final JLabel message;
    private final ImageTextPanel close;
    private final ImageTextPanel nextGame;
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.decode("#f3f5f9");
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.decode("#6c6f85");
    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    public WinWindow(String player) {
        frame = new JFrame();
        message = new JLabel();
        close = new ImageTextPanel("button-idle", "close");
        nextGame = new ImageTextPanel("button-idle", "new game");

        // configure the message
        message.setText(
                "<html><body>Congratulations! Player "
                        + player
                        + " <br> has won this round.</body></html>");
        message.setBounds(10, 10, 295, 32);
        message.setFont(Theme.SEMI_BOLD);
        message.setForeground(DEFAULT_FOREGROUND_COLOR);
        message.setBackground(DEFAULT_BACKGROUND_COLOR);

        // configure close button
        close.setBounds(140, 50, 100, 32);
        close.setFont(Theme.SEMI_BOLD);
        close.setForeground(DEFAULT_FOREGROUND_COLOR);
        close.setBackground(DEFAULT_BACKGROUND_COLOR);
        close.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        LOGGER.info("Closing game.");
                        frame.setVisible(false);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        close.setImage("button-hovered");
                        frame.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        close.setImage("button-idle");
                        frame.repaint();
                    }
                });

        // configure nextGame button
        nextGame.setBounds(20, 50, 100, 32);
        nextGame.setFont(Theme.SEMI_BOLD);
        nextGame.setForeground(DEFAULT_FOREGROUND_COLOR);
        nextGame.setBackground(DEFAULT_BACKGROUND_COLOR);
        nextGame.addMouseListener(
                new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        LOGGER.info("Closing WinWindow.");
                        frame.setVisible(false);
                        LOGGER.info("Starting new game.");
                        new Main();
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        nextGame.setImage("button-hovered");
                        frame.repaint();
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        nextGame.setImage("button-idle");
                        frame.repaint();
                    }
                });

        // add elements to frame
        frame.add(message);
        frame.add(close);
        frame.add(nextGame);

        // set JFrame values
        frame.setTitle("congratulations");
        frame.setSize(315, 150);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(null);
        frame.getContentPane().setBackground(DEFAULT_BACKGROUND_COLOR);
        frame.setResizable(true);
        frame.setVisible(true);
        LOGGER.info("Displaying WinWindow.");
    }
}

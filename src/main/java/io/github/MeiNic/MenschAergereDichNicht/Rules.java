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

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Rules extends JFrame{
    private final ImageTextPanel close;
    public final JFrame executingClass;

    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    Rules(JFrame JFrameN){
        executingClass = JFrameN;
	
        //declaration of the JLabels & the JCheckBox
        JLabel header = new JLabel("Info");
        JLabel goalHeader = new JLabel("Goal of the Game:");
        JLabel goal1 = new JLabel("The Goal is to get all of your 4 figures into your House.");
        JLabel goal2 = new JLabel("In order to get there you have to kick your opponents figures.");
        JLabel ruleHeader = new JLabel("Rules: ");
        JLabel rule1 = new JLabel("1. When a 6 is rolled, you have to move a figure out of base!");
        JLabel rule2 = new JLabel("2. you have to keep your start-field free, as long as there are figures in your Base");
        JLabel rule3 = new JLabel("<html><body> 3. If possible, you must kick another person! If you didn't your " +
                "figure, that was able to <br> kick someone, will get moved to your Base. </body></html>");
        JLabel rule4 = new JLabel("<html><body> 4. In order to get into your house you have to get the exact number, " +
                "you can't go in with <br> a number, that is to high. </body></html>");
        JLabel rule5 = new JLabel("5. You cannot jump over your figures in your House.");
        close = new ImageTextPanel("button-idle", "close");

        //position of all j-components
        header.setBounds(3, 0, 150, 35);
        goalHeader.setBounds(3, 40, 300, 35);
        ruleHeader.setBounds(3, 115, 300, 35);
        goal1.setBounds(6, 65, 700, 32);
        goal2.setBounds(6, 85, 700, 32);
        rule1.setBounds(6, 140, 700, 32);
        rule2.setBounds(6, 160, 700, 32);
        rule3.setBounds(6, 190, 900, 32);
        rule4.setBounds(6, 230, 900, 32);
        rule5.setBounds(6, 260, 700, 32);
        close.setBounds(6, 300, 100, 32);

        //Set Font
        header.setFont(Theme.EXTRA_BOLD);
        goalHeader.setFont(Theme.BOLD);
        ruleHeader.setFont(Theme.BOLD);

        //Button action for close
        close.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                closeWindow();
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                close.setImage("button-hovered");
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                close.setImage("button-idle");
                repaint();
            }
        });

        //Add UI elements
        add(header);
        add(goalHeader);
        add(goal1);
        add(goal2);
        add(ruleHeader);
        add(rule1);
        add(rule2);
        add(rule3);
        add(rule4);
        add(rule5);
        add(close);

        //display UI
        setTitle("Overview ruleHeader");
        setSize(750, 375);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Theme.BACKGROUND_COLOR);
        setResizable(true);
        setVisible(true);
        LOGGER.info("Displaying Landingpage.");
    }

    private void closeWindow(){
        LOGGER.info("Closing ruleHeader and setting previous window to visible again.");
        setVisible(false);
        executingClass.setVisible(true);
    }
}

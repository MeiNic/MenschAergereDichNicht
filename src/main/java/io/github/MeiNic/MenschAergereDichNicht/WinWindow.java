package io.github.MeiNic.MenschAergereDichNicht;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

public class WinWindow{
    private final JFrame frame;
    private final JLabel message;
    private final ImageTextPanel close;
    private final ImageTextPanel nextGame;
    private final Font jetBrainsMonoSemiBold;
    private static final Color DEFAULT_FOREGROUND_COLOR = Color.decode("#f3f5f9");
    private static final Color DEFAULT_BACKGROUND_COLOR = Color.decode("#6c6f85");
    private static final Logger LOGGER = LoggerFactory.getLoggerInstance();

    public WinWindow(String player){
        frame = new JFrame();
        message = new JLabel();
        close = new ImageTextPanel("button-idle", "close");
        nextGame = new ImageTextPanel("button-idle", "new game");

        //configure font
        try {
            jetBrainsMonoSemiBold = Font.createFont(Font.TRUETYPE_FONT,
                    new File("fonts/jetBrainsMono/JetBrainsMono-SemiBold.ttf")).deriveFont(13f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(jetBrainsMonoSemiBold);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        //configure the message
        message.setText("<html><body>Congratulations! Player " + player + " <br> has won this round.</body></html>");
        message.setBounds(10, 10, 295, 32);
        message.setFont(jetBrainsMonoSemiBold);
        message.setForeground(DEFAULT_FOREGROUND_COLOR);
        message.setBackground(DEFAULT_BACKGROUND_COLOR);

        //configure close button
        close.setBounds(140, 50, 100, 32);
        close.setFont(jetBrainsMonoSemiBold);
        close.setForeground(DEFAULT_FOREGROUND_COLOR);
        close.setBackground(DEFAULT_BACKGROUND_COLOR);
        close.addMouseListener(new MouseAdapter() {
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

        //configure nextGame button
        nextGame.setBounds(20, 50, 100, 32);
        nextGame.setFont(jetBrainsMonoSemiBold);
        nextGame.setForeground(DEFAULT_FOREGROUND_COLOR);
        nextGame.setBackground(DEFAULT_BACKGROUND_COLOR);
        nextGame.addMouseListener(new MouseAdapter() {
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

        //add elements to frame
        frame.add(message);
        frame.add(close);
        frame.add(nextGame);

        //set JFrame values
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

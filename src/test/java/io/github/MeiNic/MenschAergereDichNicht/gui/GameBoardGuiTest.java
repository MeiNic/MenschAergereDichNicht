package io.github.MeiNic.MenschAergereDichNicht.gui;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import io.github.MeiNic.MenschAergereDichNicht.BackEnd;
import java.awt.*;
import javax.swing.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class GameBoardGuiTest {

    private MockedConstruction<JFrame> mockedJFrame;
    private BackEnd backEnd;
    private GameBoardGui gameBoardGui;

    @BeforeEach
    public void setUp() {
        // Setze Headless-Modus für Sicherheit
        System.setProperty("java.awt.headless", "true");

        // Testdaten
        String[] names = {"Player1", "Player2", "Player3", "Player4"};
        backEnd = new BackEnd(names, 4, false);

        // Verwende MockedConstruction für den JFrame-Konstruktor
        mockedJFrame =
                Mockito.mockConstruction(
                        JFrame.class,
                        (mock, context) -> {
                            // Konfiguriere den Mock
                            Container mockContainer = mock(Container.class);
                            when(mock.getContentPane()).thenReturn(mockContainer);
                        });

        // Erstelle GameBoardGui - ohne HeadlessException dank Mock
        gameBoardGui = new GameBoardGui(backEnd);
    }

    @AfterEach
    public void tearDown() {
        mockedJFrame.close();
    }

    @Test
    public void testGameBoardGuiInitialization() {
        assertNotNull(gameBoardGui);
    }
}

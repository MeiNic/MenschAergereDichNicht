package io.github.MeiNic.MenschAergereDichNicht.gui;

import io.github.MeiNic.MenschAergereDichNicht.BackEnd;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedConstruction;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.*;
import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

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
        backEnd = new BackEnd(names, 3, false);

        // Verwende MockedConstruction für den JFrame-Konstruktor
        mockedJFrame = Mockito.mockConstruction(JFrame.class,
                (mock, context) -> {
                    // Konfiguriere den Mock
                    Container mockContainer = mock(Container.class);
                    when(mock.getContentPane()).thenReturn(mockContainer);
                });

        // Erstelle GameBoardGui - ohne HeadlessException dank Mock
        gameBoardGui = new GameBoardGui(backEnd) {
            // Überschreibe Methoden, die UI-Operationen ausführen
            @Override
            protected void replaceFigures() {
                // Keine echte UI-Aktualisierung im Test
            }

            @Override
            protected void executeNextMove() {
                // Kein Aufruf der echten Methode im Test
            }
        };
    }

    @AfterEach
    public void tearDown() {
        // Aufräumen der Mockito-Ressourcen
        mockedJFrame.close();
    }

    @Test
    public void testGameBoardGuiInitialization() {
        // Bestätige, dass die GUI ohne Ausnahmen initialisiert wurde
        assertNotNull(gameBoardGui);

        // Verifiziere, dass ein JFrame erstellt wurde
        assertEquals(1, mockedJFrame.constructed().size());
    }
}

package io.github.MeiNic.MenschAergereDichNicht.gui;

import io.github.MeiNic.MenschAergereDichNicht.BackEnd;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameBoardGuiTest {

    GameBoardGui gameBoardGui;

    @BeforeEach
    public void setUp() {
        System.setProperty("java.awt.headless", "true");


        BackEnd backEnd = new BackEnd(new String[]{"Test", "Test", "Test", "Test"}, 4, false);
        gameBoardGui = new GameBoardGui(backEnd);
    }

    @Test
    public void test(){
        assertEquals(1, 1);
    }
}

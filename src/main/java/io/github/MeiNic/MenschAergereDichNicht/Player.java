package io.github.MeiNic.MenschAergereDichNicht;

interface Player {
    String getName();
    int getPlayerState();
    int getPlayerIndex();
    default int getIndexOfFirstFigure() {
        return 4 * getPlayerIndex();
    }
    default int getIndexOfLastFigure() {
        return 4 + getIndexOfFirstFigure();
    }
    default int getIndexOfStartField () {
        return 10 * getPlayerIndex();
    }
}

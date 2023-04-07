package com.danyarmarkin.blind_chess;

import java.util.ArrayList;

public class Player {
    private boolean isWhite;
    private Figure[] figures = new Figure[16];
    private Game game;
    private ArrayList<WindowRepaintListener> listeners = new ArrayList<>();

    public Player(boolean isWhite) {
        this.isWhite = isWhite;

        for (int i = 0; i < 8; i++) {
            Figure f = new Figure(Figure.Type.pawn, i, isWhite? 6 : 1, isWhite);
            figures[i] = f;
        }

        int y = isWhite? 7 : 0;
        figures[8] = new Figure(Figure.Type.rook, 0, y, isWhite);
        figures[9] = new Figure(Figure.Type.horse, 1, y, isWhite);
        figures[10] = new Figure(Figure.Type.elephant, 2, y, isWhite);
        figures[11] = new Figure(Figure.Type.queen, 3, y, isWhite);
        figures[12] = new Figure(Figure.Type.king, 4, y, isWhite);
        figures[13] = new Figure(Figure.Type.elephant, 5, y, isWhite);
        figures[14] = new Figure(Figure.Type.horse, 6, y, isWhite);
        figures[15] = new Figure(Figure.Type.rook, 7, y, isWhite);


    }

    public boolean isWhite() {
        return isWhite;
    }

    public void setWhite(boolean white) {
        isWhite = white;
    }

    public Figure[] getFigures() {
        return figures;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        for (WindowRepaintListener l : listeners) game.addRepaintListener(l);
    }

    public void addListener(WindowRepaintListener listener) {
        listeners.add(listener);
    }
}

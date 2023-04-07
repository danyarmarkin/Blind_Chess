package com.danyarmarkin.blind_chess;

import java.util.ArrayList;
import java.util.Collections;

public class Main {

    public static Figure[] allFigures(Player p1, Player p2) {
        Figure[] f = new Figure[32];
        for (int i = 0; i < 16; i++) {
            f[i] = p1.getFigures()[i].copy();
            f[16 + i] = p2.getFigures()[i].copy();
        }
        return f;
    }

    public static void main(String[] args) {
        ArrayList<Figure[]> figures = new ArrayList<>();

        Player whitePlayer = new Player(true);
        new Window(whitePlayer);

        Player blackPlayer = new Player(false);
        new Window(blackPlayer);

        Game game = new Game(whitePlayer, blackPlayer);
        whitePlayer.setGame(game);
        blackPlayer.setGame(game);

        figures.add(allFigures(whitePlayer, blackPlayer));

        game.addRepaintListener(() -> {
            figures.add(allFigures(whitePlayer, blackPlayer));
        });

        game.addEndGameListener(isWinnerWhite -> new HistoryWindow(figures));
    }
}

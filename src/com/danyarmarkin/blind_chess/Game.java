package com.danyarmarkin.blind_chess;

import java.util.ArrayList;

public class Game {
    private Player whitePlayer;
    private Player blackPlayer;
    private boolean whiteMove = true;
    private Figure[] figures = new Figure[32];
    private ArrayList<WindowRepaintListener> repaintListeners = new ArrayList<>();
    private ArrayList<EndGameListener> endGameListeners = new ArrayList<>();

    public Game(Player whitePlayer, Player blackPlayer) {
        this.whitePlayer = whitePlayer;
        this.blackPlayer = blackPlayer;

        for (int i = 0; i < 16; i++) {
            figures[i] = whitePlayer.getFigures()[i];
            figures[16 + i] = blackPlayer.getFigures()[i];
        }
    }

    public void addRepaintListener(WindowRepaintListener listener) {
        repaintListeners.add(listener);
    }
    public void addEndGameListener(EndGameListener listener) {endGameListeners.add(listener);}

    public boolean move(int x1, int y1, int x2, int y2) {
        Figure[] currentFigures = whiteMove? whitePlayer.getFigures() : blackPlayer.getFigures();
        Figure currentFigure = currentFigures[0];
        boolean flag = true;
        for (Figure f : currentFigures) {
            if (f.getX() == x1 && f.getY() == y1) {
                currentFigure = f;
                flag = false;
                break;
            }
        }
        if (flag) return false;

        int dx = x2 > x1? 1: -1;
        dx = x2 == x1? 0: dx;
        int dy = y2 > y1? 1: -1;
        dy = y2 == y1? 0: dy;
        if (currentFigure.getType() == Figure.Type.horse) {
            dx = x2 - x1;
            dy = y2 - y1;
        }

        int[][] mv = getMoveVariations(x1, y1);
        for (int[] p : mv) {
            if (p[0] == x2 && p[1] == y2) {
                while (x1 != x2 || y1 != y2) {
                    x1 += dx;
                    y1 += dy;
                    for (Figure f : whiteMove? blackPlayer.getFigures(): whitePlayer.getFigures()) {
                        if (f.getX() == x1 && f.getY() == y1) {
                            f.setAlive(false);
                            if (f.getType() == Figure.Type.king)
                                for(EndGameListener l : endGameListeners) l.onWin(whiteMove);
                            flag = true;
                            break;
                        }
                    }
                    if (flag) break;
                }

                if (whiteMove && y2 == 0 && currentFigure.getType() == Figure.Type.pawn) currentFigure.setType(Figure.Type.queen);
                if (!whiteMove && y2 == 7 && currentFigure.getType() == Figure.Type.pawn) currentFigure.setType(Figure.Type.queen);

                currentFigure.setCoords(x1, y1);
                whiteMove = !whiteMove;
                for (WindowRepaintListener l : repaintListeners) l.onAction();
                return true;
            }
        }
        return false;
    }

    private boolean checkCells(int x, int y, Figure[] figures) {
        for (Figure f : figures) {
            if (f.getX() == x && f.getY() == y || x > 7 || x < 0 || y > 7 || y < 0) return false;
        }
        return true;
    }

    public int[][] getMoveVariations(int x, int y, boolean white) {
        if (white == whiteMove) return getMoveVariations(x, y);
        return new int[][] {};
    }

    public int[][] getMoveVariations(int x, int y) {
        ArrayList<int[]> result = new ArrayList<>();

        Figure[] currentFigures = whiteMove? whitePlayer.getFigures() : blackPlayer.getFigures();
        Figure currentFigure = currentFigures[0];
        boolean flag = true;
        for (Figure f : currentFigures) {
            if (f.getX() == x && f.getY() == y) {
                currentFigure = f;
                flag = false;
                break;
            }
        }
        if (flag) return new int[][] {};

        switch (currentFigure.getType()) {
            case king -> {
                int[][] v = {{x + 1, y}, {x + 1, y + 1},
                        {x + 1, y - 1}, {x, y + 1},
                        {x, y - 1}, {x - 1, y},
                        {x - 1, y - 1}, {x - 1, y + 1}};
                for (int[] pair : v) {
                    if (checkCells(pair[0], pair[1], currentFigures)) result.add(new int[] {pair[0], pair[1]});
                }
            }

            case pawn -> {
                if (whiteMove) {
                    if (checkCells(x, y - 1, figures)) {
                        result.add(new int[]{x, y - 1});
                        if (y == 6 && checkCells(x, y - 2, figures)) result.add(new int[] {x, y - 2});
                    }
                    if (checkCells(x + 1, y - 1, currentFigures) && !checkCells(x + 1, y - 1, blackPlayer.getFigures()))
                        result.add(new int[] {x + 1, y - 1});
                    if (checkCells(x - 1, y - 1, currentFigures) && !checkCells(x - 1, y - 1, blackPlayer.getFigures()))
                        result.add(new int[] {x - 1, y - 1});
                } else {
                    if (checkCells(x, y + 1, figures)) {
                        result.add(new int[]{x, y + 1});
                        if (y == 1 && checkCells(x, y + 2, figures)) result.add(new int[] {x, y + 2});
                    }
                    if (checkCells(x + 1, y + 1, currentFigures) && !checkCells(x + 1, y + 1, whitePlayer.getFigures()))
                        result.add(new int[] {x + 1, y + 1});
                    if (checkCells(x - 1, y + 1, currentFigures) && !checkCells(x - 1, y + 1, whitePlayer.getFigures()))
                        result.add(new int[] {x - 1, y + 1});
                }
            }

            case horse -> {
                int[][] v = {{x + 2, y + 1}, {x + 2, y - 1},
                        {x + 1, y - 2}, {x + 1, y + 2},
                        {x - 1, y - 2}, {x - 1, y + 2},
                        {x - 2, y - 1}, {x - 2, y + 1}};
                for (int[] pair : v) {
                    if (checkCells(pair[0], pair[1], currentFigures)) result.add(new int[] {pair[0], pair[1]});
                }
            }

            case rook -> {
                int vx = x;
                while(checkCells(++vx, y, currentFigures)) result.add(new int[] {vx, y});
                vx = x;
                while(checkCells(--vx, y, currentFigures)) result.add(new int[] {vx, y});
                int vy = y;
                while(checkCells(x, --vy, currentFigures)) result.add(new int[] {x, vy});
                vy = y;
                while(checkCells(x, ++vy, currentFigures)) result.add(new int[] {x, vy});
            }

            case elephant -> {
                int vx = x, vy= y;
                while(checkCells(--vx, --vy, currentFigures)) result.add(new int[] {vx, vy});
                vx = x;
                vy = y;
                while(checkCells(--vx, ++vy, currentFigures)) result.add(new int[] {vx, vy});
                vx = x;
                vy = y;
                while(checkCells(++vx, ++vy, currentFigures)) result.add(new int[] {vx, vy});
                vx = x;
                vy = y;
                while(checkCells(++vx, --vy, currentFigures)) result.add(new int[] {vx, vy});
            }

            case queen -> {
                int vx = x;
                while(checkCells(++vx, y, currentFigures)) result.add(new int[] {vx, y});
                vx = x;
                while(checkCells(--vx, y, currentFigures)) result.add(new int[] {vx, y});
                int vy = y;
                while(checkCells(x, --vy, currentFigures)) result.add(new int[] {x, vy});
                vy = y;
                while(checkCells(x, ++vy, currentFigures)) result.add(new int[] {x, vy});
                vx = x;
                vy = y;
                while(checkCells(--vx, --vy, currentFigures)) result.add(new int[] {vx, vy});
                vx = x;
                vy = y;
                while(checkCells(--vx, ++vy, currentFigures)) result.add(new int[] {vx, vy});
                vx = x;
                vy = y;
                while(checkCells(++vx, ++vy, currentFigures)) result.add(new int[] {vx, vy});
                vx = x;
                vy = y;
                while(checkCells(++vx, --vy, currentFigures)) result.add(new int[] {vx, vy});
            }
        }

        int[][] res = new int[result.size()][2];
        res = result.toArray(res);
        return res;
    }

}

package com.danyarmarkin.blind_chess;

import javax.swing.*;
import java.awt.*;

public class Figure {
    private Type type;
    private int x = 0;
    private int y = 0;
    private boolean isAlive = true;
    private boolean isWhite = true;

    public boolean isAlive() {
        return isAlive;
    }

    public void setAlive(boolean alive) {
        isAlive = alive;
        if (!alive) setCoords(-5, -5);
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCoords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Figure(Type type, int x, int y, boolean isWhite) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.isWhite = isWhite;
    }

    public Image getImage() {
        switch (type) {
            case pawn -> {
                return new ImageIcon(isWhite? "images/pawn.png": "images/pawn_b.png").getImage();
            }
            case rook -> {
                return new ImageIcon(isWhite? "images/rook.png": "images/rook_b.png").getImage();
            }
            case horse -> {
                return new ImageIcon(isWhite? "images/horse.png": "images/horse_b.png").getImage();
            }
            case elephant -> {
                return new ImageIcon(isWhite? "images/elephant.png": "images/elephant_b.png").getImage();
            }
            case king -> {
                return new ImageIcon(isWhite? "images/king.png": "images/king_b.png").getImage();
            }
            case queen -> {
                return new ImageIcon(isWhite? "images/queen.png": "images/queen_b.png").getImage();
            }
        }
        return new ImageIcon(isWhite? "images/pawn.png": "images/pawn_b.png").getImage();
    }

    enum Type {
        pawn,
        rook,
        horse,
        elephant,
        king,
        queen,
    }
}

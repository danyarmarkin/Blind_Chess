package com.danyarmarkin.blind_chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window extends JPanel {
    private static final int WIDTH = 600;
    private static final int HEIGHT = 600;
    private static final int CELL_SIZE = 64;
    public static final int BOUNDS = 50;

    private JFrame frame;
    private Player player;
    private int[][] moveVariations = {};
    private Figure chosenFigure = null;

    public Window(Player player) {
        this.player = player;
        frame = new JFrame(player.isWhite()? "White" : "Black");
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);

        player.addListener(this::repaint);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                int x = (e.getX() - BOUNDS) / CELL_SIZE;
                int y = (e.getY() - BOUNDS) / CELL_SIZE;

                if (chosenFigure != null) {
                    boolean moveResult = player.getGame().move(chosenFigure.getX(), chosenFigure.getY(), x, y);
                    if (moveResult) {
                        moveVariations = new int[][] {};
                    }
                    chosenFigure = null;
                }
                for (Figure f : player.getFigures()) {
                    if (f.getX() == x && f.getY() == y) {
                        chosenFigure = f;
                        break;
                    }
                }
                moveVariations = player.getGame().getMoveVariables(x, y, player.isWhite());
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (i % 2 == 1 && j % 2 == 0 || i % 2 == 0 && j % 2 == 1) {
                    g.fillRect(BOUNDS + j * CELL_SIZE, BOUNDS + i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
            }
        }

        g.setColor(Color.GREEN);
        for (int[] p : moveVariations) {
            g.fillOval(BOUNDS + p[0] * CELL_SIZE + CELL_SIZE / 3,
                    BOUNDS + p[1] * CELL_SIZE + CELL_SIZE / 3,
                    CELL_SIZE / 3, CELL_SIZE / 3);
        }

        g.setColor(Color.black);
        for (int i = 0; i < 9; i++) {
            int x = BOUNDS + i * CELL_SIZE;
            g.drawLine(x, BOUNDS, x, BOUNDS + CELL_SIZE * 8);
        }
        for (int i = 0; i < 9; i++) {
            int y = BOUNDS + i * CELL_SIZE;
            g.drawLine(BOUNDS, y, BOUNDS + CELL_SIZE * 8, y);
        }

        for (Figure figure : player.getFigures()) {
            if (figure.isAlive()) {
                int x = figure.getX() * CELL_SIZE + BOUNDS;
                int y = figure.getY() * CELL_SIZE + BOUNDS;
                g.drawImage(figure.getImage(), x, y, null);
            }
        }
    }
}

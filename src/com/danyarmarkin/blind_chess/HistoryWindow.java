package com.danyarmarkin.blind_chess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class HistoryWindow extends JPanel {
    private static final int WIDTH = 900;
    private static final int HEIGHT = 800;
    private static final int CELL_SIZE = 64;
    public static final int BOUNDS = 50;

    private final ArrayList<Figure[]> figures;
    private int currentIndex = 0;

    public void next() {
        currentIndex++;
        currentIndex %= figures.size();
        repaint();
    }

    public void previous() {
        currentIndex -= 1;
        currentIndex += figures.size();
        currentIndex %= figures.size();
        repaint();
    }


    public HistoryWindow(ArrayList<Figure[]> figures) {
        this.figures = figures;
        JFrame frame = new JFrame("History");
        frame.setSize(WIDTH, HEIGHT);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(this);
        JButton toStartButton = new JButton("to start");
        toStartButton.addActionListener(e -> {
            currentIndex = 0;
            repaint();
        });
        this.add(toStartButton);

        JButton previousButton = new JButton("<---- PREVIOUS");
        previousButton.addActionListener(e -> previous());
        this.add(previousButton);

        JButton nextButton = new JButton("NEXT ---->");
        nextButton.addActionListener(e -> next());
        this.add(nextButton);

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

        g.setColor(Color.black);
        for (int i = 0; i < 9; i++) {
            int x = BOUNDS + i * CELL_SIZE;
            g.drawLine(x, BOUNDS, x, BOUNDS + CELL_SIZE * 8);
        }
        for (int i = 0; i < 9; i++) {
            int y = BOUNDS + i * CELL_SIZE;
            g.drawLine(BOUNDS, y, BOUNDS + CELL_SIZE * 8, y);
        }

        for (Figure figure : figures.get(currentIndex)) {
            if (figure.isAlive()) {
                int x = figure.getX() * CELL_SIZE + BOUNDS;
                int y = figure.getY() * CELL_SIZE + BOUNDS;
                g.drawImage(figure.getImage(), x, y, null);
            }
        }
    }
}

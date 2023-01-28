package com.danyarmarkin.blind_chess;

public class Main {
    public static void main(String[] args) {

        Player player = new Player(true);
        Window window = new Window(player);
        Player player1 = new Player(false);
        Window window1 = new Window(player1);
        Game game = new Game(player, player1);
        player.setGame(game);
        player1.setGame(game);
    }
}

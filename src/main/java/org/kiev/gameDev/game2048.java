package org.kiev.gameDev;

import javax.swing.*;

/**
 * Created by blabu on 03.07.16.
 */
public class game2048 {
    public static void main(String[] args) {
        JFrame game = new JFrame();
        gameCoreInterface model = new gameCore(5,2048);
        gameVisualizer visual = new gameVisualizer(model);

        game.setTitle("2048 Game");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(visual.getOptimalSize());
        game.setResizable(false);

        game.add(visual);

        game.setVisible(true);
    }
}

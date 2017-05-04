package org.kiev.gameDev;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by blabu on 03.07.16.
 * Клас визуализации
 */
    public class gameVisualizer extends JPanel {

        // Абстракция одна "Плитка" - определяет поведение одной плиточки в игре
        static class Tile {
            int value;
            public Tile(int num) {
                value = num;
            }
            public Color getForeground() {
                return value < 16 ? new Color(0x776e65) :  new Color(0xf9f6f2);
            }

            public Color getBackground() {
                switch (value) {
                    case 2:    return new Color(0xeee4da);
                    case 4:    return new Color(0xede0c8);
                    case 8:    return new Color(0xf2b179);
                    case 16:   return new Color(0xf59563);
                    case 32:   return new Color(0xf67c5f);
                    case 64:   return new Color(0xf65e3b);
                    case 128:  return new Color(0xedcf72);
                    case 256:  return new Color(0xedcc61);
                    case 512:  return new Color(0xedc850);
                    case 1024: return new Color(0xedc53f);
                    case 2048: return new Color(0xedc22e);
                    default:   return new Color(0xcdc1b4);
                }
            }
        }

        private static final Color BG_COLOR = new Color(0xbbada0); // Цвет фона
        private static final String FONT_NAME = "Arial"; // Шрифт цифр в табличке
        private static final int TILE_SIZE = 64;    // Размер одной таблички
        private static final int TILES_MARGIN = 16; // Ширина границ таблицы
        private static final int SCORE_XY = 25;     // Начальная позиция для надписи Счет
        private final int POLE_SIZE;                // Размер поля

        private final Tile[][] myTiles;  // Вся Плитка представление нашей матрицы в графическом интерфейсе

        private final gameCoreInterface game;    // Модель нашей игры

        private static int offsetCoors(int arg) {
            return arg * (TILES_MARGIN + TILE_SIZE) + TILES_MARGIN;
        }

        private void drawTile(Graphics g2, Tile tile, int x, int y) {
            Graphics2D g = ((Graphics2D) g2);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);

            GameStatus currentStatus = game.getStatus();
            int value = tile.value;
            int xOffset = offsetCoors(x) + SCORE_XY;
            int yOffset = offsetCoors(y) + SCORE_XY;
            g.setColor(tile.getBackground());
            g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
            g.setColor(tile.getForeground());

            final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
            final Font font = new Font(FONT_NAME, Font.BOLD, size);
            g.setFont(font);

            String s = String.valueOf(value);
            final FontMetrics fm = getFontMetrics(font);

            final int w = fm.stringWidth(s);
            final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

            if (value != 0)
                g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);

            if (currentStatus == GameStatus.YOUR_WIN ||
                    currentStatus == GameStatus.YOUR_LOSE) {
                g.setColor(new Color(255, 255, 255, 30));
                g.fillRect(0, 0, getWidth(), getHeight());
                g.setColor(new Color(78, 139, 202));
                g.setFont(new Font(FONT_NAME, Font.BOLD, 48));
                if (currentStatus == GameStatus.YOUR_WIN) {
                    g.drawString("You won!", SCORE_XY, 150);
                }
                if (currentStatus == GameStatus.YOUR_LOSE) {
                    g.drawString("Game over!", SCORE_XY, 130);
                    g.drawString("You lose!", SCORE_XY, 200);
                }
                g.setFont(new Font(FONT_NAME, Font.PLAIN, 16));
                g.setColor(new Color(128, 128, 128, 128));
                g.drawString("Press ESC to play again", SCORE_XY, getOptimalSize().height - 2*SCORE_XY);
            }
            g.setFont(new Font(FONT_NAME, Font.PLAIN, 18));
            g.drawString("Score: " + game.getScore(), SCORE_XY, SCORE_XY);
        }

        public gameVisualizer(gameCoreInterface thisGame) {
            setFocusable(true);
            game = thisGame;
            POLE_SIZE = game.getSize();
            myTiles = new Tile[POLE_SIZE][POLE_SIZE];
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        game.restartGame();
                    }
                    if(e.getKeyCode() == KeyEvent.VK_WINDOWS) {
                        System.out.println("Hello everybody");
                    }
                    if (game.canMove()) {       // Если мы можем сделать ход делаем его
                        switch (e.getKeyCode()) {
                            case KeyEvent.VK_LEFT:
                            case KeyEvent.VK_D:
                                game.toLeft();
                                break;
                            case KeyEvent.VK_RIGHT:
                            case KeyEvent.VK_A:
                                game.toRight();
                                break;
                            case KeyEvent.VK_DOWN:
                            case KeyEvent.VK_S:
                                game.toDown();
                                break;
                            case KeyEvent.VK_UP:
                            case KeyEvent.VK_W:
                                game.toUp();
                                break;
                        }
                    }
                    repaint();
                }
            });
            game.restartGame();
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
            g.setColor(BG_COLOR);
            g.fillRect(0, 0, this.getSize().width, this.getSize().height);
            int[][] matrix = game.getMatr();
            for (int i = 0; i < game.getSize(); i++) {
                for (int j = 0; j < game.getSize(); j++) {
                    myTiles[i][j] = new Tile(matrix[i][j]);
                    drawTile(g, myTiles[i][j], i, j);
                }
            }
        }

        public Dimension getOptimalSize(){
            int hight = POLE_SIZE * (TILE_SIZE + TILES_MARGIN) + TILES_MARGIN + SCORE_XY;
            int wight = hight+70;

            return new Dimension(hight, wight);
        }
    }
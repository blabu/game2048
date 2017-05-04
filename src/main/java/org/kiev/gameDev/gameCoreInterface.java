package org.kiev.gameDev;


/**
 *  Возможные состояния в игре
 */
enum GameStatus{
    YOUR_WIN,
    YOUR_LOSE,
    IN_GAME,
}

/**
 * Created by blabu on 03.07.16.
 */
public interface gameCoreInterface {
    void toLeft();
    void toRight();
    void toUp();
    void toDown();
    void restartGame();
    boolean canMove();
    int[][] getMatr();
    int getScore();
    int getSize();
    GameStatus getStatus();
}

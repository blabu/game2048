package org.kiev.gameDev;

import java.util.List;
import java.util.LinkedList;


/**
 * Created by blabu on 03.07.16.
 * Ядро игры 2048.
 * Реализация всей логики игры
 */
class gameCore implements gameCoreInterface{
    private final int POLE_SIZE;   // Размер квадратной матрицы
    private int[][] matrix;        // Сама матрица
    private final int MaxValue;    // Максимальное значение до которого играем
    private int Score;             // Счет

    private GameStatus status;      // Состояние игры

    /**
     * Класс "Пара" содержит пару значений (для передачи координат пустых ячеек матрицы)
     */
    private static class Pair{
        public final int first;
        public final int second;
        public Pair(int i1, int i2){
            first  = i1;
            second = i2;
        }
    }

    /**
     * @return Список пустых (нулевых) элементов матрицы
     */
    private List<Pair> emptyElements(){
        LinkedList<Pair> returnList = new LinkedList<Pair>();
        for (int i = 0; i < POLE_SIZE; i++) {
            for (int j = 0; j < POLE_SIZE; j++) {
                if(matrix[i][j] == 0) returnList.add(new Pair(i, j));
            }
        }
        return returnList;
    }

    /**
     * Слияние строки матрицы по правилам игры
     * @param oldLine старая строка или столбец (до слияния)
     * @return новая строка или столбец (после слияния, хода)
     */
    private int[] mergeLine(int[] oldLine){
        if(status != GameStatus.IN_GAME) return oldLine;
        int[] newLine = new int[POLE_SIZE];
        for(int i = 0, j = 0; i < POLE_SIZE; i++){
            if(oldLine[i] == 0) continue; //пропускаем все пустышки
            newLine[j] = oldLine[i];  // Если не пустышка сохраняем в новую строку
            j++;
            if(j > 1)  // Если в новой строке болше одного элемента
            {
                if(newLine[j-2] == newLine[j-1]){
                    newLine[j-1] = 0;
                    newLine[j-2] = newLine[j-2] * 2;
                    if(newLine[j-2] >= MaxValue){
                        status = GameStatus.YOUR_WIN;
                    }
                    Score += newLine[j-2];
                    j--;  // У нас на один элемент стало меньше
                }
            }
        }
        return newLine;
    }

    /**
     * Поворот матрицы на заданный угол
     * @param angle угол в градусах
     */
    private void rotateMatrix(int angle){
        angle %= 360;
        int[][] rotateMatr = new int[POLE_SIZE][POLE_SIZE];
        double rad = Math.toRadians(angle);
        int offsetX = POLE_SIZE-1, offsetY = POLE_SIZE-1;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }
        int cos = (int)Math.cos(rad);
        int sin = (int)Math.sin(rad);
        for(int i = 0; i < POLE_SIZE; i++)
            for (int j = 0; j < POLE_SIZE; j++) {
                int newX = (i * cos) - (j * sin) + offsetX;
                int newY = (i * sin) + (j * cos) + offsetY;
                rotateMatr[newX][newY] = matrix[i][j];
            }
        matrix = rotateMatr;
    }

    /**
     * Сравнение двух строк (столбцов)
     * @param line1 первая линия
     * @param line2 вторая линия
     * @return true если line1 equals line2
     */
    private boolean compare(int [] line1, int [] line2){
        return java.util.Arrays.equals(line1, line2);
    }

    /**
     * Добавляем новый элемент в пустую ячейку
     * и проверяет есть ли возможность ходаы
     */
    private void addNewElement(){
        List<Pair> allEmptyElements = emptyElements();
        if(status != GameStatus.YOUR_WIN && allEmptyElements.isEmpty()) {
            status = GameStatus.YOUR_LOSE;
            return;
        }
        int emptyListIndex = ((int)(Math.random()*100)) % allEmptyElements.size();
        Pair tempPair = allEmptyElements.get(emptyListIndex);

        int value = (Math.random() < 0.4)? 2:4;

        matrix[tempPair.first][tempPair.second] = value;
    }

    /**
     *  Обработка нажатия кнопки вверх
     */
    public void toUp(){
        boolean isChanged = false;
        if(status == GameStatus.YOUR_LOSE) return;
        for(int i = 0; i < POLE_SIZE; i++){
            int[] oldLine = mergeLine(matrix[i]);
            if(!compare(oldLine, matrix[i])) {
                isChanged = true;
                matrix[i] = oldLine;
            }
        }
        if(isChanged) addNewElement();
    }

    /**
     *  Обработка нажатия кнопки вниз
     */
    public void toDown(){
        rotateMatrix(180);
        toUp();
        rotateMatrix(180);
    }

    /**
     * Обработка нажатия кнопки влево
     */
    public void toLeft(){
        rotateMatrix(90);
        toUp();
        rotateMatrix(270);
    }

    /**
     * Обработка нажатия кнопки вправо
     */
    public void toRight(){
        rotateMatrix(270);
        toUp();
        rotateMatrix(90);
    }

    /**
     * Создание работающей модели
     * @param poleSize Размер игрового поля
     * @param maxValue До скольки играем
     */
    public gameCore(int poleSize, int maxValue) {
        if(poleSize > 2) POLE_SIZE = poleSize;
        else POLE_SIZE = 4;
        if((Math.sqrt(maxValue))%2 != 0) MaxValue = 2048;
        else MaxValue = maxValue;

        matrix = new int[POLE_SIZE][POLE_SIZE];
        for(int i = 0; i<POLE_SIZE; i++) {
            for(int j = 0; j<POLE_SIZE; j++){
                matrix[i][j] = 0;
            }
        }
        Score = 0;
        status = GameStatus.IN_GAME;
    }

    /**
     * Перезагрузка Игры
     */
    public void restartGame(){
        for(int i = 0; i<POLE_SIZE; i++) {
            for(int j = 0; j<POLE_SIZE; j++){
                matrix[i][j] = 0;
            }
        }
        Score = 0;
        status = GameStatus.IN_GAME;
        addNewElement();
        addNewElement();
    }

    public int getSize(){
        return POLE_SIZE;
    }

    public int getScore(){
        return Score;
    }

    public GameStatus getStatus(){
        return status;
    }

    public int [][] getMatr(){
        return matrix.clone();
    }

    /**
     * Есть ли возможность зделать ход
     * @return true если ход возможен
     */
    public boolean canMove(){
        if(!emptyElements().isEmpty()) return true;
        for(int i = 0; i < POLE_SIZE-1; i++){
            for(int j = 0; j < POLE_SIZE-1; j++){
                if(matrix[i][j] == matrix[i][j+1] ||
                        matrix[i][j] == matrix[i+1][j]) return true;
            }
        }
        if(status != GameStatus.YOUR_WIN){
            status = GameStatus.YOUR_LOSE;
        }
        return false;
    }
}


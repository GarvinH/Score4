/*
 * [Score4.java]
 * Connect Four in 3D Assignment
 * @author Albert Quon, Garvin Hui
 * 04/03/2019
 */

import javax.swing.*;
import java.util.Random;
import java.util.Scanner;

class Score4 {

    // MAIN METHOD
    public static void main(String[] args) {
        //String boardSizeInput = JOptionPane.showInputDialog("Please enter the board size:");
        //int boardSize = Integer.parseInt(boardSizeInput);
        int[][][] grid = {
                {       {1,1,0,-1},//floor 1
                        {1,0,0,-1},
                        {0,0,0,0},
                        {0,0,0,0}},

                {       {1,0,0,0},//floor 2
                        {0,0,0,0},
                        {0,0,0,0},
                        {0,0,0,0}},

                {       {1,0,0,0},//floor 3
                        {0,0,0,0},
                        {0,0,0,0},
                        {0,0,0,0}},

                {       {0,0,0,0}, //floor4
                        {0,0,0,0},
                        {0,0,0,0},
                        {0,0,0,0}}
        };
        int[][][] testGrid2 = {
                {       {0,0,0,0,0},//floor 1
                        {0,0,0,0,0},
                        {0,0,0,0,0},
                        {0,-1,0,0,0},
                        {0,0,0,0,0}},

                {       {0,0,0,0,0},//floor 2
                        {0,0,0,0,0},
                        {0,0,-1,0,0},
                        {0,0,0,0,0},
                        {0,0,0,0,0}},

                {       {0,0,0,0,0},//floor 3
                        {0,0,0,-1,0},
                        {0,0,0,0,0},
                        {0,0,0,0,0},
                        {0,0,0,0,0}},

                {       {0,0,0,0,-1},//floor 4
                        {0,0,0,0,0},
                        {0,0,0,0,0},
                        {0,0,0,0,0},
                        {0,0,0,0,0}},

                {       {0,0,0,0,0},//floor 5
                        {0,0,0,0,0},
                        {0,0,0,0,0},
                        {0,0,0,0,0},
                        {0,0,0,0,0}

                }
        };

        /*int board[][][] = new int[boardSize][boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int k = 0; k < boardSize; k++) {
                for (int o = 0; o < boardSize; o++) {
                    board[i][k][o] = 0;
                }
            }
        }*/

        // Initialize Map
        //moveItemsOnGrid(map);

        // display the fake grid on Console
        //DisplayGridOnConsole(map);

        //Set up Grid Panel
//      DisplayGrid grid = new DisplayGrid(board);
        turn(grid);

        for (int i = 0; i < grid.length; i++) {
            System.out.println("Floor " + (i+1));
            for (int j = 0; j < grid.length; j++) {

                for (int k = 0; k < grid.length; k++) {
                    System.out.print(grid[i][j][k]);
                }
                System.out.println("");

            }

        }
        int win2 = win(grid);
        System.out.println(win2);

        /*while (true) {
            //Display the grid on a Panel
  //          grid.refresh();


            //Small delay
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            ;


            // Initialize Map (Making changes to map)
            //moveItemsOnGrid(map);

            //Display the grid on a Panel
    //        grid.refresh();
        }*/
    }

    // END OF MAIN

    //method to display grid a text for debugging
//    public static void DisplayGridOnConsole(String[][] map) {
//        for(int i = 0; i<map.length;i++){
//            for(int j = 0; j<map[0].length;j++)
//                System.out.print(map[i][j]+" ");
//            System.out.println("");
//        }
//    }


    // START of WIN CHECKING METHODS AND BEST MOVE METHODS
    /**
     * Checks if any player has won
     * @param grid Current Game State
     * @return An integer value that states if player won, computer won, or no win was determined based on the sign of the integer
     */
    static int win(int[][][] grid) {
        int checkWin = 0;
        for (int i = 0; i < grid.length; i++) { // loop through the entire grid (i is used for different parameters)
            // check through rows
            checkWin += rowUpWin(grid, 0, i, 0) + rowUpWin(grid, 0, 0, i);
            checkWin += diagRowUpWin(grid, 0, i, 0) + diagRowUpWin(grid, 0, 0, i);
            // check through columns
            checkWin += colUpWin(grid, i, 0, 0) + colUpWin(grid, 0, 0, i);
            checkWin += diagColUpWin(grid, i, 0, 0) + diagRowColUpWin(grid, 0, 0, i);
            // check diagonally on the same height
            checkWin += diagSameFloorWin(grid, i, 0, 0) + diagSameFloorWin(grid, 0, i, 0) +
                    diagSameFloorWin(grid, 0, 0, i); // checks from the right
            checkWin += diagBackSameFloorWin(grid, i, grid.length-1, 0) + diagBackSameFloorWin(grid, 0, grid.length-1-i, 0) +
                    diagBackSameFloorWin(grid, 0, grid.length-1, i); // checks from the left
            // check through heights and diagonally through heights (bottom to top)
            checkWin += floorUpWin(grid, 0, i, 0) + floorUpWin(grid, i, 0, 0);
            checkWin += diagRowColUpWin(grid, i, 0, 0) + diagRowColUpWin(grid, 0, i, 0) +
                    diagRowColUpWin(grid, 0, 0, i);
            checkWin += diagLeftUpWin(grid, i, grid.length-1, 0) + diagLeftUpWin(grid, 0, grid.length-1-i, 0) +
                    diagLeftUpWin(grid, 0, grid.length-1, i);
            //check from top to bottom across rows, then columns, then diagonally
            checkWin += rowDownWin(grid, 0, i, (grid.length-1)) + rowDownWin(grid, 0, 0, (grid.length-1)-i);
            checkWin += colDownWin(grid, i, 0, (grid.length-1)) + colDownWin(grid, 0, 0, (grid.length-1)-i);
            checkWin += colRowDownWin(grid, i, 0, (grid.length-1)) + colRowDownWin(grid, 0, i, (grid.length-1)) +
                            colRowDownWin(grid, i, 0, (grid.length-1)-i);
            checkWin += colRowDownBackWin(grid, i, grid.length-1, (grid.length-1)) + colRowDownBackWin(grid, 0, grid.length-1-i, (grid.length-1)) +
                    colRowDownBackWin(grid, 0, grid.length-1, (grid.length-1)-i);

        }
        return checkWin;
    }

    // loop through the grid
    static void randomMove(int[][][] grid) {
        Random rand = new Random();
        int randX;
        int randY;
        boolean floorClear = false;
        boolean played = false;
        for (int i = 0; i < grid.length; i++) {
            //System.out.println("Floor " + (i + 1));
            for (int j = 0; j < grid.length; j++) {
                for (int k = 0; k < grid.length; k++) {
                    if (grid[i][j][k] == 0) {
                        floorClear = true;

                    }
                    //System.out.print(grid[i][j][k]);
                }
                //System.out.println("");
            }
            while (!played && floorClear) {
                randX = rand.nextInt(4);
                randY = rand.nextInt(4);
                if (grid[i][randX][randY] == 0) {
                    grid[i][randX][randY] = 1;
                    played = true;
                }
            }
            //System.out.println("");
        }

    }

    /**
     *
     * @param grid
     */
    static void turn(int grid[][][]) {
        int size = grid.length;
        int[][][] playerCopy = new int[size][size][size];
        int[][][] playerValues = new int[size][size][size];
        int[][][] currentCopy = new int[size][size][size];
        int[][][] currentValues = new int[size][size][size];
        int[][][] bestValues = new int[size][size][size];
        int bestCompMove;
        int bestPlayerMove;
        int bestMove = 0; // stores the highest value

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    playerCopy[i][j][k] = grid[i][j][k]; // create copies of player's possible moves
                    currentCopy[i][j][k] = grid[i][j][k]; // create copies of computer's possible moves
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (grid[i][j][k] == 0) {
                        if ((i == 0)) {
                            currentCopy[i][j][k] = -1;
                            playerCopy[i][j][k] = 1;
//                            for (int a = 0; a < size; a++) {
//                                for (int b = 0; b < size; b++) {
//                                    for (int c = 0; c < size; c++) {
//                                        System.out.print(currentCopy[a][b][c]);
//                                    }
//                                    System.out.println("");
//                                }
//                                System.out.println("");
//                            }
                            bestPlayerMove = checkCombos(playerCopy, j, k, i, 1);
                            bestCompMove = checkCombos(currentCopy, j, k, i, -1);
                            //System.out.println(i+":"+j+":"+k);

                            if (bestPlayerMove > Math.abs(bestMove)) {
                                bestMove = bestPlayerMove;
                            }
                            if (Math.abs(bestCompMove) > Math.abs(bestMove)) {
                                bestMove = bestCompMove;
                            }
                            if (bestPlayerMove > Math.abs(bestCompMove)) {
                                bestValues[i][j][k] = bestPlayerMove;
                            }
                            else if (Math.abs(bestCompMove) > bestPlayerMove) {
                                bestValues[i][j][k] = bestCompMove;
                            }

                            currentCopy[i][j][k] = 0;
                            playerCopy[i][j][k] = 0;

                        } else if ((grid[i-1][j][k] != 0)) {
                            currentCopy[i][j][k] = -1;
                            playerCopy[i][j][k] = 1;
                            bestPlayerMove = checkCombos(playerCopy, j, k, i, 1);
                            bestCompMove = checkCombos(currentCopy, j, k, i, -1);

                            if (bestPlayerMove > Math.abs(bestMove)) {
                                bestMove = bestPlayerMove;
                            }
                            if (Math.abs(bestCompMove) > Math.abs(bestMove)) {
                                bestMove = bestCompMove;
                            }

                            if (bestPlayerMove > Math.abs(bestCompMove)) {
                                bestValues[i][j][k] = bestPlayerMove;
                            }
                            else if (Math.abs(bestCompMove) > bestPlayerMove) {
                                bestValues[i][j][k] = bestCompMove;
                            }


                            currentCopy[i][j][k] = 0;
                            playerCopy[i][j][k] = 0;

                        }


                    }

                }
            }
        }
        boolean placed = false;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    //System.out.print(bestValues[i][j][k]);
                    if (Math.abs(bestValues[i][j][k]) == Math.abs(bestMove) && !placed) {
                        grid[i][j][k] = -1;
                        placed = true;
                        //System.out.println(i+":"+j+":"+k);
                    }
                }
                //System.out.println("");
            }
            //System.out.println("");
        }

    }


    /**
     * Checks all possible fourteen locations around a move, gives a score based on the number of combinations present
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param player
     * @return
     */
    static int checkCombos(int[][][] grid, int row, int column, int height, int player) {
        int score = 0;
        score += left(grid, row, column, height,0, player);
        score += up(grid, row, column, height,0, player);
        score += right(grid, row, column, height,0, player);
        score += down(grid, row, column, height,0, player);
        score += upLeft(grid, row, column, height,0, player);
        score += downLeft(grid, row, column, height,0, player);
        score += upRight(grid, row, column, height,0, player);
        score += downRight(grid, row, column, height,0, player);

        score += above(grid, row, column, height,0, player);
        score += aboveFront(grid, row, column, height, 0, player);
        score += aboveFrontLeft(grid, row, column, height,0, player);
        score += aboveFrontRight(grid, row, column, height,0, player);
        score += aboveBehind(grid, row, column, height, 0, player);
        score += aboveBehindLeft(grid, row, column, height,0, player);
        score += aboveBehindRight(grid, row, column, height,0, player);
        score += aboveLeft(grid, row, column, height,0, player);
        score += aboveRight(grid, row, column, height,0, player);

        score += below(grid, row, column, height,0, player);
        score += belowFront(grid, row, column, height,0, player);
        score += belowFrontLeft(grid, row, column, height,0, player);
        score += belowFrontRight(grid, row, column, height,0, player);
        score += belowBehind(grid, row, column, height,0, player);
        score += belowBehindLeft(grid, row, column, height,0, player);
        score += belowBehindRight(grid, row, column, height,0, player);
        score += belowLeft(grid, row, column, height,0, player);
        score += belowRight(grid, row, column, height,0, player);

        return score;

    }

    //END OF WIN CHECKING METHODS AND BEST MOVE METHODS






    // RECURSIVE HELPER METHODS
    // START OF WIN CHECKING RECURSIVE METHODS


    /**
     * Determines if there's a four in a row in a specific row through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int rowUpWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length-4)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height][row+i][column];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return rowUpWin(grid, row+1, column, height);
    }

    /**
     * Determines if there's a four in a row in a specific column through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int colUpWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((column > grid.length-4)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height][row][column+i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        // check column
        return colUpWin(grid, row, column+1, height);
    }

    /**
     * Determines if there's a four in a row in a specific height through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int floorUpWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((height > grid.length-4)) {
            return 0;
        }
        // check height
        for (int i = 0; i < 4; i++) {
            sum+=grid[height+i][row][column];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return floorUpWin(grid, row, column, height+1);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int diagSameFloorWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length - 4) || (column > grid.length - 4)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height][row+i][column+i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return diagSameFloorWin(grid, row+1, column+1, height);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int diagBackSameFloorWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length-4) || (column < 3)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height][row+i][column-i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return diagBackSameFloorWin(grid, row+1, column-1, height);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int diagRowUpWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length - 4) || (height > grid.length - 4)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height+i][row+i][column];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return diagRowUpWin(grid, row+1, column, height+1);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int diagColUpWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((column > grid.length - 4) || (height > grid.length - 4)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height+i][row][column+i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return diagColUpWin(grid, row, column+1, height+1);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int diagRowColUpWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length - 4) || (column > grid.length - 4) || (height > grid.length - 4)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height+i][row+i][column+i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return diagRowColUpWin(grid, row+1, column+1, height+1);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int diagLeftUpWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length - 4) || (column < 3) || (height > grid.length - 4)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height+i][row+i][column-i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return diagRowColUpWin(grid, row+1, column-1, height+1);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int rowDownWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length-4)|| (height < 3)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height-i][row+i][column];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return rowDownWin(grid, row+1, column, height-1);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int colDownWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((column > grid.length - 4) || (height < 3)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height-i][row][column+i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return colDownWin(grid, row, column+1, height-1);
    }

    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int colRowDownWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length - 4) || (column > grid.length - 4) || (height < 3)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height-i][row+i][column+i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return colRowDownWin(grid, row+1, column+1, height-1);
    }
    /**
     * Determines if there's a four in a row in a ______ through recursion
     * @param grid The current game state
     * @param row The starting row
     * @param column The starting column
     * @param height The starting height
     * @return An integer value that is negative, positive, or zero that respectively represents a player win, computer win, or no win
     */
    static int colRowDownBackWin(int[][][] grid, int row, int column, int height) {
        int sum = 0;
        if ((row > grid.length - 4) || (column < 3) || (height < 3)) {
            return 0;
        }
        for (int i = 0; i < 4; i++) {
            sum+=grid[height-i][row+i][column-i];
        }
        if ((sum == 4) || (sum == -4)) {
            return sum;
        }
        return colRowDownBackWin(grid, row+1, column-1, height-1);
    }

    // END OF RECURSIVE WIN CHECKING METHODS





    // START OF BEST MOVE RECURSIVE METHODS

    // METHODS THAT CHECK FOR COMBINATIONS ON THE SAME HEIGHT
    // Row: [UP, DOWN] Column: [LEFT, RIGHT] Diagonals: upRight, upLeft, downLeft, downRight
    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int up(int[][][] grid, int row, int column, int height, int combo, int playerType) {

        if (row < 0) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return up(grid, row-1, column, height, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int down(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if (row >= grid.length) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return down(grid, row+1, column, height, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int left(int[][][] grid, int row, int column, int height, int combo, int playerType) {

        if (column < 0) {
            return (int) (Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {

            return (int) Math.pow(combo, combo) * playerType;
        }
        return left(grid, row, column-1, height, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int right(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if (column == grid.length) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {

            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return right(grid, row, column+1, height, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int upLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((column < 0) || (row < 0)){
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return upLeft(grid, row-1, column-1, height, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int upRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((column == grid.length) || (row < 0)){
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return upRight(grid, row-1, column+1, height, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int downLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((column < 0) || (row == grid.length)){
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return downLeft(grid, row+1, column-1, height, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int downRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((column == grid.length) || (row == grid.length)){
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return downRight(grid, row+1, column+1, height, combo+1, playerType);
    }

    // END OF SAME METHODS ON SAME HEIGHT





    // START OF METHODS THAT CHECK VARYING HEIGHTS
    // Height: [Above = +1, Down = -1], Rows: [Front = -1, Behind = +1], Columns: [Left = -1, Right = +1]
    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int above(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if (height == grid.length) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return above(grid, row, column, height+1, combo+1, playerType);
    }
    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int aboveFront(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return aboveFront(grid, row-1, column, height+1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int aboveBehind(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row == grid.length)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return aboveBehind(grid, row+1, column, height+1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int aboveLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (column < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return aboveLeft(grid, row, column-1, height+1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int aboveRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (column == grid.length)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return aboveRight(grid, row, column+1, height+1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int aboveFrontLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row < 0) || (column < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return aboveFrontLeft(grid, row-1, column-1, height+1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int aboveFrontRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row < 0) || (column == grid.length)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return aboveFrontRight(grid, row-1, column+1, height+1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int aboveBehindLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row == grid.length) || (column < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return aboveBehindLeft(grid, row+1, column-1, height+1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int aboveBehindRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row == grid.length) || (column == grid.length)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return aboveBehindRight(grid, row+1, column+1, height+1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int below(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return below(grid, row, column, height-1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int belowFront(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return belowFront(grid, row-1, column, height-1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int belowBehind(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row == grid.length)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return belowBehind(grid, row+1, column, height-1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int belowLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (column < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return belowLeft(grid, row, column-1, height-1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int belowRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (column == grid.length)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return belowRight(grid, row, column+1, height-1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int belowFrontLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row < 0) || (column < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return belowFrontLeft(grid, row-1, column-1, height-1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int belowFrontRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row < 0) || (column == grid.length)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return belowFrontRight(grid, row-1, column+1, height-1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int belowBehindLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row == grid.length) || (column < 0)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return belowBehindLeft(grid, row+1, column-1, height-1, combo+1, playerType);
    }

    /**
     *
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param combo
     * @param playerType
     * @return
     */
    static int belowBehindRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row == grid.length) || (column == grid.length)) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        if (grid[height][row][column] != playerType) {
            return (int) Math.abs(Math.pow(combo, combo)) * playerType;
        }
        return belowBehindRight(grid, row+1, column+1, height-1, combo+1, playerType);
    }





}

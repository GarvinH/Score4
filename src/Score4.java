/* [Score4.java]
 * Score 4 game (3d Connect 4). Has AI and player moves.
 * Author: Albert Quon, Garvin Hui
 */
import javax.swing.*;
import java.util.Random;

class Score4 {
    public static void main(String[] args) {
        boolean validSize;
        int boardSize;
        String boardSizeInput = JOptionPane.showInputDialog("Please enter the board size:");
        do {
            validSize = true;
            for (int i = 0; i < boardSizeInput.length(); i++) {
                if ((boardSizeInput.charAt(i) < '1') || (boardSizeInput.charAt(i) > '9')) {
                    validSize = false;
                }
            }
            if (!validSize) {
                boardSizeInput = JOptionPane.showInputDialog("Please enter a valid board size:");
            }
        } while (!validSize);
        boardSize = Integer.parseInt(boardSizeInput);

        int playerTurn;//-1 will be AI turn and 1 will be player turn.
        Object[] options = {"Player", "Computer"};
        playerTurn = JOptionPane.showOptionDialog(null, "Choose who goes first.", "Choose player turn", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        if (playerTurn == 0) {
            playerTurn = 1;
        } else {
            playerTurn = -1;
        }
        int numberTurns = 0;

        int board[][][] = new int[boardSize][boardSize][boardSize];

        for (int i = 0; i < boardSize; i++) {
            for (int k = 0; k < boardSize; k++) {
                for (int o = 0; o < boardSize; o++) {
                    board[i][k][o] = 0;
                }
            }
        }

        //Set up Grid Panel
        DisplayGrid grid = new DisplayGrid(board);
        boolean quit = false;
        boolean won = false;
        while (!quit) {
            //Display the grid on a Panel
            grid.refresh();
            if (playerTurn == 1) {
                userMove(board);
            } else {
                boardUpdate(board, randomMove(board), playerTurn);
            }

            playerTurn *= -1;
            numberTurns++;

            //Display the grid on a Panel
            grid.refresh();
        }
    }

    public static void userMove (int board[][][]) {
        String input = JOptionPane.showInputDialog("Please Enter Coordinates");
        boolean valid;
        boolean placed;
        char letter;
        do {
            valid = true;
            placed = false;
            if (input == null) {
                valid = false;
            } else if (!input.contains(",")) {
                valid = false;
            }
            if (valid) {
                String[] userInput = input.split(",");
                for (int i = 0; i < 2; i++) {
                    for (int k = 0; k < userInput[i].length(); k++) {
                        letter = userInput[i].charAt(k);
                        if ((letter < '0') || (letter > '9')) {
                            valid = false;
                        }
                    }
                }
                if (valid) {
                    int[] coordinates = new int[2];
                    for (int i = 0; i < 2; i++) {
                        coordinates[i] = Integer.parseInt(userInput[i])-1;
                    }
                    placed = boardUpdate(board, coordinates, 1);
                }
            }
            if (!placed) {
                input = JOptionPane.showInputDialog("Please Enter Valid Point");
            }
        } while ((!valid) || (!placed));
    }

    public static boolean boardUpdate(int board[][][], int[] coordinate, int player)  {
        boolean placed = false;
        int level = 0;
        boolean valid = true;
        coordinate[1] = board.length-coordinate[1]-1;
        if ((coordinate[0] < 0) || (coordinate[1] < 0)) {
            valid = false;
        } else if ((coordinate[0] > board.length) || (coordinate[1] > board.length)){
            valid = false;
        }
        /*if (board[board.length-1][coordinate[1]][coordinate[0]] != 0) {
            valid = false;
        }*/
        if (valid) {
            while ((!placed) && (level < board.length)) {
                if ((board[level][coordinate[1]][coordinate[0]] == 0)) {
                    if (player == 1) {
                        board[level][coordinate[1]][coordinate[0]] = 1;
                        placed = true;
                    } else {
                        board[level][coordinate[1]][coordinate[0]] = -1;
                        placed = true;
                    }
                } else {
                    level++;
                }
            }
        }
        return placed;
    }

    static int[] randomMove(int[][][] grid) {
        Random rand = new Random();
        int[] coordinate = new int[2];
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
                randX = rand.nextInt(grid.length);
                randY = rand.nextInt(grid.length);
                if (grid[i][randX][randY] == 0) {
                    coordinate[0] = randX;
                    coordinate[1] = randY;
                    played = true;
                    //return coordinate;
                }
            }
            //System.out.println("");
        }
        return coordinate;
    }
    
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

    /**
     * Determines the computers move based on the current game state
     * @param grid Current Game State
     */
    static int[] turn(int grid[][][]) {
        int size = grid.length;
        int[][][] playerCopy = new int[size][size][size];
        int[][][] currentCopy = new int[size][size][size];
        int[][][] bestValues = new int[size][size][size];
        int[] coordinate = new int[2];
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
                            } else if (Math.abs(bestCompMove) > bestPlayerMove) {
                                bestValues[i][j][k] = bestCompMove;
                            } else if (Math.abs(bestCompMove) == bestPlayerMove) {
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
                    System.out.print(bestValues[i][j][k]);
                    if (Math.abs(bestValues[i][j][k]) == Math.abs(bestMove) && !placed) {
                        //grid[i][j][k] = -1;
                        placed = true;
                        //System.out.println(i+":"+j+":"+k);
                        coordinate[0] = k;
                        coordinate[1] = j;
                    }
                }
                System.out.println("");
            }
            System.out.println("");
        }
        return coordinate;
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

        // checks all directions on the same plane/height
        score += left(grid, row, column, height,0, player);
        score += up(grid, row, column, height,0, player);
        score += right(grid, row, column, height,0, player);
        score += down(grid, row, column, height,0, player);
        score += upLeft(grid, row, column, height,0, player);
        score += downLeft(grid, row, column, height,0, player);
        score += upRight(grid, row, column, height,0, player);
        score += downRight(grid, row, column, height,0, player);

        //checks all directions above the move
        if (height < grid.length) {
            score += above(grid, row, column, height, 0, player);
            score += aboveFront(grid, row, column, height, 0, player);
            score += aboveFrontLeft(grid, row, column, height, 0, player);
            score += aboveFrontRight(grid, row, column, height, 0, player);
            score += aboveBehind(grid, row, column, height, 0, player);
            score += aboveBehindLeft(grid, row, column, height, 0, player);
            score += aboveBehindRight(grid, row, column, height, 0, player);
            score += aboveLeft(grid, row, column, height, 0, player);
            score += aboveRight(grid, row, column, height, 0, player);
        }
        // checks all directions below a move
        if (height > 0) {
            score += below(grid, row, column, height, 0, player);
            score += belowFront(grid, row, column, height, 0, player);
            score += belowFrontLeft(grid, row, column, height, 0, player);
            score += belowFrontRight(grid, row, column, height, 0, player);
            score += belowBehind(grid, row, column, height, 0, player);
            score += belowBehindLeft(grid, row, column, height, 0, player);
            score += belowBehindRight(grid, row, column, height, 0, player);
            score += belowLeft(grid, row, column, height, 0, player);
            score += belowRight(grid, row, column, height, 0, player);
        }

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

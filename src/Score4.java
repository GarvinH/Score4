/* [Score4.java]
 * Score 4 game (3d Connect 4). Has AI and player moves.
 * @author Albert Quon, Garvin Hui
 */
import javax.swing.*;

class Score4 {

    /**
     * Main Method for the game
     * @author Albert Quon & Garvin Hui
     * @param args
     */
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

        int[][][] testGrid = {
                {       {-1,0,-1,-1},//floor 1
                        {1,1,1,-1},
                        {1,1,1,-1},
                        {1,0,-1,1}},

                {       {1,0,0,0},//floor 2
                        {0,-1,0,0},
                        {0,0,-1,0},
                        {0,0,0,0}},

                {       {0,0,0,0},//floor 3
                        {0,0,0,0},
                        {0,-1,1,0},
                        {0,0,0,0}},

                {       {0,0,0,0}, //floor4
                        {0,0,0,0},
                        {0,1,1,0},
                        {0,0,0,0}}
        };

        //Set up Grid Panel
        DisplayGrid grid = new DisplayGrid(board);
        boolean quit = false;
        boolean won = false;
        boolean aiPlaced;
        int win;
        while (!quit && !won) {
            //Display the grid on a Panel
            grid.refresh();
            if (playerTurn == 1) {
                userMove(board);
            } else {
//                do {
//                    aiPlaced = boardUpdate(board, turn(board), playerTurn);
//                } while (!aiPlaced);
                boardUpdate(board, turn(board, playerTurn), playerTurn);
            }
            win=win(board);
            if (win == 2) {
                System.out.println("tie!");
                won = true;
            } else if (win < 0) {
                System.out.println("computer wins!");
                won = true;
            } else if (win > 0) {
                won = true;
                System.out.println("player wins!");
            }
            playerTurn *= -1;
            numberTurns++;

            //Display the grid on a Panel
            grid.refresh();
        }
    }

    /**
     * Validates User Input
     * @author Garvin Hui
     * @param board Current Game State
     */
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

    /**
     * Updates the board with computer and user input
     * @author Garvin Hui
     * @param board
     * @param coordinate
     * @param player
     * @return a boolean value indicating whether the board has been updated or not
     */
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

    // **********START of WIN CHECKING METHODS AND BEST MOVE METHODS *************************************************************************

    /**
     * Checks if any player has won
     * @author Albert Quon
     * @param grid Current Game State
     * @return An integer value that states if player won, computer won, or no win was determined based on the sign of the integer
     */
    static int win(int[][][] grid) {
        int checkWin = 0;
        boolean space = false;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                for (int k = 0; k < grid.length; k++) {
                    if (grid[i][j][k] == 0) {
                        space = true;
                    }
                }
            }
        }
        if (space) {
            for (int i = 0; i < grid.length; i++) { // loop through the entire grid (i is used for different parameters)
                // i and j alternate in parameters, never repeated if the method is called more than once within a variable
                for (int j = 0; j < grid.length; j++) {
                    checkWin += rowUpWin(grid, 0, i, j);
                    checkWin += diagRowUpWin(grid, 0, i, j);
                    // check through columns
                    checkWin += colUpWin(grid, i, 0, j);
                    checkWin += diagColUpWin(grid, i, 0, j);
                    // check diagonally on the same height
                    checkWin += diagSameFloorWin(grid, i, j, 0) + diagSameFloorWin(grid, 0, i, j) +
                            diagSameFloorWin(grid, j, 0, i); // checks for right diagonals
                    checkWin += diagBackSameFloorWin(grid, i, grid.length - 1, j) + diagBackSameFloorWin(grid, j, grid.length - 1 - i, 0) +
                            diagBackSameFloorWin(grid, 0, grid.length - 1 - j, i); // checks left diagonals
                    // check through heights and diagonally through heights (bottom to top)
                    checkWin += floorUpWin(grid, j, i, 0);
                    checkWin += diagRowColUpWin(grid, i, j, 0) + diagRowColUpWin(grid, 0, i, j) +
                            diagRowColUpWin(grid, j, 0, i);
                    checkWin += diagLeftUpWin(grid, i, grid.length - 1 - j, 0) + diagLeftUpWin(grid, j, grid.length - 1, i) +
                            diagLeftUpWin(grid, 0, grid.length - 1 - i, j);
                    //check from top to bottom across rows, then columns, then diagonally
                    checkWin += rowDownWin(grid, 0, i, (grid.length - 1) - j);
                    checkWin += colDownWin(grid, i, 0, (grid.length - 1) - j);
                    checkWin += colRowDownWin(grid, i, 0, (grid.length - 1) - j) + colRowDownWin(grid, 0, i, (grid.length - 1) - j) +
                            colRowDownWin(grid, i, j, (grid.length - 1));
                    checkWin += colRowDownBackWin(grid, i, grid.length - 1 - j, (grid.length - 1)) + colRowDownBackWin(grid, 0, grid.length - 1 - i, (grid.length - 1) - j) +
                            colRowDownBackWin(grid, j, grid.length - 1, (grid.length - 1) - i);

                }
            }
        } else {
            checkWin = 2; // indicates a tie
        }
        return checkWin;
    }

    /**
     * Determines the computers move based on the current game state
     * @author Albert Quon
     * @param grid Current Game State
     * @param turn Value that indicates priority
     * @return Coordinate of the computer's move in an array
     */
    static int[] turn(int grid[][][], int turn) {
        int size = grid.length;
        int[][][] playerCopy = new int[size][size][size];
        int[][][] compCopy = new int[size][size][size];
        int[][][] bestCombos = new int[size][size][size];
        int[] coordinate = new int[2];
        int playerCombo, compCombo;
        int sameCombo = 0, bestCombo = 0;
        boolean placed = false;
        int bestMove = 0; // stores the highest value

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    playerCopy[i][j][k] = grid[i][j][k]; // create copies of player's possible moves
                    compCopy[i][j][k] = grid[i][j][k]; // create copies of computer's possible moves
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (grid[i][j][k] == 0) {
                        if ((i == 0)) { // bottom floor
                            compCopy[i][j][k] = -1;
                            playerCopy[i][j][k] = 1;
                            playerCombo = checkCombos(playerCopy, j, k, i, 1);
                            compCombo = checkCombos(compCopy, j, k, i, -1);

                            // check if this is the highest combination found
                            if (playerCombo > Math.abs(bestCombo)) {
                                bestCombo = playerCombo;
                            }
                            if (Math.abs(compCombo) > Math.abs(bestCombo)) {
                                bestCombo = compCombo;
                            }
                            // check if this is the best combo for the spot on the grid
                            if (playerCombo > Math.abs(compCombo)) {
                                bestCombos[i][j][k] = playerCombo;
                            } else if (Math.abs(compCombo) > playerCombo) {
                                bestCombos[i][j][k] = compCombo;
                            }
                            // if player's and computer's move both match
                            if (playerCombo == Math.abs(compCombo)) {
                                sameCombo = playerCombo;
                            }

                            compCopy[i][j][k] = 0;
                            playerCopy[i][j][k] = 0;

                        } else if ((grid[i-1][j][k] != 0)) { // bottom floor
                            compCopy[i][j][k] = -1;
                            playerCopy[i][j][k] = 1;
                            playerCombo = checkCombos(playerCopy, j, k, i, 1);
                            compCombo = checkCombos(compCopy, j, k, i, -1);
                            // check if this is the highest combination found
                            if (playerCombo > Math.abs(bestCombo)) {
                                bestCombo = playerCombo;
                            }
                            if (Math.abs(compCombo) > Math.abs(bestCombo)) {
                                bestCombo = compCombo;
                            }
                            // check if this is the best combo for the spot on the grid
                            if (playerCombo > Math.abs(compCombo)) {
                                bestCombos[i][j][k] = playerCombo;
                            } else if (Math.abs(compCombo) > playerCombo) {
                                bestCombos[i][j][k] = compCombo;
                            }
                            if (playerCombo == Math.abs(compCombo)) {
                                sameCombo = playerCombo;

                            }

                            compCopy[i][j][k] = 0;
                            playerCopy[i][j][k] = 0;

                        }
                    }

                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    System.out.print(bestCombos[i][k][j]);
                }
                System.out.println("");
            }
            System.out.println("");
        }

        // priority on the best move based on turn priority (if computer or player places first)
        // only if both moves are the same
        if (sameCombo == Math.abs(bestCombo) && (sameCombo > 3)) {
            if ((turn == -1) && (bestCombo > 0)) {
                bestCombo *= (-1);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                for (int k = 0; k < size; k++) {
                    if (bestCombos[i][j][k] == bestCombo && !placed) {
                        placed = true;
                        coordinate[0] = k;
                        coordinate[1] = grid.length-j-1;
                    }

                }
            }
        }
        return coordinate;
    }


    /**
     * Checks all possible fourteen locations around a move, gives a score based on the number of combinations present
     * @author Albert Quon
     * @param grid Current game state
     * @param row Current row
     * @param column Current column
     * @param height Current height
     * @param player Computer or player
     * @return Highest combination found for a certain move
     */
    static int checkCombos(int[][][] grid, int row, int column, int height, int player) {
        int combo = 0; // highest combo for the player
        int tempCombo; // stores the return value of the methods
        //determine if any diagonal combinations can be valid by finding the length of diagonals
        int diagLengthLeftRight = diagonalLengthLR(grid.length, row, column);
        System.out.println(diagLengthLeftRight + "LR" + height + ";" + row + ";" + column);
        int diagLengthRightLeft = diagonalLengthRL(grid.length, row, column);
        System.out.println(diagLengthRightLeft + "RL" + height + ";" + row + ";" + column);
        int diagHeightLeftRightRow = diagonalHeightRow(grid.length, row, height);
        int diagHeightRightLeftRow = diagonalHeightRowOpp(grid.length, row, height);
        int diagHeightLeftRightCol = diagonalHeightCol(grid.length, column, height);
        int diagHeightRightLeftCol = diagonalHeightColOpp(grid.length, column, height);

        // checks all directions on the same plane/height
        // methods are grouped by complimentary/opposite directions
        //entire column
        tempCombo = left(grid, row, column, height, 0, player);
        tempCombo = right(grid, row, column, height, tempCombo, player);
        if (Math.abs(tempCombo) > Math.abs(combo)) {
            combo = tempCombo;
        }

        //entire row
        tempCombo = up(grid, row, column, height,0, player);
        tempCombo = down(grid, row, column, height, tempCombo, player);
        if (tempCombo > combo) {
            combo = tempCombo;
        }
        // diagonals will be considered if diagonal wins are possible
        if (diagLengthLeftRight > 3) {

            tempCombo = upLeft(grid, row, column, height, 0, player);
            tempCombo = downRight(grid, row, column, height, tempCombo, player);
            if (tempCombo > combo) {
                combo = tempCombo;
            }

            tempCombo = aboveFrontLeft(grid, row, column, height, 0, player);
            tempCombo = belowBehindRight(grid, row, column, height, tempCombo, player);
            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }

            tempCombo = aboveBehindLeft(grid, row, column, height, 0, player);
            tempCombo = belowFrontRight(grid, row, column, height, tempCombo, player);

            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }
        }
        // diagonals will be considered if diagonal wins are possible
        if (diagLengthRightLeft > 3) {
            tempCombo = upRight(grid, row, column, height, 0, player);
            tempCombo = downLeft(grid, row, column, height, tempCombo, player);
            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }
            tempCombo = aboveFrontRight(grid, row, column, height, 0, player);
            tempCombo = belowBehindLeft(grid, row, column, height, tempCombo, player);

            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }
            tempCombo = aboveBehindRight(grid, row, column, height, 0, player);
            tempCombo = belowFrontLeft(grid, row, column, height, tempCombo, player);

            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }
            System.out.println(combo + "c");

        }

        //checks all directions that vary in height
        tempCombo = above(grid, row, column, height, 0, player);
        tempCombo = below(grid, row, column, height, tempCombo, player);
        
        if (Math.abs(tempCombo) > Math.abs(combo)) {
            combo = tempCombo;
        }

        // checks from increasing row and height, diagonally with decreasing row and height
        if (diagHeightLeftRightRow > 3) {
            tempCombo = aboveFront(grid, row, column, height, 0, player);
            tempCombo = belowBehind(grid, row, column, height, tempCombo, player);
            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }
        }
        // checks from increasing row and decreasing height, diagonally with decreasing row and increasing height
        if (diagHeightRightLeftRow > 3) {
            tempCombo = aboveBehind(grid, row, column, height, 0, player);
            tempCombo = belowFront(grid, row, column, height, tempCombo, player);

            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }
        }
        // checks from increasing columns and decreasing height, diagonally with decreasing columns and increasing height
        if (diagHeightRightLeftCol > 3) {
            tempCombo = aboveLeft(grid, row, column, height, 0, player);
            tempCombo = belowRight(grid, row, column, height, tempCombo, player);

            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }
        }
        // checks from increasing columns and height, diagonally with decreasing columns and height
        if (diagHeightLeftRightCol > 3) {
            tempCombo = aboveRight(grid, row, column, height, 0, player);
            tempCombo = belowLeft(grid, row, column, height, tempCombo, player);

            if (Math.abs(tempCombo) > Math.abs(combo)) {
                combo = tempCombo;
            }
        }

        //highest combination has been found, subtract the extra value from the initial spot
        return combo-1;

    }

    /**
     * Checks for the diagonal length from top right to bottom left from a specific point
     * @author Albert Quon
     * @param length The maximum length of the board
     * @param row The current row
     * @param column The current column
     * @return The total length of the diagonal
     */
    static int diagonalLengthRL(int length, int row, int column) {
        int diagonalLength = 1; // add initial point
        int tempRow = row;
        int tempCol = column;
        // split into two directions from a single point (diagonally)
        while ((tempRow != 0) && (tempCol != length)) {
            diagonalLength++;
            tempRow--;
            tempCol++;
        }
        while ((column != 0) && (row != length)) {
            diagonalLength++;
            row++;
            column--;
        }
        return diagonalLength;
    }

    /**
     * Checks for the diagonal length from top left to bottom right on a specific point
     * @author Albert Quon
     * @param length The maximum length of the board
     * @param row The current row
     * @param column The current column
     * @return The total length of the diagonal
     */
    static int diagonalLengthLR(int length, int row, int column) {
        int diagonalLength = 0;
        int tempRow = row;
        int tempCol = column;
        // split into two directions from a single point (diagonally)
        while ((tempRow != length) && (tempCol != length)) {
            diagonalLength++;
            tempRow++;
            tempCol++;

        }

        while ((column != 0) && (row != 0)) {
            diagonalLength++;
            row--;
            column--;

        }
        return diagonalLength;
    }

    /**
     * Checks from top left to bottom right through heights and rows
     * @author Albert Quon
     * @param length The maximum length of the board
     * @param row The current row
     * @param height The current height
     * @return The length of the diagonal
     */
    static int diagonalHeightRow(int length, int row, int height) {
        int diagonalLength = 0;
        int tempRow = row;
        int tempHeight = height;
        // split into two directions from a single point (diagonally)
        while ((tempRow != length) && (tempHeight != length)) {
            diagonalLength++;
            tempRow++;
            tempHeight++;

        }
        while ((height != 0) && (row != 0)) {
            diagonalLength++;
            row--;
            height--;

        }
        return diagonalLength;
    }

    /**
     * Checks from top right to bottom left through heights and rows
     * @author Albert Quon
     * @param length The maximum length of the board
     * @param row The current row
     * @param height The current height
     * @return The length of the diagonal
     */
    static int diagonalHeightRowOpp(int length, int row, int height) {
        int diagonalLength = 1; // initial move
        int tempRow = row;
        int tempHeight = height;
        // split into two directions from a single point (diagonally)
        while ((tempRow != 0) && (tempHeight != length)) {
            diagonalLength++;
            tempRow--;
            tempHeight++;

        }
        while ((height != length) && (row != 0)) {
            diagonalLength++;
            row--;
            height++;

        }
        return diagonalLength;
    }

    /**
     * Checks the diagonal height from top left to bottom right diagonally through columns
     * @author Albert Quon
     * @param length The maximum length of the board
     * @param column The current column
     * @param height The current height
     * @return The length of the diagonal
     */
    static int diagonalHeightCol(int length,int column, int height) {
        int diagonalLength = 0;
        int tempCol = column;
        int tempHeight = height;

        // split into two directions from a single point (diagonally)
        while ((tempCol != length) && (tempHeight != length)) {
            diagonalLength++;
            tempCol--;
            tempHeight++;

        }
        while ((height != 0) && (column != 0)) {
            diagonalLength++;
            column--;
            height++;

        }
        return diagonalLength;
    }

    /**
     * Checks the diagonal height from top right to bottom left diagonally through columns
     * @author Albert Quon
     * @param length The maximum length of the board
     * @param column The current column
     * @param height The current height
     * @return The length of the diagonal
     */
    static int diagonalHeightColOpp(int length, int column, int height) {
        int diagonalLength = 1; // initial move
        int tempCol = column;
        int tempHeight = height;

        // split into two directions from a single point (diagonally)
        while ((tempCol != 0) && (tempHeight != length)) {
            diagonalLength++;
            tempCol--;
            tempHeight++;

        }
        while ((height != length) && (column != 0)) {
            diagonalLength++;
            column--;
            height++;

        }
        return diagonalLength;
    }


    //********************************END OF WIN CHECKING METHODS AND BEST MOVE METHODS*****************************






    // RECURSIVE HELPER METHODS
    //************************************* START OF WIN CHECKING RECURSIVE METHODS********************************


    /**
     * Determines if there's a four in a row in a specific row through recursion
     * @author Albert Quon
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
     * @author Albert Quon
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
     * @author Albert Quon
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
     * Determines if there's a four in a row in a diagonal on the same height through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in a diagonally left on the same height through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in a through increasing height and rows through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in increasing heights and columns through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in through increasing heights diagonally through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in diagonally left in increasing heights through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in decreasing heights and increasing rows through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in decreasing heights and increasing columns through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in diagonally right in decreasing heights through recursion
     * @author Albert Quon
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
     * Determines if there's a four in a row in diagonally left in decreasing heights through recursion
     * @author Albert Quon
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
    // Row: [UP(-1), DOWN(+1)] Column: [LEFT(-1), RIGHT(+1)] Diagonals: upRight, upLeft, downLeft, downRight
    /**
     * Recursively checks for combinations through decreasing rows
     * @author Albert Quon
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int up(int[][][] grid, int row, int column, int height, int combo, int playerType) {

        if (row < 0) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return up(grid, row-1, column, height, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations through increasing rows
     * @author Albert Quon
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int down(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if (row >= grid.length) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return down(grid, row+1, column, height, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations through decreasing columns
     * @author Albert Quon
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int left(int[][][] grid, int row, int column, int height, int combo, int playerType) {

        if (column < 0) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return left(grid, row, column-1, height, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations through increasing columns
     * @author Albert Quon
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int right(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if (column == grid.length) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {

            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return right(grid, row, column+1, height, combo+1, playerType);
    }
    
    /**
     * Recursively checks for combinations through diagonally in decreasing rows and columns
     * @author Albert Quon
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int upLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((column < 0) || (row < 0)){
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return upLeft(grid, row-1, column-1, height, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations through diagonally with increasing columns and decreasing rows
     * @author Albert Quon
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int upRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((column == grid.length) || (row < 0)){
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return upRight(grid, row-1, column+1, height, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through increasing rows and decreasing columns
     * @author Albert Quon
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int downLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((column < 0) || (row == grid.length)){
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return downLeft(grid, row+1, column-1, height, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations through diagonally through increasing rows and increasing columns
     * @author Albert Quon
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int downRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((column == grid.length) || (row == grid.length)){
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return downRight(grid, row+1, column+1, height, combo+1, playerType);
    }

    // END OF SAME METHODS ON SAME HEIGHT





    // START OF METHODS THAT CHECK VARYING HEIGHTS
    // Height: [Above = +1, Down = -1], Rows: [Front = -1, Behind = +1], Columns: [Left = -1, Right = +1]
    /**
     * Recursively checks for combinations through increasing height above the move
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int above(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if (height == grid.length) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return above(grid, row, column, height+1, combo+1, playerType);
    }
    
    /**
     * Recursively checks for combinations diagonally through increasing height and decreasing rows
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int aboveFront(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return aboveFront(grid, row-1, column, height+1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through increasing height and rows
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int aboveBehind(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row == grid.length)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return aboveBehind(grid, row+1, column, height+1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through increasing heights and decreasing columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int aboveLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (column < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return aboveLeft(grid, row, column-1, height+1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through increasing height and columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int aboveRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (column == grid.length)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return aboveRight(grid, row, column+1, height+1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through increasing height and decreasing rows and columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int aboveFrontLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row < 0) || (column < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return aboveFrontLeft(grid, row-1, column-1, height+1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through increasing heights and columns and decreasing rows
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int aboveFrontRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row < 0) || (column == grid.length)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return aboveFrontRight(grid, row-1, column+1, height+1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through increasing height and rows and decreasing columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int aboveBehindLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row == grid.length) || (column < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return aboveBehindLeft(grid, row+1, column-1, height+1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through increasing heights, rows, and columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int aboveBehindRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height == grid.length) || (row == grid.length) || (column == grid.length)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return aboveBehindRight(grid, row+1, column+1, height+1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations through decreasing height 
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int below(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            }
            return combo*playerType;
        }
        return below(grid, row, column, height-1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through decreasing height and rows
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int belowFront(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            }
            return combo*playerType;
        }
        return belowFront(grid, row-1, column, height-1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through decreasing height and increasing rows
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int belowBehind(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row == grid.length)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return belowBehind(grid, row+1, column, height-1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through decreasing height and columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int belowLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (column < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return belowLeft(grid, row, column-1, height-1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through decreasing height and increasing columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int belowRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (column == grid.length)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return belowRight(grid, row, column+1, height-1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through decreasing height, rows, and columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */

    static int belowFrontLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row < 0) || (column < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return belowFrontLeft(grid, row-1, column-1, height-1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through decreasing height and rows and decreasing columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int belowFrontRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row < 0) || (column == grid.length)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return belowFrontRight(grid, row-1, column+1, height-1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through decreasing height and columns and increasing rows
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int belowBehindLeft(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row == grid.length) || (column < 0)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return belowBehindLeft(grid, row+1, column-1, height-1, combo+1, playerType);
    }

    /**
     * Recursively checks for combinations diagonally through decreasing height and increasing rows and columns
     * @param grid Current Game State
     * @param row The current row
     * @param column  The current column
     * @param height The current height
     * @param combo Current combination
     * @param playerType Player combination to specifically search for
     * @return The score of the move and highest combination found
     */
    static int belowBehindRight(int[][][] grid, int row, int column, int height, int combo, int playerType) {
        if ((height < 0) || (row == grid.length) || (column == grid.length)) {
            return combo*playerType;
        }
        if (grid[height][row][column] != playerType) {
            if (playerType == playerType*(-1)) {
                combo = 0; // combo will not be considered since it's not possible for a win
            } 
            return combo*playerType;
        }
        return belowBehindRight(grid, row+1, column+1, height-1, combo+1, playerType);
    }





}

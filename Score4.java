/* [GridTest.java]
 * A program to demonstrate usage of DisplayGrid.java.
 * @author Mangat
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
                if ((boardSizeInput.charAt(i) < '0') || (boardSizeInput.charAt(i) > '9')) {
                    validSize = false;
                }
            }
            if (!validSize) {
                boardSizeInput = JOptionPane.showInputDialog("Please enter a valid board size:");
            }
        } while (!validSize);
        boardSize = Integer.parseInt(boardSizeInput);
        //int boardSize = Integer.parseInt(boardSizeInput);

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

        // Initialize Map
        //moveItemsOnGrid(map);

        // display the fake grid on Console
        //DisplayGridOnConsole(map);

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

            //Small delay
            /*try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }*/
            ;


            // Initialize Map (Making changes to map)
            //moveItemsOnGrid(map);

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
    //method to display grid a text for debugging
    public static void DisplayGridOnConsole(String[][] map) {
        for(int i = 0; i<map.length;i++){
            for(int j = 0; j<map[0].length;j++)
                System.out.print(map[i][j]+" ");
            System.out.println("");
        }
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
    static void findValues(int[][][] grid) {


    }

    // record the number of possibilities to win
    // alternate between player A and B (A prefers highest, B prefers lowest)
    // return the optimal move
    static void turn (int[][][] grid, int currentPlayer, int[] gridIndex, int score) {


    }
    // go through each row
    // go through each column
    // go through height
    // ex. [0][0][0] = [0][0][1]
    // go through diagonally on the same plane
    // go through diagonally on each separate individual plane
    // ex. [0][0][0] == [1][1][1] or [0][0][0] == [1][0][1] or [0][0][0] == [0][1][1]
    static int win(int[][][] grid) {
        int checkC, checkH, checkR, checkDiag,
                checkDiagRowUp, checkDiagColUp,
                checkDiagRowDown, checkDiagColDown,
                checkFloorsUp, checkFloorsDown, checkWin;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                for (int k = 0; k < grid.length; k++) {
                    checkR = winCheck(grid, j, k, i, 0, 0);
                    checkC = winCheck(grid, j, k, i, 0, 1);
                    checkH = winCheck(grid, j, k, i, 0, 2);
                    checkDiag = winCheck(grid, j, k, i, 0, 3);
                    checkDiagRowUp = winCheck(grid, j, k, i, 0, 4);
                    checkDiagColUp = winCheck(grid, j, k, i, 0, 5);
                    checkFloorsUp = winCheck(grid, j, k, i, 0, 6);

                    if((checkR == 1) || (checkR == -1)) {
                        return checkR;
                    }
                    if((checkC == 1) || (checkC == -1)) {
                        return checkC;
                    }
                    if((checkH == 1) || (checkH == -1)) {
                        return checkH;
                    }
                    if((checkDiag == 1) || (checkDiag == -1)) {
                        return checkDiag;
                    }
                    if((checkFloorsUp == 1) || (checkFloorsUp == -1)) {
                        return checkFloorsUp;
                    }
                    if((checkDiagRowUp == 1) || (checkDiagRowUp == -1)) {
                        return checkDiagRowUp;
                    }
                    if((checkDiagColUp == 1) || (checkDiagColUp == -1)) {
                        return checkDiagColUp;
                    }
                    if (i >= 3) {

                        checkDiagRowDown = winCheckDown(grid, j, k, i, 0, 0);
                        checkDiagColDown = winCheckDown(grid, j, k, i, 0, 1);
                        checkFloorsDown = winCheckDown(grid, j, k, i, 0, 2);
                        if ((checkFloorsDown == 1) || (checkFloorsDown == -1)) {
                            return checkFloorsDown;
                        }
                        if ((checkDiagColDown == 1) || (checkDiagColDown == -1)) {
                            return checkDiagColDown;
                        }
                        if ((checkDiagRowDown == 1) || (checkDiagRowDown == -1)) {
                            return checkDiagRowDown;
                        }
                    }
                    //checkWin = checkR + checkC +

                }
            }
        }

        return 0;
    }



    /**
     * Checks all win conditions and see if it's valid for any player
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param target
     * @param type Determines where the method should search
     * @return
     */
    static int winCheck(int[][][] grid, int row, int column, int height, int target, int type) {
        if ((target == 6) || (target == -6)) {
            return target/4;
        }
        if ((row == grid.length-1) || (column == grid.length-1) || (height == grid.length-1)) {
            return 0;
        }
        // check row
        if ((grid[height][row][column] == grid[height][row+1][column]) && (grid[height][row][column] != 0)
                && (type == 0)) {
            return (winCheck(grid, row+1, column, height,
                    target+grid[height][row][column]+grid[height][row+1][column], 0) +
                    winCheck(grid, row+1, column, height, target, 0));
        }
        // check column
        if ((grid[height][row][column] == grid[height][row][column+1]) && (grid[height][row][column] != 0)
                && (type == 1)) {
            return (winCheck(grid, row, column+1, height,
                    target+grid[height][row][column]+grid[height][row][column+1], 1) +
                    winCheck(grid, row, column+1, height, target, 1));
        }
        // check height
        if ((grid[height][row][column] == grid[height+1][row][column]) && (grid[height][row][column] != 0)
                && (type == 2)) {
            return (winCheck(grid, row, column, height+1,
                    target+grid[height][row][column]+grid[height+1][row][column], 2) +
                    winCheck(grid, row, column, height+1, target, 2));
        }
        // check diagonal on the same plane
        if ((grid[height][row][column] == grid[height][row+1][column+1]) && (grid[height][row][column] != 0)
                && (type == 3)) {
            return (winCheck(grid, row+1, column+1, height,
                    target+grid[height][row][column]+grid[height][row+1][column+1], 3) +
                    winCheck(grid, row+1, column+1, height, target, 3));
        }
        // check diagonal heights on same column
        if ((grid[height][row][column] == grid[height+1][row+1][column]) && (grid[height][row][column] != 0)
                && (type == 4)) {
            return (winCheck(grid, row+1, column, height+1,
                    target+grid[height][row][column]+grid[height+1][row+1][column], 4) +
                    winCheck(grid, row+1, column, height+1, target, 4));
        }
        // check diagonal heights on the same row
        if ((grid[height][row][column] == grid[height+1][row][column+1]) && (grid[height][row][column] != 0)
                && (type == 5)) {
            return (winCheck(grid, row, column, height+1,
                    target+grid[height][row][column]+grid[height+1][row][column+1], 5) +
                    winCheck(grid, row+1, column, height+1, target, 5));
        }
        // check diagonal on increasing floors across
        if ((grid[height][row][column] == grid[height+1][row+1][column+1]) && (grid[height][row][column] != 0)
                && (type == 6)) {
            return (winCheck(grid, row+1, column+1, height+1,
                    target+grid[height][row][column]+grid[height+1][row+1][column+1], 6) +
                    winCheck(grid, row+1, column+1, height+1, target, 6));
        }

        return 0;

    }

    /**
     * Fix this method
     * @param grid
     * @param row
     * @param column
     * @param height
     * @param target
     * @param type
     * @return
     */
    static int winCheckDown(int[][][] grid, int row, int column, int height, int target, int type) {
        System.out.println(target);
        if ((target == 6) || (target == -6)) {
            return target/4;
        }
        if ((row == 0) || (column == 0) || (height == 0)) {
            return 0;
        }
        //check diagonally downwards on same row
        if ((grid[height][row][column] == grid[height-1][row][column-1]) && (grid[height][row][column] != 0)
                && (type == 0)) {
            return (winCheck(grid, row, column-1, height-1,
                    target+grid[height][row][column]+grid[height-1][row][column-1], 0) +
                    winCheck(grid, row, column-1, height-1, target, 0));
        }
        //check diagonally downwards on same column
        if ((grid[height][row][column] == grid[height-1][row-1][column]) && (grid[height][row][column] != 0)
                && (type == 1)) {
            return (winCheck(grid, row-1, column, height-1,
                    target+grid[height][row][column]+grid[height-1][row-1][column], 1) +
                    winCheck(grid, row-1, column, height-1, target, 1));
        }
        //check diagonally downwards across
        if ((grid[height][row][column] == grid[height-1][row-1][column-1]) && (grid[height][row][column] != 0)
                && (type == 0)) {
            return (winCheck(grid, row-1, column-1, height-1,
                    target+grid[height][row][column]+grid[height-1][row-1][column-1], 2) +
                    winCheck(grid, row-1, column-1, height-1, target, 2));
        }
        return 0;
    }
}
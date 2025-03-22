import java.util.Scanner;

// This class makes a Connect Four game. Connect Four is a game that is played by 2 players, where
// they drop tokens, hoping to get 4 in a row, either vertically, horizontally, or diagonally.
public class ConnectFour extends AbstractStrategyGame{
    public static final char PLAYER_1_TOKEN = '#';
    public static final char PLAYER_2_TOKEN = '$';
    public static final int PLAYER_1 = 1;
    public static final int PLAYER_2 = 2;
    public static final int TIE = 0;
    public static final int GAME_IS_OVER = -1;
    public static final int GAME_NOT_OVER = -1;

    private char[][] board;
    private boolean isXTurn;

    // Behavior: 
    //   - This method constructs a Connect Four game
    public ConnectFour(){
        board = new char[7][6];
        for(int row = 0; row < board.length; row++){
            for(int col = 0; col < board[0].length; col++){
                board[row][col] = '0';
            }
        }
        isXTurn = true;
    }

    // Behavior: 
    //   - This method displays the instructions of Connect Four to the user
    // Returns:
    //   - String: the instructions of how to play Connect Four  
    public String instructions(){
        String result = "";
        result += "Player 1 is # and Player 2 is $. Choose where to play by entering the number\n";
        result += "of the column (1-6 left to right). Spaces shown as a 0 are empty.\n";
        result += "The game ends when a player marks four spaces in a row, column, or diagonal.\n";
        result += "This player wins, or if the board is full, the game ends in a tie.";
        return result;
    }

    // Behavior: 
    //   - This method displays the game board to the user, with empty spaces being represented as
    //   - 0s
    // Returns:
    //   - String: the game board
    public String toString(){
        String result = "";
        for (int i = 0; i < board.length; i++) {
            result += "|";
            for (int j = 0; j < board[0].length; j++) {
                result += " " + board[i][j] + " |";
            }
            result += "\n";
        }
        return result;
    }

    // Behavior: 
    //   - This method calculates who is the winner of the game, or if a tie has occurred, or if no
    //   - one has won. The winner can be decided by who has 4 tokens in a row horizontally,
    //   - vertically, or diagonally.
    // Returns:
    //   - int: the corresponding number to the player who has won (1 for player 1 and 2 for player
    //   - 2). If a tie has occurred, 0 is returned. If the game isn't over, -1 is returned. 
    public int getWinner() {
        for (int i = 0; i < board.length; i++) {
            int rowWinner = getRowWinner(i);
            if (rowWinner != GAME_NOT_OVER) {
                return rowWinner;
            }
            if(i != board[0].length){
                int colWinner = getColWinner(i);
                if (colWinner != GAME_NOT_OVER) {
                    return colWinner;
                }
            }
        }

        int diagWinner = getDiagWinner();
        if (diagWinner != GAME_NOT_OVER) return diagWinner;

        return checkTie();
    }

    // Behavior: 
    //   - This method checks to see if 4 tokens of one player are on the board in a row
    // Parameters:
    //   - row: the row to be checked
    // Returns:
    //   - int: the number for the corresponding player who has won if they indeed have 4 tokens in
    //   - a row horizontally. If they haven't won, -1 is returned, signaling the game isn't over.
    private int getRowWinner(int row) {
        int left = 0;
        int right = 1;
        int count = 1;

        while(right < board[0].length){
            if(board[row][left] == board[row][right]){
                right++;
                count++;
                if(count == 4){
                    return getPlayer(board[row][left]);
                }
            }
            else{
                left = right;
                right = left + 1;
                count = 1;
            }
        }

        return GAME_NOT_OVER;
    }

    // Behavior: 
    //   - This method calculates if a player has 4 tokens in a row vertically
    // Parameters:
    //   - col: the column being checked
    // Returns:
    //   - int: the number for the corresponding player who has won if they indeed have 4 tokens in
    //   - a row vertically. If they haven't won, -1 is returned, signaling the game isn't over.
    private int getColWinner(int col) {
        int bottom = board.length - 1;
        int top = board.length - 2;
        int count = 1;

        while (top >= 0) {
            if (board[bottom][col] == board[top][col]) {
                count++;
                if (count == 4) {
                    return getPlayer(board[bottom][col]);
                }
            } else {
                bottom = top;
                count = 1;
            }
            top--;
        }

        return GAME_NOT_OVER;
    }

    // Behavior: 
    //   - This method calculates to see if a player has 4 tokens in a row diagonally.
    // Returns:
    //   - int: the number for the corresponding player who has won if they indeed have 4 tokens in
    //   - a row diagonally. If they haven't won, -1 is returned, signaling the game isn't over. 
    private int getDiagWinner() {
        // Checks if a diagonal of 4 in a row exists going from left to right
        for(int row = board.length - 1; row >= (board.length / 2); row--){
            for(int col = 0; col < (board[0].length / 2); col++){
                if(board[row][col] != '0'){
                    if(board[row][col] == board[row-1][col+1] && board[row-1][col+1] == 
                        board[row-2][col+2] && board[row-2][col+2] == board[row-3][col+3]){
                            return getPlayer(board[row][col]);
                        }
                }
            }
        }

        // Checks if a diagonal of 4 in a row exists going from right to left
        for(int row = board.length - 1; row >= (board.length / 2) - 1; row--){
            for(int col = board[0].length - 1; col >= board[0].length / 2; col--){
                if(board[row][col] != '0'){
                    if(board[row][col] == board[row-1][col-1] && board[row-1][col-1] == 
                        board[row-2][col-2] && board[row-2][col-2] == board[row-3][col-3]){
                            return getPlayer(board[row][col]);
                        }
                }
            }
        }

        return GAME_NOT_OVER;
    }

    // Behavior: 
    //   - This method calculates to see if a tie has occurred.
    // Returns:
    //   - int: 0 signaling a tie has occurred. If a tie hasn't occurred, then -1 is returned,
    //   - signaling the game isn't over
    private int checkTie() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                if (isEmpty(i, j)) {
                    return GAME_NOT_OVER;
                }
            }
        }

        // it's a tie!
        return TIE;
    }

    // Behavior: 
    //   - This method figures out which player is associated with which token
    // Parameters:
    //   - token: either players token ($ or #)
    // Returns:
    //   - int: the number of the player associated with the token
    private int getPlayer(char token) {
        if (token == PLAYER_1_TOKEN) {
            return PLAYER_1;
        } else if (token == PLAYER_2_TOKEN) {
            return PLAYER_2;
        }

        return GAME_NOT_OVER;
    }

    // Behavior: 
    //   - This method calculates which player's turn is next. Before doing that however, it checks
    //   - to see if the game is over or not.
    // Returns:
    //   - int: the number of the player who's turn is next. If the game is over, then -1 is
    //   - returned.
    public int getNextPlayer(){
        if (isGameOver()) {
            return GAME_IS_OVER;
        }
        
        return isXTurn ? PLAYER_1 : PLAYER_2;
    }

    // Behavior: 
    //   - This method allows the players to make their move, putting their token in an available
    //   - spot in the column
    // Parameters:
    //   - input: the column the user wants to play their token into
    // Exceptions:
    //   - if the given input is null, an IllegalArgumentException is thrown. If the column number
    //   - the user gives is less than 0 or greater than the length of the column of the board,
    //   - an IllegalArgumentException is also thrown. If there isn't any empty space in the column
    //   - available, an IllegalArgumentException is also thrown. A lot of exceptions!
    public void makeMove(Scanner input) {
        if (input == null) {
            throw new IllegalArgumentException();
        }
        char currToken = isXTurn ? PLAYER_1_TOKEN : PLAYER_2_TOKEN;

        System.out.print("Column? ");
        int column = input.nextInt();

        makeMove(column, currToken);
        isXTurn = !isXTurn;
    }

    // Behavior: 
    //   - This method is a helper method for the makeMove method. It checks to see if there is an
    //   - available row to in the specified column to place the player's token, updating the board
    //   - with the token in the lowest possible row if there is an available row, simulating a 
    //   - connect four experience.
    // Parameters:
    //   - column: the column the player wants to play the token
    //   - token: the token of the respective player
    // Exceptions:
    //   - income < 0: if the given income is negative, an IllegalArgumentException is thrown. 
    private void makeMove(int column, char token) {
        int row = -1;
        int col = column - 1;
        if (col < 0 || col >= board[0].length) {
            throw new IllegalArgumentException("Invalid column position: " + column);
        }
        for(int i = 0; i < board.length; i++){
                if(isEmpty(i, col)){
                    row = i;
                }
        }
        if(row == -1){
            throw new IllegalArgumentException("Column is full");
        }
        else{
            board[row][col] = token;
        }
        
    }

    // Behavior: 
    //   - This method calculates to see if a spot on the board is empty or not
    // Parameters:
    //   - row: the row of the spot
    //   - column: the column of the spot
    // Returns:
    //   - boolean: true if this spot is empty, false otherwise
    private boolean isEmpty(int row, int col) {
        return board[row][col] != PLAYER_1_TOKEN && board[row][col] != PLAYER_2_TOKEN;
    }
}

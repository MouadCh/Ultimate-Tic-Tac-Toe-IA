package Algorithms;

import TicTacToe.TicTacFrame;
import java.util.*;


class Position{ // Saving Position of the Board
    int board_num;	
    int cell_num;
	
	public Position(int board_num, int cell_num)//starting board
	{
		this.cell_num = cell_num;
		this.board_num = board_num;
	}
}

public class IA_Algorithme {//board -> board_num (1-9), cell_num (1-9)
	/*
		       || 		    ||
	-----------||-----------||------------
	 1 | 2 | 3 || 1 | 2 | 3 || 1 | 2 | 3
	---|---|---||---|---|---||---|---|----
	 4 | 5 | 6 || 4 | 5 | 6 || 4 | 5 | 6
	---|---|---||---|---|---||---|---|----
	 7 | 8 | 9 || 7 | 8 | 9 || 7 | 8 | 9
	-----------||-----------||------------
	-----------||-----------||------------
	 1 | 2 | 3 || 1 | 2 | 3 || 1 | 2 | 3
	---|---|---||---|---|---||---|---|----
	 4 | 5 | 6 || 4 | 5 | 6 || 4 | 5 | 6
	---|---|---||---|---|---||---|---|----
	 7 | 8 | 9 || 7 | 8 | 9 || 7 | 8 | 9
	-----------||-----------||------------
	-----------||-----------||------------
	 1 | 2 | 3 || 1 | 2 | 3 || 1 | 2 | 3
	---|---|---||---|---|---||---|---|----
	 4 | 5 | 6 || 4 | 5 | 6 || 4 | 5 | 6
	---|---|---||---|---|---||---|---|----
	 7 | 8 | 9 || 7 | 8 | 9 || 7 | 8 | 9
			   ||		    ||
	*/
	Player user;
	Player computer;
	private Position comP = new Position(0,1);

        private String[][] board = new String[9][9];
	private int current_board = -1;//initialize to -1 to signify the beginning of the game
        
        private int cell_num, board_num,     LastidButt, LastidPan ;
         
        public static int idButt , idPan;
        
	public IA_Algorithme(){
		Tac();
	}

	/*

		exec_move
		- executes move for the user
		- asks for user input
		- validates user input
			- checks if input is in an empty space
			- checks if input is in valid board
			- checks if input is number between 1-9
		- fills board if the user input is valid
	*/
       public void setBoard_num(int idPanel){
           this.board_num = idPanel;
       }
       public void setCell_num(int idButton){
           this.cell_num=idButton;
       }
	
       public int exec_move(){

//		Scanner scan = new Scanner(System.in); ///////// Funct GetMoveIndice()
		System.err.println("It's Your Turn! Enter board number, cell number [1-9]");
		while(true){
			try{
				if (current_board != -1) System.err.println("Current board is: " + (current_board+1));
//				board_num = scan.nextInt();
//				cell_num = scan.nextInt();	
				if (board_num >9 || board_num <1 || cell_num >9 || cell_num < 1) System.err.println("Please enter two valid integers between 1-9");
				else if( current_board != -1 && (board_num-1) != (current_board)) System.err.println("Invalid move. You input board " +board_num+ "Must use correct board: " + (current_board+1));
				else if (!checkPos(new Position(board_num-1, cell_num-1), user)) System.err.println("Invalid move. Spot already taken.");
				else break;
			}
			catch(Exception e){
				System.err.println("Please enter two valid integers between 1-9");
//				scan.nextLine();
			}
		}
		board_num -=1;
		cell_num -=1;//get index values
                 LastidButt = cell_num;
                 LastidPan =board_num;
		Position move = new Position(board_num, cell_num);
		fillBoard(move, user);
		if(current_board == -1) current_board = board_num;//if it's the first turn, change board_num
		int win_check = checkWin(user, current_board);
		
		current_board = cell_num;
		comP.board_num = current_board;//update computer's board_num to current board
		return win_check;

	}

	/*
		Tac is the framework for the game

	*/
	public void Tac(){
		current_board = -1;//reset
		setUsers();
		initiateBoard();
	}
               
	public void setUsers(){//takes in x or o and sets user/computer values
		user = new Player("X");
		computer = new Player("O");
	}
	public boolean userMove(){
		int win_check = exec_move();//executes move and also checks for a win for user
		if (win_check == 1) {
			System.err.println("You win!");
			return true;//return a win
		}
		return false;
	}
	public boolean computerMove(){
		int last_board =ComFillBoard(comP);
		printBoard();
		if(checkWin(computer, last_board)==1){
			System.err.println("You Lost");
			return true;
		}
		if(checkDraw()){
			System.err.println("DRAW! No more spaces to play in this board");
			return true;
		}
		return false;
	}
//        public void getPreviousMove(){
//            board[LastidButt][LastidPan]="";
//            this.user.value= "X".equals(this.user.value) ? "O" : "X";
//        }
	/*
		checkWin:
		- checks win for all valid winning configurations on a given board for a player
		
		Possible Wins:
			- 3 across
			- 3 down
			- diagonal
		
	*/
	public int checkWin(Player p, int board_num){ 
		
		int win = 0;
		//System.out.println("Checking board num: "+ board_num);
		
		for(int i = 0 ; i<=6 ; i+=3)
		{
		//check for 3 across
			//System.out.println("First Check: " + board[board_num][i]+board[board_num][i+1]+board[board_num][i+2]);
			if(p.value.equals(board[board_num][i])&&!board[board_num][i].equals(" ")&&board[board_num][i+1].equals(p.value)&&board[board_num][i+2].equals(p.value)){
				win = 1;
				break;
			}
			
		}
		for(int i=0; i<3; i++){//check for 3 down
			//System.out.println("Second Check: " + board[board_num][i]+board[board_num][i+3]+board[board_num][i+6]);
			if(p.value.equals(board[board_num][i])&&!board[board_num][i].equals(" ")&&board[board_num][i+3].equals(p.value)&&board[board_num][i+6].equals(p.value)){
				win = 1;
				break;
			}
		}
		//check diagonal upper-left to bottom-right
		//System.out.println("Checking Diagonal 1: " + board[board_num][0]+board[board_num][4] +board[board_num][8]);
		if(p.value.equals(board[board_num][0])&&!board[board_num][0].equals(" ")&&board[board_num][0].equals(board[board_num][4]) && board[board_num][0].equals(board[board_num][8]))
		{
			
			win = 1;
		}//check diagonal bottom-left to upper-right
		//System.out.println("Checking Diagonal 2: " + board[board_num][6]+board[board_num][4] +board[board_num][2]);
		if(p.value.equals(board[board_num][6])&&!board[board_num][6].equals(" ")&&board[board_num][6].equals(board[board_num][4]) && board[board_num][2].equals(board[board_num][6]))
		{
		
			win = 1;
		}
		return win;
	}

	/*
		checkDraw
		- separate from checkWin
		- checks if there are no spaces left in the current board

	*/
	public boolean checkDraw(){
		List<Position> emptyStates = emptySpace(current_board);//gets list of all empty states
		if(emptyStates.isEmpty()) return true; //the current board has no more spaces to play
		return false;
	}
	/*

		initiateBoard
		- fills in the board with empty spaces
	*/
	public void initiateBoard() // initiate Board
	{
		for(int i=0; i<9; i++) value_array[i] = 0;
		for(int i = 0; i<9; i++)
			for(int j = 0; j<9; j++)
					board[i][j] = " ";
	}
        
        public void MarkBoard(String mark,int idBut,int idPan){
            this.board[idBut][idPan] = mark;
        }
        
/*
	printBoard
	- prints 9board tic tac toe


*/
	public void printBoard() // Printing the board
	{
		System.err.println();
		for(int k=0; k<3; k++){
			for(int i = 0; i<3; i++){//cell_num..?
				int j;
				for(j=0; j<3; j++){//board_num
					int board_num = j+k*3;
					int cell_num = i*3;
					System.err.print(" " + board[board_num][cell_num] + " | " + board[board_num][cell_num+1]  + " | " + board[board_num][cell_num+2]);
					if(j != 2) System.err.print(" ||"); 
					else {
						System.err.println();
						System.err.println("---|---|---||---|---|---||---|---|----");
						
					}

				}
				if(j==2) System.err.print(" |");
			}
			if (k<2) {
				System.err.println("-----------||-----------||------------");
				System.err.println("-----------||-----------||------------");
			}
		}
		System.err.println();
	}
/*
		ComFillBoard -> fills board for the computer's move
		- stores and returns the last board
		- then calls minimax 
		- resets the "current board"
*/
	public int ComFillBoard(Position P){
		int last_board = current_board;//store the last board to check for a win
		scoreStates s = minimax(0, true, current_board, -1000, 1000);
		int value = s.position.cell_num;
		

		current_board = value;
		board[comP.board_num][value] = computer.value;
		/*Printing the computers move to the console*/
                idButt = value;
                idPan = last_board;
                
                LastidButt = value;
                LastidPan =last_board;
                
		System.out.println(last_board);
		System.out.println(value);
		return last_board;
	}
/*
	checkPos
	- checks board for a position/player to see if it is a valid move (e.g. empty)
*/
	public boolean checkPos(Position pos, Player p){
		// Check if board is already filled
		if(board[pos.board_num][pos.cell_num]== " ") return true;
		return false;
	}
/*
	fillBoard:
	- places a value in an empty space
	- checkPos is always called before fillBoard
*/
	public void fillBoard(Position pos, Player p){ // fillBoard depends on where player puts
		//System.out.println("Filling in space for " + p.value + ": " + pos.board_num + ", " + pos.cell_num);
		board[pos.board_num][pos.cell_num] = p.value;
	}
/*
	emptySpace()
	- takes in board number
	- checks for empty spaces in that board
	- returns a list of empty spaces for that board
*/
	List<Position> emptySp;
	public List<Position> emptySpace(int curr_board)// Make an list with an emptySpace
	{
		emptySp = new ArrayList<>();
		for(int i=0;i<9;i++)
			
			if(board[curr_board][i].equals(" "))
			{
				emptySp.add(new Position(curr_board, i));//TO-DO FIX
			}
		return emptySp;
		
	}
	
	/*
		setter for Computer Position
	*/
	public void setComp(Position p) 
	{
		comP=p;
		
	}
/*
	
	Minimax 
	- takes in depth, boolean for is maximizing, board number, alpha, and beta
	- returns scoreStates (tuple containing score and position)
	- uses minimax algorithm with alpha beta pruning
	- depth of 2 is used
	

	
	- alpha = value of the best (i.e. highest-value) choice so far for MAX
	- beta = value of the best (i.e. lowest-value) choice so far for MIN

		Score
			- one in a row. +1
			- two in a row. +100
			- three in a row. +1000
		Assuming:
			- isMaximizingPlayer == true -> computer
			- isMaximizingPlayer == false-> human
		
		//terminating depth of 2
		

*/
	public scoreStates minimax(int depth, boolean isMaximizingPlayer, int board_num, int alpha, int beta){
		
		int max = -99999;
                int min = 99999;
		int curr_val =0;
		int best_val;
		int best_cell = -1;
		List<Position> emptyStates = emptySpace(board_num);//gets list of all empty states

                if(depth == TicTacFrame.MACHINE_NIVEAU || emptyStates.isEmpty() || end()){
			best_val = heuristic(board_num, true);
			return new scoreStates(best_val, new Position(board_num, best_cell));
			
		}
		String move_value = (isMaximizingPlayer) ? computer.value : user.value;//gets the value of the move
                        //Print statement for depth
			for(int i=0; i<depth; i++){
				System.err.print("	");
			}
			if(isMaximizingPlayer){				
				for(Position state : emptyStates){
					board[state.board_num][state.cell_num] = move_value;
					int original_val = value_array[board_num];
					curr_val = heuristic(board_num, true) + minimax(depth+1, !isMaximizingPlayer, state.cell_num, alpha, beta).score;
					max = Math.max(curr_val, max);
					alpha = Math.max(curr_val, alpha);
					System.err.println("score is: " + curr_val + " on board "+ board_num+ " cell: " + state.cell_num);
					if (curr_val ==alpha){//if we took the curr_val, this is the best cell to take
						alpha = curr_val;
						best_cell = state.cell_num;
						
					}
					value_array[board_num]=original_val;
					board[board_num][state.cell_num]=" ";//undo move
					if (alpha >= beta) break;//pruning the tree
					
				}
			}else{//if it is not the maximizing player
				
				for(Position state : emptyStates){
					board[state.board_num][state.cell_num] = move_value;
					int original_val = value_array[board_num];
					curr_val = heuristic(board_num, false) + minimax(depth+1, !isMaximizingPlayer, state.cell_num, alpha, beta).score;

					System.err.println("score is: " + curr_val + " on board "+ board_num+ " cell: " + state.cell_num);
					min = Math.min(min, curr_val);
					beta = Math.min(curr_val, beta);
					if(curr_val== beta){//if we took the curr_val, this is the best cell to take

						beta = curr_val;
						best_cell = state.cell_num;
						
					}
					value_array[board_num]=original_val;
					board[board_num][state.cell_num]=" ";//undo move
					if (beta <= alpha) break;//pruning the tree
					
				}
			}
		best_val = (isMaximizingPlayer) ? alpha : beta;
		return new scoreStates(best_val, new Position(board_num, best_cell));
	}
	/*
		end
		- checks for a win 
		- if the computer wins, return end
		- called in minimax to see whether or not to terminate 

	*/
	public boolean end()
	{
		boolean end = false;
		for(int i =0; i<9 ; i++)
		{
			if(checkWin(computer,i)==1)
				return true;
			if(checkWin(user,i) ==1)
				return false;
		}
		return end;
	}
	/*
	- value_array[] is a global array containing the value of each board at a given state
	- this method prints 

	*/
	public void printValArray(){
		System.err.println("Printing value array");
		for(int i=0; i<9; i++){
			System.err.print(value_array[i] + " ");
		}
		System.err.println();
	}
	/*
Heuristic Function:
 - checks for each row and column and diagonal for how many x or o's in a row
 - each number corresponds to a winning configuration
 - uses HHelper function to value the move
	*/
    private int heuristic(int board_num, boolean isMaximizingPlayer) {
        int value = 0;

        value += HHelper(0, 4, 8, board_num);// diagonal UL - BR
        value += HHelper(6, 4, 2, board_num);// diagonal BL - UR

        //Down
        value += HHelper(1, 4, 7, board_num);  
        value += HHelper(0, 3, 6, board_num); 
        value += HHelper(2, 5, 8, board_num); 

        //Across
        value += HHelper(3, 4, 5, board_num);  
        value += HHelper(6, 7, 8, board_num);  
        value += HHelper(0, 1, 2, board_num );  

        //update value 
        int original_val = value_array[board_num];
        value_array[board_num] = value- original_val ;//gets the difference
        //returns positive if value is bigger, negative if value is smaller
        return value;
   }
    
    int[]value_array = new int[9];
/*
Helper function for heuristic function
	input: three cells
	output: score

	Score:
		- 1 in a row = 1 pt
		- 2 in a row = 100 pts
		- 3 in a row = 1000 pts
		(Computer is positive, User is negative)

	
*/
   private int HHelper(int cell1, int cell2, int cell3, int board_num) {
      int value = 0;
 	//created for clarity
      String [] temp_board = board[board_num];

      //checks for 1 in a row
      if (temp_board[cell1] == computer.value) value = 1;
      else if (temp_board[cell1] == user.value) value = -1;
      if (temp_board[cell2] == computer.value) {// checks for whether there is 1 in a row or 2 in a row so far
         if (value == 1)value = 10;
         else if (value == -1)return 0;
         else value = 1;
      } else if (temp_board[cell2] == user.value) {
         //prev cell is users
         if (value == -1) value = -10;
         //prev cell is computers
         else if (value == 1)return 0;
         //prev cell is empty
         else  value = -1;
      }
      if (temp_board[cell3] == computer.value) {//checks for three in a row
      	//computer gets 3 in a row
         if (value == 10) value *= 100;
         //check if it is o_o vs oxo o_o has 10 points, oxo is 0 pts
         else if(value == 1 && temp_board[cell1]==temp_board[cell3]) value = 10;
         else if(value == 1 && temp_board[cell1]!=temp_board[cell3]) value = 0;
         //not 3 in a row, and no possibility of getting 3 in a row here
         else if (value < 0)return 0;
         // cell1 and cell2 are empty
         else value = 1;
      } 
      else if (temp_board[cell3] == user.value) {
      	//user gets 3 in a row
         if (value ==-10) value *= 100;
         //check if it is x_x vs. oxx, oxx is useless
         if(value == -1 && temp_board[cell1] == user.value)value=10;
         //not 3 in a row, and no possibility of getting 3 in a row here
         else if (value > 1) return 0;
         else if(value==-1 && temp_board[cell1]!=user.value) value = 0;
         // user gets 1 in a row
         else value = -1;
      }

      return value;
   }


   /*
		scoreStates
		- tuple used for minimax algo
		- made up of score and position

   */
	class scoreStates{

		int score;
		Position position;
		scoreStates(int score, Position position){
			this.score = score;
			this.position = position;
		}

	}
	

	public static void main(String[]args){
		IA_Algorithme ttt = new IA_Algorithme();
		ttt.printBoard();
		ttt.initiateBoard();
		
	}
}

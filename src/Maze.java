

import java.util.ArrayList;
import java.util.Stack;
import java.util.Random;

public class Maze{
	private int r;
	private int size;

	private Cell noCell; // treated as 'null'
	private Cell beginCell; // where the maze starts

	private Cell endCell; // where the maze ends
	
	private char maze[][];

	private Random randomFriend;
	
	public Maze(int r, int seed){ // a deterministic maze
		this.r = r;
		initializeRandom(seed);
		size = r*2 + 1;
		maze = new char[size][size];
		noCell = new Cell(-1,-1);
	}

	public Maze(int r){ // a random maze
		this(r,-1);
	}
	
	private void initializeRandom(int seed){
		if(seed == -1)
			randomFriend = new Random();
		else 
			randomFriend = new Random(seed);
	}
	private double myRandom(){
		return randomFriend.nextDouble();
	}
	
	/**
	 * At the start of this function, it makes a maze with no room
	 * Two for loops
	 */
	
	private void initializeMaze(){

		for(int i = 0; i < size(); i ++){
			for(int j = 0; j < size(); j ++){
				
				if( isEven(i)){
					if(isEven(j))
						maze[i][j] = '+';
					else
						maze[i][j] = '-';
				}
				
				else{
					if( isEven(j) ) 
						maze[i][j] = '|'; 
					else maze[i][j] = ' ';
	    
				}
			}
		}
	
	}
	/*
	 * In this function, the function will start to knock down walls with a stack 
	 * It will return a maze
	 */
	public char[][] generateMaze(){
		return generateMaze(r);
	}
	public char[][] generateMaze(int r){     
		initializeMaze();
		Stack<Cell> stack = new Stack<Cell>();
		//add the start of the maze to the stack, it is random
		beginCell = new Cell(1, randomOdd(r)); 
     
		maze[beginCell.getRow()][beginCell.getColumn()] = 'x';
		maze[beginCell.getRow() - 1][beginCell.getColumn()] = ' ';
	
		int totalCell = r*r;          
		int beginCount = 1;
		
		Cell currentCell = beginCell;
            
		while(beginCount < totalCell){ // the condition
			Cell friend = this.getNeighborsRandom(currentCell);

			if(!friend.equals(noCell)){
				
				knockDownWall(friend,currentCell);
				currentCell = friend;
				addMark(currentCell); // this marks the cell with 'x', so my algorithm knows 
										//that this current cell should not be apart of the algorithm again
				beginCount ++;
				stack.push(currentCell);
                   
			}
			
			else
				currentCell = stack.pop();
            
		}	
		//determine where the end of the maze should be
            
		endCell = new Cell(size - 2,  randomOdd(r));
		maze[endCell.getRow() + 1][endCell.getColumn()] = ' ';
            
		removeMark();
			
		return maze;
           
	}
	
	private void removeMark(){
		for(int i = 1; i < size() - 1; i ++)
			for(int j = 1; j < size() - 1; j ++)
				if(maze[i][j] == 'x')
					maze[i][j] = ' ';
	}
	
	private void addMark(Cell cell){
    	
    	maze[cell.getRow()][cell.getColumn()] = 'x';
        
    }
     
	/*
	 * An odd number is 2*k + 1, so this is what it does
	 */
        
    private int randomOdd(int x){  
    	return 2*((int)(this.myRandom()*x)) + 1;
    }
        
	private int size(){
		return maze.length;
	}
	
	private boolean isEven(int x){
		return x % 2 == 0;
	}

	private void knockDownWall(Cell friend, Cell currentCell){
            
		if(friend.getRow() == currentCell.getRow()){    
			if(friend.getColumn() - currentCell.getColumn() < 0)
				maze[friend.getRow()][friend.getColumn() + 1] = ' ';
			else
				maze[currentCell.getRow()][friend.getColumn() - 1] = ' ';
           
		}
		
		else {
               // if the columns are the same
			if( friend.getRow() > currentCell.getRow() )
				maze[friend.getRow() - 1][friend.getColumn()] = ' ';
			
			else if(friend.getRow() < currentCell.getRow())
				maze[currentCell.getRow() - 1][currentCell.getColumn()] = ' ';
			
		}
        
	}
     /*
      * It looks for neighbors, and then randomly returns a cell, else it will return "null" or the noCell variable
      */
        
	private Cell getNeighborsRandom(Cell cell){
		
		ArrayList<Cell> nb = new ArrayList<Cell>();
		  //is this cell direction in the maze and does it have a wall and is that space empty??
		if( isInMaze(cell.east()) 
				&& !hasWall(cell.east(), Direction.EAST) 
				&& isEmpty(cell.east()))
			nb.add( cell.east() );
		if( isInMaze(cell.south()) && 
                        !hasWall(cell.south(), Direction.SOUTH) 
                        && isEmpty(cell.south()))
			nb.add(cell.south());
		
		if( isInMaze(cell.north()) 
				&& !hasWall(cell.north(), Direction.NORTH) 
				&& isEmpty(cell.north()))
			nb.add(cell.north());
		
		if(isInMaze(cell.west())
                        && !hasWall(cell.west(), Direction.WEST) 
                        && isEmpty(cell.west()))
			nb.add(cell.west());

		
		if(nb.isEmpty())
			return noCell;
		else  
			return nb.get((int)(myRandom()*nb.size())); 
			
	}
	
	/*
	 * Below are simple functions, the name should be self-explanatory
	 */
        
	private boolean isInMaze(Cell cell){
		
		return cell.getRow() >= 0 && cell.getRow() < size() 
				&& cell.getColumn() >= 0 && cell.getColumn() < size(); 
	}
	/*
	 * The direction indicate what orientation is this cell, then 
	 * it checks if the cell has a wall or '|' or '-'
	 */

	private boolean hasWall(Cell cell, Direction direction){
		
		if(direction.equals(Direction.EAST) 
				&& maze[cell.getRow()][cell.getColumn() - 1] != ' ')
				return false;
		
		else if(direction.equals(Direction.WEST) 
				&& maze[cell.getRow()][cell.getColumn() + 1] != ' ')
				return false;
		
		else if (direction.equals(Direction.SOUTH) 
				&& maze[cell.getRow() - 1][cell.getColumn()] != ' ')
			return false;
		
		else if (direction.equals(Direction.NORTH)
				&& maze[cell.getRow() + 1][cell.getColumn()] != ' ')
			return false;
		
		else
			return true;
	}
	
	private boolean isEmpty(Cell cell){
		//is maze[row][column] equal blank?
		return maze[cell.getRow()][cell.getColumn()] == ' ';
		
	}
	
	public Cell getBeginCell(){
		return beginCell;
	}
	
	public Cell getEndCell(){
		return endCell;
	}

}

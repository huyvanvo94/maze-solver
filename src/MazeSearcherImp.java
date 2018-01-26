
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class MazeSearcherImp implements MazeSearcher{

	private ArrayList<Cell > record; // For DFS and BFS to know the path out of the maze
	
	private Cell noCell; // this variable will be treated like "null"
	private Cell beginCell; // this variable to know where to begin

	private Cell endCell; // this variable is to know where to end
	
	private int count; // a counting variable to know the sequences of cells that DFS and BFS finds
	
	private char maze[][];
	private char savedMaze[][];

	public MazeSearcherImp(char[][] maze){
		this.maze = maze;
		savedMaze = clone(maze);
		noCell = new Cell(-1,-1);

		/*
		 * This loop determines where to find the starting position and find where the maze ends
		 */

		for(int col = 0; col < maze.length; col ++){
			if( this.maze[0][col] == ' '){
				beginCell = new Cell(1,col);
			}

			if(this.maze[maze.length - 1][col] == ' '){
				endCell = new Cell(maze.length - 2, col);
			}

		}

	}
	/*
	 * Below is an opinion for an random maze
	 */
  
	public MazeSearcherImp(int r){
		this(r,-1);
		
	}
	/*
	 * Below is a deterministic maze
	 */
	public MazeSearcherImp(int r, int seed){
		
		Maze m = new Maze(r,seed);
		
		maze = m.generateMaze();
		savedMaze = clone(maze);
		noCell = new Cell(-1,-1);
		
		beginCell = m.getBeginCell(); // starting position
		endCell = m.getEndCell(); // end position
	
		
	}

	public int size(){
		return maze.length;
	}

	
	private void markRoom(Cell cell, int count){
		
		maze[cell.getRow()][cell.getColumn()] = 
				Integer.toString((count % 10)).charAt(0);

	}

	//a boolean to know if the cell or position is in the maze
	private boolean isInMaze(Cell cell){
		return ( cell.getRow() >= 0 && cell.getRow() < size() 
                    && cell.getColumn() >= 0 && cell.getColumn() < size()); 
	}
		
	private boolean unVisited(Cell cell){
		return maze[cell.getRow()][cell.getColumn()] == ' '; // ' ' equals ROOM
	}

	//test for current cell and end of the maze's position
	private boolean endOfMaze(Cell cell){
		return cell.getRow() == endCell.getRow() &&
			cell.getColumn() == endCell.getColumn();
	}

	private char[][] clone(char maze[][]) {
		char[][] mazeCopy = new char[size()][size()]; 

		for (int i=0; i< size(); i++) 
			for (int j=0; j<size(); j++)
				mazeCopy[i][j] = maze[i][j];
		
		return mazeCopy; 
    }
	
	
	//there are four condition that indicate if the previous slot is empty
	private boolean previousIsNotEmpty(Cell cell, Direction direction){
		
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
	
	private Cell getNeighbor(Cell cell){	
		
		if( isInMaze(cell.east()) 
				&& unVisited(cell.east()) 
				&& previousIsNotEmpty(cell.east(), Direction.EAST))	
			
			return cell.east();
		
		else if( isInMaze(cell.south()) 
				&& unVisited(cell.south()) 
				&& previousIsNotEmpty(cell.south(),Direction.SOUTH) )

				return cell.south();
	
		else if( isInMaze(cell.north())
				&& unVisited(cell.north()) 
				&& previousIsNotEmpty(cell.north(), Direction.NORTH))
			
			return cell.north();

		else if( isInMaze(cell.west()) 
				&& unVisited(cell.west()) 
				&& previousIsNotEmpty(cell.west(), Direction.WEST))
			return cell.west();
		
	
		else 
			return noCell;
	}
	
	/*
	 * This is a nice method to make my code more readable
	 */
	
	private void add1(Cell cell, Stack<Cell> s, ArrayList<Cell> list, int count){
		
		s.push(cell);
		list.add(cell);
		markRoom( cell, count);
		
		
	}
	
	/*
	 * This a DFS. It finds the neighbor and the predecessor of the top of the stack
	 * Acts just like DFS
	 * My record variable add the cell to it to find the path out of the maze, but with a condition
	 */

    public void depthFirstSearch(){
   
    	char savedMaze2[][] = clone(maze);
		Stack<Cell> stack = new Stack<Cell>();
    	count = 0;
    	record = new ArrayList<Cell>();
    	
    	add1(beginCell, stack, record, count ++ ); // adds the start of the maze into the stack
    	Cell next;
    	
    	
    	while(!stack.isEmpty()){
	  
    		next = getNeighbor(stack.peek()); 
    		
    		next.predecessor = stack.peek();
    		
    		if(endOfMaze(next)){ 
  
    			record.add(next);
    			markRoom(next, count ++);
    			break; 
    			
    		}
  
    		if(next.equals(noCell)){  // if no neighbor, then remove it.
    				
    			stack.pop(); 		
    			record.remove(next.predecessor);	
    			
    		}
    			
    		else{
    			add1( next, stack, record, count ++);
    		}
    	}
    	
    	System.out.println("DFS path taken: ");
    	print();
    	
    	maze = savedMaze;
 
    
    	System.out.println("Path out of the maze:");
    	displayArray();
    	
    	maze = savedMaze2;
    	
    }
    
    //same as void add1
    
    private void add2(Cell cell, Queue<Cell> qq, ArrayList<Cell> list, int count){
    	qq.add(cell);
    	list.add(cell);
    	
    	markRoom(cell, count);
    	
    }
    
  
    /*
     * This is BFS, a bit more complicated then my DFS function
     */
    public void breathFirstSearch(){
    	count = 0;
    	char[][] savedMaze2 = clone(maze);

		Queue<Cell> qq = new LinkedList<Cell>();
    	record = new ArrayList<Cell>();
    	
    	add2(beginCell, qq, record, count ++);
    	Cell next;
    	
    	boolean continueTrue = true; // this continueTrue variable is use to exit out of the loops at the same time
    	while( continueTrue){
    		
    		Cell current = qq.poll();
    
    		while( continueTrue ){
    			
    			next = getNeighbor(current);
    	
    			if(next.equals(noCell)) 
    				break; 
    			
    			next.predecessor = current;	
    			add2(next, qq, record, count ++);
    			if(endOfMaze(next))
    				continueTrue = false;
    		}
    		
    	}
    	
    	System.out.println(" BFS paths");
    	print();
    	
    	maze = savedMaze;
    	
    	System.out.println("Path out of the maze:");
    	displayPathBFS(); // compute the path out of the maze
    	
    	maze = savedMaze2;
   
    }
    /*
     * This function looks for where the end of the maze is. 
     * It loops through, looking for the predecessor. 
     * It will end if it is back at the beginning of the maze
     * While in the loop, it marks it in the maze of path out
     */
    
    private void displayPathBFS(){
    	
    	Cell current = null;
    	/*
    	 * Finds the cell to know where to begin
    	 */
    	for(int i = record.size() - 1; i >= 0; i --)
    		if(endOfMaze(record.get(i)))
    			current = record.get(i);
    	
    	
    
    	while(true){
    		
    		addToPath(current, current.predecessor);
    		current = current.predecessor;
    		
    		if( current.equals(beginCell) ){
    
    			maze[ beginCell.getRow()][beginCell.getColumn()] = '#';
    			break;
    		}
    	
    	}
    	
    	print();
    }
    
    private void displayArray(){
    	
    	for(int i = record.size() - 1; i > 0; i --)	
    		addToPath(record.get(i), record.get(i - 1));
    		
    	
    	print(this.maze);
    }
    
    /*
     * The character '#' is use to denote the shortest path
     * It finds the corner space from two cells then marks it with '#'
     */
   
    private void addToPath(Cell friend, Cell currentCell){
       
    	maze[friend.getRow()][friend.getColumn()] = '#';
    	
    	maze[currentCell.getRow()][currentCell.getColumn()] = '#';
    	
    	
            if(friend.getRow() == currentCell.getRow()){
                
                if(friend.getColumn() - currentCell.getColumn() < 0)
                    maze[friend.getRow()][friend.getColumn() + 1] = '#';
   
                else
                    maze[currentCell.getRow()][friend.getColumn() - 1] = '#';
                
            }
            else 
            {
               // if the columns are the same
                if( friend.getRow() > currentCell.getRow() )
                    maze[friend.getRow() - 1][friend.getColumn()] = '#';
                 
                else //if(friend.getRow() < currentCell.getRow())  
                    maze[currentCell.getRow() - 1][currentCell.getColumn()] = '#';
         
            }
        }
    
    private void print(char maze[][]) {

    	for (int i=0; i<size(); i++) {
    		for (int j=0; j<size(); j++) {
    			System.out.print(maze[i][j]); System.out.print(' ');
    		}
    		System.out.println();
    	}
    }
    
    public void print(){
    	
    	print(this.maze);
    	
    }

}

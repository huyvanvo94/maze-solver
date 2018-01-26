
public class Cell{
	//variables
	private int row;
	private int column;
	Cell predecessor;
	Cell(int r, int c){
		row = r;
		column = c;
	}
	
	@Override
	public String toString(){
		return "(" + row + ")" + "(" + column + ")";
	}
	
	public int getRow(){
		return row;
	}
	
	public int getColumn(){
		return column;
	}

	public void setRow(int row){
		this.row = row;
	}
	public void setCol(int column){
		this.column = column;
	}
	
	/*
	 * The directions
	 */
	
	public Cell north(){
		return new Cell( row - 2, column);
	}
	
	public Cell south(){
		return new Cell( row + 2, column );
	}
	
	public Cell west(){
		return new Cell(row, column - 2);
	}
	
	public Cell east(){
		return new Cell(row, column + 2);
	}
}
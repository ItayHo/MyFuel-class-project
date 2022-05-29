package mines;

import java.util.ArrayList;
import java.util.List;

public class Mines {
	private static position[][] board;
	private static int height;
	private static int width;
	private boolean showAll=false;
	public Mines(int height, int width, int numMines) //builds the game board by any place with specific position
	{
		Mines.height=height;
		Mines.width=width;
		board = new position[height][width];
		for (int i = 0;i<height;i++)
			for (int j = 0; j<width;j++)
				board[i][j]=new position(i,j);
	}
	public boolean addMine(int i, int j) //adds mine to the board and the mine to the specific neighbor
	{
		if (board[i][j].mine==true) return false;
		board[i][j].mine=true;
		for (position p : Place.neighbours(i, j))
			p.nextToMines++;
		return true;
	}



	 public boolean open(int i, int j) //opens a place in the board and recursively open negibhorus(if there are mines)
		{

			if (board[i][j].mine) return false; //if the position has a mine in it
			board[i][j].open=true; 
			if (board[i][j].nextToMines==0) // in case there are not any mines in the place surrounding
				for (position p : Place.neighbours(i, j))
					if (!p.open) //if neighbor is not opened yet
						open(p.x,p.y); //will open the neighbor
			return true;
		}
	public void toggleFlag(int x, int y) //setting flag to position
	{
		board[x][y].flag= (!board[x][y].flag);
	}

	public boolean isDone() { //will check if the game is finished
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (!board[i][j].mine && !board[i][j].open) //in case there is any place that still close+it's not a mine
					return false;
			}
		}
		return true; //in case there is not any closed places+places that caught by mines
	}
	
	public void setShowAll(boolean showAll) //sets our class showAll to given the requested value
	{
		this.showAll=showAll;
	}

	public String get(int i, int j) //return string in requsted i,j index in the board
	{
		String ret = "";
		if (showAll){ 
			boolean val=board[i][j].open;
			board[i][j].open=true;
			ret=board[i][j].toString();
			board[i][j].open=val;
			return ret;
		}
		return board[i][j].toString();
	}

	public String toString() //return the board as a string
	{
		StringBuilder b = new StringBuilder();
		for (int i = 0 ; i < height;i++)
		{
			for (int j=0;j<width;j++)
				b.append(this.get(i, j));
			b.append("\n");
		}
		return b.toString();
	}


	

	private class position //this class represents a position on the board
	
	{
		private int x,y; //values that hold details about the position on board
		private boolean mine;
		private int nextToMines;
		private boolean flag;
		private boolean open;
		
		public position(int x,int y) //position constructor
		{
			flag=false; mine=false; open=false; nextToMines=0;
			this.x=x; this.y=y;
		}
		public String toString() //will return string representation of the position on board
		{
			if (!flag&&!open) return ".";
			else if (flag&&!open) return "F";
			else if (mine&&open) return "X";
			else if (open&&!mine)
				if (nextToMines==0) return " ";
			 return ""+nextToMines;

		}
	}

	private static class Place //a static class for a place that is used to get neighbors list of his position
	{
		public static List<position> neighbours(int x,int y) //returns that list
		{
			List<position> l = new ArrayList<position>();
			if (x+1<height) l.add(board[x+1][y]); //will add those neigbhors to the list by the correct places of them on board
			if (x-1>=0) l.add(board[x-1][y]);
			if (y+1<width) l.add(board[x][y+1]);
			if (y-1>=0) l.add(board[x][y-1]);
			if (x+1<height&&y+1<width) l.add(board[x+1][y+1]);
			if (x-1>=0&&y-1>=0) l.add(board[x-1][y-1]);
			if (x-1>=0&&y+1<width) l.add(board[x-1][y+1]);
			if (x+1<height&&y-1>=0) l.add(board[x+1][y-1]);
			return l;
		}

	}

}

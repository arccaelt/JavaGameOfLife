import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

class Cell
{
	private int x;
	private int y;
	private boolean isAlive;
	
	private static int dirX[] = {-1, -1, -1, 0, 1, 1, 1, 0};
	private static int dirY[] = {1, 0, -1, -1, -1, 0, 1, 1};

	Cell(int x, int y)
	{
		this.x = x;
		this.y = y;
		isAlive = true;
	}
	
	static List<Cell> searchForNewCells(final List<Cell> cells)
	{
		List<Cell> newbies = new ArrayList();
		
		for(Cell a : cells)
		{
			for(int i = 0; i < 8; i++)
			{
				Cell temp = new Cell(a.x + dirX[i], a.y + dirY[i]);
				if(!cells.contains(temp))
				{
					if(temp.countAliveNeighbours(cells) == 3)
						if(!newbies.contains(temp))
							newbies.add(temp);
				}
			}
		}
		
		return newbies;
	}
	
	int countAliveNeighbours(final List<Cell> cells)
	{
		int alive = 0;
		
		for(int i = 0; i < 8; i++)
		{
			Cell temp = new Cell(x + dirX[i], y + dirY[i]);
			if(cells.contains(temp))
			{
				int idx = cells.indexOf(temp);
				if(cells.get(idx).isCellAlive())
					alive++;
			}
		}
		
		return alive;
	}
	
	boolean isCellAlive()
	{
		return isAlive;
	}
	
	void die()
	{
		isAlive = false;
	}
	
	void resurrect()
	{
		isAlive = true;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		Cell c = (Cell)obj;
		return x == c.x && y == c.y;
	}
	
	@Override
	public String toString()
	{
		return String.format("%d %d", x, y);
	}
}

class ConwayGame
{
	private List<Cell> cells;
	private int alives = 0;
	
	ConwayGame()
	{
		cells = new ArrayList();
		readConfig();
		alives = cells.size();
	}
	
	void readConfig()
	{
		Scanner scn = new Scanner(System.in);
	
		int x, y;
		while(scn.hasNextLine())
		{
			String line = scn.nextLine();
			if(line.length() > 0)
			{
				String tok[] = line.split(" ");
				cells.add(new Cell(Integer.parseInt(tok[0]), Integer.parseInt(tok[1])));
			}
			else
				break;
		}
		
		scn.close();
	}
	
	void mainloop()
	{
		Cell c; // ref variable
		List<Integer> markedForKilling = new ArrayList<>();
		List<Integer> markedForResurrecting = new ArrayList<>();
		List<Cell> newCells;
		
		int epoch = 0;
		
		while(alives > 0)
		{
			System.out.printf("Epoch: %d\n", epoch++);
			markedForKilling.clear();
			markedForResurrecting.clear();
			
			for(int i = 0, n = cells.size(); i < n; i++)
			{
				c = cells.get(i);
				int aliveNeighbours = c.countAliveNeighbours(cells);
				System.out.printf("\t%s = %d\n", c, aliveNeighbours);
				if(c.isCellAlive())
				{
					System.out.println("cell is alive");
					if(aliveNeighbours <= 1 || aliveNeighbours >= 4)
					{
						alives--;
						markedForKilling.add(i);
					}
				}
				else
				{
					if(aliveNeighbours == 3)
					{
						alives++;
						markedForResurrecting.add(i);
					}
				}
			}
			
			// find the new cells we will need to create
			System.out.println("New cells: ");
			newCells = Cell.searchForNewCells(cells);
			for(Cell nc : newCells)
			{
				cells.add(nc);
			}
			
			// kill the cells
			for(int id : markedForKilling)
				cells.get(id).die();
			
			// resurrect the cells
			for(int id : markedForResurrecting)
				cells.get(id).resurrect();
		}
	}
	
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		
		for(Cell c : cells)
		{
			sb.append(c.toString() + "\n");
		}

		return sb.toString();
	}
}

class Solver
{
	public static void main(String []args)
	{
		ConwayGame cg = new ConwayGame();
		cg.readConfig();
		System.out.print(cg);
		cg.mainloop();
//		GUI g = new GUI();
	}
}
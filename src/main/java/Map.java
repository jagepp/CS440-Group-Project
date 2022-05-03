import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.util.Scanner; 

public class Map {

	private Tile map[][];
	private int rowSize;
	private int colSize;

	public Map(int rowSize, int colSize)
	{
		this.rowSize = rowSize;
		this.colSize = colSize;
		map = new Tile[rowSize][colSize];

		fillInOcean();	// Fill in the map with ocean tiles		
	}

	public Map(String mapFilename) 
	{
		//mapFilename = "/map.txt";

		try 
		{
			File mapFile = new File(Map.class.getResource(mapFilename).toURI());
			Scanner s = new Scanner(mapFile);
			rowSize = s.nextInt();
			colSize = s.nextInt();
			map = new Tile[rowSize][colSize];
			fillInOcean();

			int x, y;
			while (s.hasNextInt()) 
			{
				x = s.nextInt();
				y = s.nextInt();
				map[x][y] = new Island();
			}
			s.close();
		} catch (FileNotFoundException | URISyntaxException e) {
			System.out.println("An error occurred with the map file.");
			e.printStackTrace();
		}
	}

	private void fillInOcean()
	{
		for(int i = 0; i < rowSize; i++)
		{
			for(int j = 0; j < colSize; j++)
			{
				map[i][j] = new Ocean();
			}
		}
	}
	
	public Tile getTile(int x, int y)
	{
		return map[x][y];
	}
	
	public void printMap()
	{
		for(int i = 0; i < rowSize; i++)
		{
			for(int j = 0; j < colSize; j++)
			{
				System.out.print(getTile(i, j).getType());
			}
			System.out.println();
		}
	}
	
	public int getColSize()
	{
		return colSize;
	}
	public int getRowSize()
	{
		return rowSize;
	}
	
	public boolean isIsland(int x, int y)
	{
		if(x < 0 || x >= rowSize || y < 0 || y >= colSize) // if x or y are out of bounds, return false
			return false;
		
		return map[x][y].getType() == 'I';
	}
}

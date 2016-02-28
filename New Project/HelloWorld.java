import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
 
public class HelloWorld {
    
    static class Position {
    public int row;
    public int col;
 
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
 
    public Position getLeft() {
        return new Position(row, col - 1);
    }
 
    public Position getRight() {
        return new Position(row, col + 1);
    }
 
    public Position getBottom() {
        return new Position(row + 1, col);
    }
 
    public Position getUp() {
        return new Position(row - 1, col);
    }
 
    public String toString() {
        return "row:" + row + " col:" + col;
    }
}
 
    private static List<Position> getNeighbour(Position p, int row, int col) {
        List<Position> neighbours = new ArrayList<Position>();
 
        Position posLeft = p.getLeft();
        if (posLeft.row >= 0
                && posLeft.row < row 
                && posLeft.col >= 0
                && posLeft.col < col)
            neighbours.add(posLeft);
        Position posRight = p.getRight();
        if (posRight.row >= 0
                && posRight.row < row 
                && posRight.col >= 0
                && posRight.col < col)
            neighbours.add(posRight);
        Position posUp = p.getUp();
        if (posUp.row >= 0
                && posUp.row < row 
                && posUp.col >= 0
                && posUp.col < col)
            neighbours.add(posUp);
        Position posDown = p.getBottom();
        if (posDown.row >= 0
                && posDown.row < row 
                && posDown.col >= 0
                && posDown.col < col)
            neighbours.add(posDown);
 
        return neighbours;
 
    }
    
    public static void printPath(int[][] grid, char[][] arr, ArrayList<Position> target, int row, int col) {
		Position pos = new Position(-1,-1);
		int d = 1, c =1;
		for(Position p : target)
		{
		    List<Position> neighbours = getNeighbour(p, row, col);
		    int val = grid[p.row][p.col];
		    for (Position neighbour : neighbours) {
		        if (grid[neighbour.row][neighbour.col] == val-1 ) {
		             if(p.col == neighbour.col && p.row < neighbour.row)
		                c = 4;
		             else if(p.col > neighbour.col && p.row == neighbour.row)
		                c = 3;
		             else if(p.col == neighbour.col && p.row > neighbour.row)
		                c = 2;
		             else if(p.col < neighbour.col && p.row == neighbour.row)
		                c = 1;
		             if(c>=d){
		                pos = neighbour;
		                d = c;
		            }
		        }
		    }
		}
		if(d == 1)
		    arr[pos.row][pos.col] = '<';
		else if(d == 2)
		    arr[pos.row][pos.col] = 'v';
		else if(d == 3)
		    arr[pos.row][pos.col] = '>';
		else if(d == 4)
		    arr[pos.row][pos.col] = '^';
		d =1; c=1;
		while(grid[pos.row][pos.col] != 1)
		{	
			int val = grid[pos.row][pos.col];
			List<Position> neighbours = getNeighbour(pos, row, col);
			for (Position neighbour : neighbours) {
                	if (grid[neighbour.row][neighbour.col] == val-1) {
                	    if(pos.col == neighbour.col && pos.row < neighbour.row)
		                    c = 4;
		                else if(pos.col > neighbour.col && pos.row == neighbour.row)
		                    c = 3;
		                else if(pos.col == neighbour.col && pos.row > neighbour.row)
		                    c = 2;
		                else if(pos.col < neighbour.col && pos.row == neighbour.row)
		                    c = 1;
		                if(c>=d){
		                    pos = neighbour;
		                    d = c;
		                 }
		            }
		    }
		    if(d == 1)
		        arr[pos.row][pos.col] = '<';
		    else if(d == 2)
		        arr[pos.row][pos.col] = 'v';
		    else if(d == 3)
		        arr[pos.row][pos.col] = '>';
		    else if(d == 4)
		        arr[pos.row][pos.col] = '^';
		    d =1; c=1;
		}
	}
    
    public static int getPath(char[][] arr, int row, int col, Position p) {
        final int[][] grid = new int[row][col];
        PriorityQueue<Position> queue = new PriorityQueue<Position>(col * row,
                new Comparator<Position>() {
 
                    public int compare(Position o1, Position o2) {
                        if (grid[o1.row][o1.col] < grid[o2.row][o2.col])
                            return -1;
                        else if (grid[o1.row][o1.col] > grid[o2.row][o2.col])
                            return 1;
                        else
                            return 0;
                    }
                });
        queue.offer(p);
        grid[p.row][p.col]=1;
        while (!queue.isEmpty()) {
 
            Position current = queue.poll();
            List<Position> neighbours = getNeighbour(current, row, col);
            ArrayList<Position> target_neighbours = new ArrayList<Position>();
            int flag = 0;
            for (Position neighbour : neighbours) {
                if (!(arr[neighbour.row][neighbour.col] == 'F') 
                        && grid[neighbour.row][neighbour.col] == 0) {
 
                    grid[neighbour.row][neighbour.col] = grid[current.row][current.col] + 1;
                    queue.offer(neighbour);
                }
                
                if (arr[neighbour.row][neighbour.col] == 'X') {
                    target_neighbours.add(neighbour);
                    flag = 1;
                }
            }
            if (flag == 1) 
            {
                
                printPath(grid, arr, target_neighbours, row, col);
                Position n = target_neighbours.get(0);
                return grid[n.row][n.col] -1 ;
            }
        }
        return -1;
    }
 
    public static String[] generateSensorNetworkRoutes(String[] input) {
        int row = input.length;
        int col = input[0].length();
        char[][] arr = new char[row][col];
        String[] res = new String[row];
        for (int i = 0; i < input.length; ++i) {
            arr[i] = input[i].toCharArray();
        }
        int path = 0;
        for(int i=0; i<row; i++){
            for (int c = 0; c < arr[i].length; c++) {
                if (arr[i][c] == 'o') {
                    Position p = new Position(i,c);
                    path = getPath(arr, row, col, p);
                    if(path == -1)
                        arr[i][c] = '?';    
                }
            }
        }
        
        for(int i=0; i<row; i++)
		    res[i] = new String(arr[i]);
        return res;
    }
    public static void main(String[] args) {
        String[] input = {"ooXFo", "oooFo", "XooFX"}; 
        int row = input.length;
        int col = input[0].length();
        String[] res = new String[row];
        res = generateSensorNetworkRoutes(input);

		for(int i=0; i < row; i++)
		    System.out.println(res[i]);
	}
}
 
/*class Position {
    public int row;
    public int col;
 
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
 
    public Position getLeft() {
        return new Position(row, col - 1);
    }
 
    public Position getRight() {
        return new Position(row, col + 1);
    }
 
    public Position getBottom() {
        return new Position(row + 1, col);
    }
 
    public Position getUp() {
        return new Position(row - 1, col);
    }
 
    public String toString() {
        return "row:" + row + " col:" + col;
    }
}
*/
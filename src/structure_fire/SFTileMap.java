package structure_fire;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SFTileMap implements TileBasedMap {

    public static final int WIDTH = 12, HEIGHT = 12;
    public int[][] graph;

    SFTileMap(String level_name, StructureFireGame fg ) {
        graph = load( level_name, fg );
    }

    public int[][] load( String level_name, StructureFireGame fg ) {
        int[][] graph = new int[WIDTH][HEIGHT];
        try {
            File myObj = new File( "structure_fire/src/structure_fire/resource/" + level_name + ".txt" );
            Scanner myReader = new Scanner(myObj);
            int row = 0, col = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                for (String s : data.split(" ")) {
                    if ( s.trim().equals("1") ) {
                        fg.map.put((row * 1000) + col, new Planks((col * 50) + 25, (row * 50) + 25));
                        graph[row][col] = 2;
                    } else if ( s.trim().equals("2") ) {
                        fg.map.put((row * 1000) + col, new Ladder((col * 50) + 25, (row * 50) + 25));
                        graph[row][col] = 1;
                    } else if ( s.trim().equals("3") ) {
                        fg.map.put((row * 1000) + col, new Stone((col * 50) + 25, (row * 50) + 25));
                        graph[row][col] = 0;
                    } else if ( s.trim().equals("4") ) {
                        fg.map.put((row * 1000) + col, new Planks((col * 50) + 25, (row * 50) + 25));
                        fg.fl_enemy = new FlameEnemy((col * 50) + 25, (row * 50) + 25);
                        graph[row][col] = 2;
                    } else if ( s.trim().equals("5") ) {
                        fg.player = new Player( (col * 50) + 25, (row * 50) + 25 );
                        graph[row][col] = 0;
                    } else {
                        graph[row][col] = 0;
                    }
                    col++;
                }
                row++; col = 0;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return graph;
    }

    public void update_tiles( int delta, StructureFireGame fg ) {
        int row = (int) Math.floor(fg.player.getY() / 50);
        int col = (int) Math.floor(fg.player.getX() / 50);
        if (
                fg.map.containsKey(( row * 1000) + col ) &&
                        fg.map.get( (row * 1000) + col ).isLadder
        ) {
            fg.map.get( (row * 1000) + col ).update(delta, fg.player, 0, 0);
            for ( int y = col - 1; y <= col + 1; y++ ) {
                if ( y != 0 ) {
                    if ( fg.map.containsKey( ( row * 1000 ) + y ) )
                        fg.map.get( (row * 1000) + y ).update( delta, fg.player, 0, y - col );
                }
            }
        } else {
            for (int x = row - 1; x <= row + 1; x++) {
                for (int y = col - 1; y <= col + 1; y++) {
                    if (fg.map.containsKey((x * 1000) + y))
                        fg.map.get((x * 1000) + y).update(delta, fg.player, x - row, y - col);
                }
            }
        }
    }

    @Override
    public int getWidthInTiles() {
        return WIDTH;
    }

    @Override
    public int getHeightInTiles() {
        return HEIGHT;
    }

    @Override
    public void pathFinderVisited(int i, int i1) {}

    @Override
    public boolean blocked(PathFindingContext pathFindingContext, int col, int row) {
        return graph[row][col] == 0;
    }

    @Override
    public float getCost(PathFindingContext pathFindingContext, int i, int i1) {
        return 1.0f;
    }
}

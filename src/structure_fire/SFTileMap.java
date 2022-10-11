package structure_fire;

import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class SFTileMap implements TileBasedMap {

    public static final int WIDTH = 12, HEIGHT = 12;
    public int[][] graph;
    private Stack<Integer> to_delete;

    SFTileMap(String level_name, StructureFireGame fg ) {
        graph = load( level_name, fg );
        to_delete = new Stack();
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
                    } else if ( s.trim().equals("6") ) {
                        fg.map.put((row * 1000) + col, new Civilian( (col * 50) + 25, (row * 50) + 25 ));
                        fg.civilians.push(new int[]{row, col});
                        graph[row][col] = 2;
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

        for ( WaterParticle p : fg.water_stream ) {
            p.update(delta);
            int p_row = (int) Math.floor(p.getY() / 50);
            int p_col = (int) Math.floor(p.getX() / 50);
            if (fg.map.containsKey( (p_row * 1000) + p_col ) ) {
                p.visible = false;
                fg.map.get(  (p_row * 1000) + p_col ).isOnFire = false;
                if ( fg.map.get(  (p_row * 1000) + p_col ).isCivilian ) {
                    fg.civilians.push(new int[]{p_row, p_col});
                }
            }
            if (p.getX() > fg.ScreenWidth || p.getX() < 0 || p.getY() > fg.ScreenHeight)
                p.visible = false;
        }

        fg.map.forEach( (k, v) -> {
            if ( v.isOnFire && !fg.flames.contains( v.flame ) ) {
                fg.flames.add( v.flame );
            } else if (!v.isOnFire) {
                fg.flames.remove( v.flame );
            } else {
                v.timeToLive -= delta;
            }
            if ( v.timeToLive < 0 ) {
                v.visible = false;
                fg.flames.remove( v.flame );
                fg.tile_map.to_delete.push(k);
                fg.tile_map.graph[(int)((v.getY() - 25) / 50)][(int)((v.getX() - 25) / 50)] = 0;
            }
        });

        fg.tile_map.to_delete.forEach( (k) -> fg.map.remove(k));
        fg.tile_map.to_delete.clear();
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

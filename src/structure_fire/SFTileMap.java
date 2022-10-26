package structure_fire;

import jig.Vector;
import org.newdawn.slick.Input;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class SFTileMap implements TileBasedMap {

    public static final int WIDTH = 12, HEIGHT = 12;
    public int[][] graph;
    public float flammable_tiles_left;
    public float initial_flammable_tiles;
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
                        this.flammable_tiles_left++;
                    } else if ( s.trim().equals("2") ) {
                        fg.map.put((row * 1000) + col, new Ladder((col * 50) + 25, (row * 50) + 25));
                        graph[row][col] = 1;
                        this.flammable_tiles_left++;
                    } else if ( s.trim().equals("3") ) {
                        fg.map.put((row * 1000) + col, new Stone((col * 50) + 25, (row * 50) + 25));
                        graph[row][col] = 0;
                    } else if ( s.trim().equals("4") ) {
                        fg.map.put((row * 1000) + col, new Planks((col * 50) + 25, (row * 50) + 25));
                        fg.fl_enemy = new FlameEnemy((col * 50) + 25, (row * 50) + 25);
                        graph[row][col] = 2;
                        this.flammable_tiles_left++;
                    } else if ( s.trim().equals("5") ) {
                        fg.player = new Player( (col * 50) + 25, (row * 50) + 25 );
                        graph[row][col] = 0;
                    } else if ( s.trim().equals("6") ) {
                        fg.map.put((row * 1000) + col, new Civilian( (col * 50) + 25, (row * 50) + 25 ));
                        fg.civilians.push(new int[]{row, col});
                        graph[row][col] = 2;
                        this.flammable_tiles_left++;
                    } else if ( s.trim().equals("7") ) {
                        fg.map.put((row * 1000) + col, new Coin( (col * 50) + 25, (row * 50) + 25 ));
                        graph[row][col] = 2;
                        this.flammable_tiles_left++;
                    } else if ( s.trim().equals("8") ) {
                        fg.map.put((row * 1000) + col, new BGPlanks( (col * 50) + 25, (row * 50) + 25 ));
                        graph[row][col] = 2;
                        this.flammable_tiles_left++;
                    } else if ( s.trim().equals("H") ) {
                        fg.map.put((row * 1000) + col, new FireHydrant( (col * 50) + 25, (row * 50) + 25 ));
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
        initial_flammable_tiles = this.flammable_tiles_left;
        return graph;
    }

    public void update_tiles( int delta, StructureFireGame fg, Input input ) {
        int row = (int) Math.floor(fg.player.getY() / 50);
        int col = (int) Math.floor(fg.player.getX() / 50);
        if (
                (fg.map.containsKey(( row * 1000) + col ) &&
                fg.map.get( (row * 1000) + col ).isLadder )
        ) {
            if ( input.isKeyDown( Input.KEY_W) ) {
                if (
                        (fg.map.containsKey(( ( row - 1 ) * 1000) + col ) &&
                        !fg.map.get( ( ( row - 1 ) * 1000) + col ).isCollideable )
                ) {
                    fg.player.setVelocity(new Vector(fg.player.getVelocity().getX(), -0.22f));
                } else {
                    fg.player.setY( (row * 50) + 25 );
                    fg.player.setVelocity(new Vector(fg.player.getVelocity().getX(), 0.0f));
                }
            } else if ( input.isKeyDown( Input.KEY_S ) ) {
                if (
                        (fg.map.containsKey(( ( row + 1 ) * 1000) + col ) &&
                        !fg.map.get( ( ( row + 1 ) * 1000) + col ).isCollideable )
                ) {
                    fg.player.setVelocity(new Vector(fg.player.getVelocity().getX(), 0.22f));
                } else {
                    fg.player.setY( (row * 50) + 25 );
                    fg.player.setVelocity(new Vector(fg.player.getVelocity().getX(), 0.0f));
                }
            } else {
                fg.player.setY( (row * 50) + 25 );
                fg.player.setVelocity(new Vector(fg.player.getVelocity().getX(), 0.0f));
            }
            for (int y = col - 1; y <= col + 1; y++) {
                if (y != 0) {
                    if (fg.map.containsKey((row * 1000) + y))
                        fg.map.get((row * 1000) + y).update(delta, fg.player, 0, y - col);
                }
            }
        } else {
            for (int x = row - 1; x <= row + 1; x++) {
                for (int y = col - 1; y <= col + 1; y++) {
                    if (fg.map.containsKey((x * 1000) + y)) {
                        if (!fg.map.get((x * 1000) + y).isLadder) {
                            fg.map.get((x * 1000) + y).update(delta, fg.player, x - row, y - col);
                        }
                    }
                }
            }
        }

        for ( WaterParticle p : fg.water_stream ) {
            p.update(delta);
            int p_row = (int) Math.floor(p.getY() / 50);
            int p_col = (int) Math.floor(p.getX() / 50);
            if (fg.map.containsKey( (p_row * 1000) + p_col )) {
                if (fg.map.get( (p_row * 1000) + p_col).isCollideable )
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
                this.flammable_tiles_left--;
                fg.flames.remove( v.flame );
                fg.tile_map.to_delete.push(k);
                fg.tile_map.graph[(int)((v.getY() - 25) / 50)][(int)((v.getX() - 25) / 50)] = 0;
                if (
                    fg.map.containsKey( k - 1000 ) &&
                    fg.map.get( k - 1000 ).flammable &&
                    !fg.map.get( k - 1000 ).isOnFire
                ) {
                    fg.map.get( k - 1000 ).isOnFire = true;
                }
                if (
                        fg.map.containsKey( k - 1 ) &&
                        fg.map.get( k - 1 ).flammable &&
                        !fg.map.get( k - 1 ).isOnFire
                ) {
                    fg.map.get( k - 1 ).isOnFire = true;
                }
                if (
                        fg.map.containsKey( k + 1 ) &&
                                fg.map.get( k + 1 ).flammable &&
                                !fg.map.get( k + 1 ).isOnFire
                ) {
                    fg.map.get( k + 1 ).isOnFire = true;
                }
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

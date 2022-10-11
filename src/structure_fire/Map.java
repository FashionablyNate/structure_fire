package structure_fire;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Map {

    public static void load( String level_name, StructureFireGame fg ) {
        try {
            File myObj = new File( "structure_fire/src/structure_fire/resource/" + level_name + ".txt" );
            Scanner myReader = new Scanner(myObj);
            int row = 0, col = 0;
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                for (String s : data.split(" ")) {
//                    System.out.print(s + " ");
                    if ( s.trim().equals("1") )
                        fg.map.put( (row * 1000) + col, new Planks( (col * 50) + 25, (row * 50) + 25 ));
                    else if ( s.trim().equals("2") )
                        fg.map.put( (row * 1000) + col, new Ladder( (col * 50) + 25, (row * 50) + 25 ));
                    else if ( s.trim().equals("3") )
                        fg.map.put( (row * 1000) + col, new Stone( (col * 50) + 25, (row * 50) + 25 ));
                    col++;
                }
                row++; col = 0;
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
}

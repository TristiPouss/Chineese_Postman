package m1chinesepostman2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Objects;
import java.util.Scanner;

import m1graphs2024.UndirectedGraph;

public class UndirectedGraphChinesePostman extends UndirectedGraph{

    /* Constructors */

    /**
     * Constructor for empty graph
     */
    public UndirectedGraphChinesePostman() {
        super();
    }

    /**
     * Constructor for empty graph with a name
     * @param name The name for the graph
     */
    public UndirectedGraphChinesePostman(String name) {
        super(name);
    }
    
    /**
     * Constructor from Successor array
     * @param sa unknown number of int as successor array
     */
    public UndirectedGraphChinesePostman(int ... sa) {
        super(sa);
    }

    /**************************
     *                        *
     *      Graph Import      *
     *       and Export       *
     *                        *
     **************************/
    
    /**
      * for importing a file in the restricted DOT format
      * The base extension is assumed to be .gv
      * @param filename a String. The absolute path to the DOT file with no extension
      * @return a Graph
      */
      public static UndirectedGraphChinesePostman fromDotFile(String filename) {
        return fromDotFile(filename, ".gv");
    }

    /**
      * for importing a file in the restricted DOT format
      * @param filename a String. The absolute path to the DOT file with no extension
      * @param extension a String, The extension of the file
      * @return a Graph
      */
    public static UndirectedGraphChinesePostman fromDotFile(String filename, String extension) {
        if(!(extension.equals(".gv") || extension.equals(".dot"))){
            return null;
        }
        UndirectedGraphChinesePostman result = null;
        File newFile = new File("./test/" + filename + extension);
        try{
            try (Scanner parser = new Scanner(newFile)) {
                parser.useDelimiter("");
                System.out.println("creating graph");
                result = new UndirectedGraphChinesePostman();
                int a = 0, b = 0, w = 0;
                int count = 1;
                while(parser.hasNext()){

                    if (parser.hasNextInt()) {
                        int curr = parser.nextInt();
                        
                        if(count == 4){
                            result.addEdge(a, b, w);
                            count=1;
                            continue;
                        }

                        if(count == 3){
                            w = curr;
                        } else if(count == 2){
                            b = curr;
                        } else a = curr;
                        
                        count++;
                    }else {
                        parser.next();
                        continue;
                    }
                }
            } catch (NumberFormatException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }catch(FileNotFoundException f){
            throw new RuntimeException(f);
        }
        return result;
    }

}


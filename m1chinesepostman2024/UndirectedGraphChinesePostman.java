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
        File newFile = new File("./m1graphs2024/dotGraphsTestPW2/" + filename + extension);
        try{
            try (Scanner parser = new Scanner(newFile)) {
                while(parser.hasNextLine()){
                    String curr = parser.nextLine().trim();

                    if(curr.charAt(0) == '#' || curr.isEmpty()){
                        continue;
                    }

                    String[] token = curr.split("\\s+");
                    if(curr.contains("{")){
                        if(token.length == 3){
                            if(Objects.equals(token[2], "{")){
                                if(token[0].equals("graph")){
                                    result = new UndirectedGraphChinesePostman(token[1]);
                                }else{
                                    return null;
                                }
                            }
                        }else{
                            if(token[0].equals("graph")){
                                result = new UndirectedGraphChinesePostman();
                            }else{
                                return null;
                            }
                        }
                    }

                    if(token[token.length - 1].equals("}")){
                        return result;
                    }
                    
                    if(result != null){
                        if(token.length >= 3){
                            if(token[1].equals("--")){
                                int node1 = Integer.parseInt(token[0]);
                                int node2 = Integer.parseInt(token[2]);
                                result.addNode(node1);
                                result.addNode(node2);

                                if(token.length > 3){
                                    result.addEdge(node1, node2, Integer.parseInt(token[token.length - 1].split("=")[1].replace("]", "")));
                                }else{
                                    result.addEdge(node1, node2);
                                }
                            }
                        }else{
                            if(token.length == 1 && token[0].matches("[0-9]+")){
                                result.addNode(Integer.parseInt(token[0]));
                            }
                        }
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


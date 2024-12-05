package m1chinesepostman2024;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import m1graphs2024.*;

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
    
    @Override
    public String toDotString() {
        String dotString = "graph {";

        List<Node> usedNodes = getAllNodesInEdges();

        for (Node n : getAllNodes()){
            if(getList().get(n).isEmpty()){
                if(!usedNodes.contains(n)){
                    dotString += "\n\t" + n;
                }
            }else{
                for (Edge e : getOutEdges(n)){
                    if(e.from().getId() <= e.to().getId()){
                        dotString += "\n\t" + e.from() + " -- " + e.to();
                        if(e.isWeighted()) dotString += " [label=" + e.getWeight() + ", len=" + e.getWeight() + "]";
                    }
                }
            }
        }

        dotString += "\n}";
        return dotString;
    }

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
                parser.useDelimiter("\\\s|\\-|\\=|\\,|\\[|\\]|\\{|\\}");
                if (dev) System.out.println("creating graph");
                result = new UndirectedGraphChinesePostman();
                Integer a = null, b = null, w = null;
                int count = 1;
                while(parser.hasNext()){

                    String next = parser.next();
                    if (dev) System.out.print(next+" ");
                    if(next.equals("rank")){
                        parser.nextLine();
                    }

                    if (parser.hasNextInt()) {
                        int curr = parser.nextInt();
                        if (dev) System.out.print(curr);
                        
                        if(count == 4){
                            if(a != null && b != null && w != null){
                                //System.out.print("\n  Adding : "+a+" -- "+b+" w :"+w+"\n");
                                result.addEdge(a, b, w);
                            }
                            count=1;
                            a = null;
                            b = null;
                            w = null;
                            continue;
                        }

                        if(count == 3){
                            w = curr;
                        } else if(count == 2){
                            b = curr;
                        } else a = curr;
                        
                        count++;
                    }else {
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
        if (dev) System.out.println();
        return result;
    }

}


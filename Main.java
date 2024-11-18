import m1graphs2024.*;

public class Main {
    public static void main(String[] args) {
        Graph g = new Graph(2, 0, 3, 0, 4, 8, 0, 3, 5, 0, 7, 8, 0, 8, 8);
        System.out.println(g.toDotString());
        var m = g.toAdjMatrix();
        System.out.println("\nAdjacency Matrix :\n");
        for(int col = 0; col < m.length; col++){
            for(int row = 0; row < m[0].length; row++){
                System.out.print(" " + m[col][row] + " ");
            }
            System.out.println();
        }
        System.out.println();
        var a = g.toSuccessorArray();
        System.out.println("\nSuccessor Array :\n");
        for(int i = 0; i < a.length; i++){
            System.out.print(a[i] + " ");
        }
        System.out.println();
        System.out.println("\nTransitiveClosure :\n");
        System.out.println(g.getTransitiveClosure().toDotString());
        System.out.println();
        System.out.println("\nDFS :\n");
        var dfs = g.getDFS();
        for(int i = 0; i < dfs.size(); i++){
            System.out.print(dfs.get(i) + " ");
        }
        System.out.println();
        System.out.println();
        //
        UndirectedGraph g2 = new UndirectedGraph(2, 0, 4, 0, 4, 0, 3);
        System.out.println(g2.toDotString());
        var m2 = g2.toAdjMatrix();
        System.out.println("\nAdjacency Matrix :\n");
        for(int col = 0; col < m2.length; col++){
            for(int row = 0; row < m2[0].length; row++){
                System.out.print(" " + m2[col][row] + " ");
            }
            System.out.println();
        }
        System.out.println();
        var a2 = g2.toSuccessorArray();
        System.out.println("\nSuccessor Array :\n");
        for(int i = 0; i < a2.length; i++){
            System.out.print(a2[i] + " ");
        }
        System.out.println();
        System.out.println("\nTransitiveClosure :\n");
        System.out.println(g2.getTransitiveClosure().toDotString());
        System.out.println();
        System.out.println("\nDFS :\n");
        var dfs2 = g2.getDFS();
        for(int i = 0; i < dfs2.size(); i++){
            System.out.print(dfs2.get(i) + " ");
        }
        System.out.println();
        //
    }
}

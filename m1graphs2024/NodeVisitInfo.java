package m1graphs2024;

/**
 * A groupment of variable containing informations about a Node relative to a DFS traversal
 * Used in DFS With Visit Infos function
 */
public class NodeVisitInfo {
    NodeColour colour;
    Node predecessor;
    Integer discovery;
    Integer finished;
}

package m1graphs2024;

/**
 * EdgeVisitType is used for the DFS with informations 
 * In order to know if an Edge is a tree, backward, forward or a cross 
 */
public enum EdgeVisitType {
    /**
     * Tree
     */
    TREE,
    /**
     * Backward
     */
    BACKWARD,
    /**
     * Forward
     */
    FORWARD,
    /**
     * Cross
     */
    CROSS
}

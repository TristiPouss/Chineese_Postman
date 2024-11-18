package m1graphs2024;

/**
 * Enumeration to know the state of a node in a traversal 
 * Used in DFS With Visit Infos function
 */
public enum NodeColour {
    /**
     * White means not discovered (or first encounter)
     */
    WHITE,
    /**
     * Gray means in treeatment
     */
    GRAY,
    /**
     * Black means the treatment is over
     */
    BLACK
}

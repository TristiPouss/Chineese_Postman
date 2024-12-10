package m1chinesepostman2024;

public enum Type {
    EULERIAN("Eulerian"),
    SEMI_EULERIAN("Semi-Eulerian"),
    NON_EULERIAN("Non-Eulerian");

    String name;
    
    Type(String string) {
        this.name = string;
    }
    
    @Override
    public String toString() {
        return name;
    }
}

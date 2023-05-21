package it.tungsteno.fp.rovinePerdute.pathfinding;

/**
 * Classe che caratterizza l'oggetto "nodo". Ogni nodo corrisponde ad una città, che possiede un nome,
 * due coordinate, x e y, e un'altezza rispetto al suolo
 */
public class Node {
    private final String name;
    private final int xCoordinate;
    private final int yCoordinate;
    private final int height;

    public Node(String name, int xCoordinate, int yCoordinate, int height) {
        this.name = name;
        this.xCoordinate = xCoordinate;
        this.yCoordinate = yCoordinate;
        this.height = height;
    }


    public String getName() {
        return name;
    }

    public int getxCoordinate() {
        return xCoordinate;
    }

    public int getyCoordinate() {
        return yCoordinate;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Node{" +
                ", name='" + name + '\'' +
                ", xCoordinate=" + xCoordinate +
                ", yCoordinate=" + yCoordinate +
                ", height=" + height +
                '}';
    }
}

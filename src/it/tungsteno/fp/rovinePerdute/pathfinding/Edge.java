package it.tungsteno.fp.rovinePerdute.pathfinding;

public class Edge {
    private Node startNode, destinationNode;
    private double heightCost, linearCost;

    public Edge(Node startNode, Node destinationNode) {
        this.startNode = startNode;
        this.destinationNode = destinationNode;
        this.heightCost = Math.abs(startNode.getHeight() - destinationNode.getHeight());
        this.linearCost = Math.sqrt(Math.pow(startNode.getxCoordinate() - destinationNode.getxCoordinate(), 2) +
                Math.pow(startNode.getyCoordinate() - destinationNode.getyCoordinate(), 2));
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getDestinationNode() {
        return destinationNode;
    }

    public double getHeightCost() {
        return heightCost;
    }

    public double getLinearCost() {
        return linearCost;
    }
}

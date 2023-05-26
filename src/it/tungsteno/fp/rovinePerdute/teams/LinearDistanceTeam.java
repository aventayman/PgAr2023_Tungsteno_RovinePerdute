package it.tungsteno.fp.rovinePerdute.teams;

import it.tungsteno.fp.rovinePerdute.pathfinding.Node;

public record LinearDistanceTeam(String name) implements Team {
    public double calculateNodeDistance(Node startNode, Node destinationNode) {
        return Math.sqrt(Math.pow(startNode.getxCoordinate() - destinationNode.getxCoordinate(), 2) +
                Math.pow(startNode.getyCoordinate() - destinationNode.getyCoordinate(), 2));
    }
}

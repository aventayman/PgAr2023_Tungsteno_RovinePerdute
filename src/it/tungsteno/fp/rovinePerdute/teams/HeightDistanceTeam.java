package it.tungsteno.fp.rovinePerdute.teams;

import it.tungsteno.fp.rovinePerdute.pathfinding.Node;

public record HeightDistanceTeam(String name) implements Team {

    public double calculateNodeDistance(Node startNode, Node destinationNode) {
        return Math.abs(startNode.getHeight() - destinationNode.getHeight());
    }
}

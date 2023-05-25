package it.tungsteno.fp.rovinePerdute.pathfinding;

import it.tungsteno.fp.rovinePerdute.teams.Team;

public class Edge {
    private final Node startNode, destinationNode;

    /**
     * Costruttore di un arco che collega un nodo di partenza a un nodo di arrivo e ne calcola il peso
     * salvando il costo di attraversamento sia lineare che con l'altezza
     * @param startNode il nodo di partenza
     * @param destinationNode il nodo di arrivo
     */
    public Edge(Node startNode, Node destinationNode) {
        this.startNode = startNode;
        this.destinationNode = destinationNode;
    }

    public Node getStartNode() {
        return startNode;
    }

    public Node getDestinationNode() {
        return destinationNode;
    }

    public double getCost(Team team) {
        return team.calculateNodeDistance(startNode, destinationNode);
    }
}

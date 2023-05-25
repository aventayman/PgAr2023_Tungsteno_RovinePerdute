package it.tungsteno.fp.rovinePerdute.teams;

import it.tungsteno.fp.rovinePerdute.pathfinding.Node;

public interface Team {
    double calculateNodeDistance(Node start, Node destination);
    String name();
}

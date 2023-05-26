package it.tungsteno.fp.rovinePerdute.teams;

import it.tungsteno.fp.rovinePerdute.pathfinding.Node;

/**
 * Interfaccia che raggruppa tipi diversi di team, ognuno caratterizzato da un nome e da un modo diverso per calcolare
 * il costo necessario per passare da un nodo a un altro
 */
public interface Team {
    double calculateNodeDistance(Node start, Node destination);
    String name();
}

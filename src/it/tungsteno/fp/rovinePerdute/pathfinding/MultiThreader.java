package it.tungsteno.fp.rovinePerdute.pathfinding;

import it.tungsteno.fp.rovinePerdute.teams.Team;

import java.util.List;

/**
 * Per ottimizzare il tempo di esecuzione del programma per ogni team viene aperto un nuovo thread che calcola
 * il percorso ottimale indipendentemente dagli altri, in modo che il tempo di esecuzione sia uguale indipendentemente
 * dal numero di team che si vuole prendere in considerazione
 */
public class MultiThreader extends Thread {
    private final RoadMap map;
    private final Team team;
    private List<Node> nodeList;

    /**
     * Costruttore di un thread, per calcolare il percorso dovrà avere a sua disposizione la mappa e il team
     * @param map la mappa su cui si vuole implementare l'algoritmo
     * @param team il team che percorre la mappa
     */
    public MultiThreader(RoadMap map, Team team) {
        this.map = map;
        this.team = team;
    }

    public List<Node> getNodeList() {
        return nodeList;
    }

    public Team getTeam() {
        return team;
    }

    @Override
    public void run() {
        nodeList = map.findShortestPath(team);
    }
}

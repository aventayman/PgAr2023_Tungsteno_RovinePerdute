package it.tungsteno.fp.rovinePerdute.pathfinding;

import it.tungsteno.fp.rovinePerdute.teams.Team;

import java.util.*;

public class ShortestPath {
    // Piccolo valore epsilon per comparare double
    private static final double EPS = 1e-6;

    // Mappa che associa a ogni indice di un nodo il nodo più conveniente finora trovato per raggiungerlo
    private static Map<Integer, Node> previousNodeMap;

    // Mappa che associa a ogni indice la minor distanza finora trovata per raggiungerlo
    private static Map<Integer, Double> minDistanceMap;

    private static Map<Integer, Double> distanceFromRuinsMap;

    /**
     * Comparatore necessario alla priorityQueue al fine di scegliere quale nodo scegliere per continuare
     * a implementare l'algoritmo, sceglierà sempre quello con il valore più basso tenendo però anche conto del fatto
     * che i nodi più vicini alle rovine perdute sono più promettenti
     */
    private static final Comparator<Integer> comparatorAStar =
            (index1, index2) -> {
                if (Math.abs((minDistanceMap.get(index1) + distanceFromRuinsMap.get(index1)) -
                        (minDistanceMap.get(index2) + distanceFromRuinsMap.get(index2))) < EPS) return 0;
                return ((minDistanceMap.get(index1) + distanceFromRuinsMap.get(index1)) -
                        (minDistanceMap.get(index2) + distanceFromRuinsMap.get(index2))) > 0 ? +1 : -1;
            };

    /**
    * Comparatore necessario alla priorityQueue al fine di scegliere quale nodo scegliere per continuare
    * a implementare l'algoritmo, sceglierà sempre quello con il valore più basso, cioè il più promettente
    */
    private static final Comparator<Integer> comparatorWithoutOptimization =
            (index1, index2) -> {
                if (Math.abs(minDistanceMap.get(index1) - minDistanceMap.get(index2)) < EPS) return 0;
                return (minDistanceMap.get(index1) - minDistanceMap.get(index2)) > 0 ? +1 : -1;
            };

    /**
     * Metodo che data una mappa e dati un nodo di partenza e un nodo di arrivo trova il percorso più corto
     * fra le due città in base al metodo di calcolo della distanza fra due luoghi
     * @param map la RoadMap su cui si vuole implementare l'algoritmo di ricerca
     * @param start il nodo di partenza
     * @param destination il nodo di arrivo
     * @param team il team per il quale si vuole calcolare il miglior percorso
     * @return la lista dei nodi percorsi per raggiungere il nodo di arrivo
     */
    public static List<Node> getShortestPath(RoadMap map, Node start, Node destination, Team team) {
        // Eseguo dijkstra e salvo la distanza in una variabile
        double dist = dijkstra(map, start, destination, team);

        // Inizializzo una nuova lista, che sarà quella dove viene immagazzinato il percorso
        List<Node> path = new ArrayList<>();

        // Se la distanza è infinita allora ritorno la lista vuota perché il nodo è irraggiungibile
        if (dist == Double.POSITIVE_INFINITY) return path;

        // Inizialmente aggiungo il nodo di destinazione
        path.add(destination);

        // Partendo dall'id del nodo di arrivo itero a ritroso all'interno della previousNodeMap in modo da ricostruire
        // il percorso finché non arrivo al nodo di partenza, che si riferisce a null, quindi so di essere arrivato
        for (int at = destination.getId(); previousNodeMap.get(at) != null; at = previousNodeMap.get(at).getId()) {
            path.add(previousNodeMap.get(at));
        }

        // Ribalto la lista in modo che sia dal nodo di partenza al nodo di arrivo
        Collections.reverse(path);

        return path;
    }

    /**
     * Algoritmo di path finding dijkstra
     * @param map la mappa su cui si vuole implementare l'algoritmo
     * @param start il nodo di partenza
     * @param destination il nodo di arrivo
     * @param team il team per il quale si vuole calcolare il miglior percorso
     * @return la distanza minore dal nodo di partenza al nodo di arrivo
     */
    private static double dijkstra(RoadMap map, Node start, Node destination, Team team) {
        List<List<Edge>> adjacencyList = map.getAdjacencyList();
        int citiesAmount = adjacencyList.size();

        // Inizializzazione delle mappe, in modo che ogni volta
        // che l'algoritmo viene richiamato si abbiano delle nuove mappe
        minDistanceMap = new HashMap<>();
        previousNodeMap = new HashMap<>();
        distanceFromRuinsMap = new HashMap<>();

        // Per ogni nodo inizializza distanceFromRuinsMap associando l'indice del nodo con la sua distanza
        // in linea d'aria dalle rovine perdute
        for (Node node : map.getCities().values()) {
            distanceFromRuinsMap.put(node.getId(),
                    Math.sqrt(Math.pow(node.getxCoordinate() - map.getRovinePerdute().getxCoordinate(), 2) +
                    Math.pow(node.getyCoordinate() - map.getRovinePerdute().getyCoordinate(), 2)));
        }

        // Array di minima distanza dal node di start
        var dist = new double[citiesAmount];

        // Tutte le distanze dal node di start inizialmente vengono impostate a POSITIVE_INFINITY
        // tranne il node di start che ha distanza da sè stesso di zero, questo viene memorizzato in entrambe le mappe
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        dist[start.getId()] = 0;
        minDistanceMap.put(start.getId(), 0.0);

        // Priority queue dove vengono inseriti i nodi da processare, verranno processati in ordine di convenienza,
        // cioè verrà scelto ogni volta il nodo che ha distanza minore dallo start.
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(2 * citiesAmount, comparatorAStar);
        // Inserisco inizialmente il nodo di start all'interno della priority queue
        priorityQueue.offer(start.getId());

        // Array usato per tenere conto dei nodi che sono già stati visitati
        boolean[] visited = new boolean[citiesAmount];

        while (!priorityQueue.isEmpty()) {
            // Scelgo il nodo più promettente prendendo l'indice dalla priority queue
            Node node = map.getNodeById(priorityQueue.poll());
            visited[node.getId()] = true;

            // Se avevamo già trovato una distanza migliore, cioè minore, rispetto a quella che
            // stiamo prendendo in considerazione ora possiamo ignorare questo nodo
            if (dist[node.getId()] < minDistanceMap.get(node.getId())) continue;

            // Prendo in considerazione gli archi del nodo che sto controllando
            List<Edge> edges = adjacencyList.get(node.getId());
            for (Edge edge : edges) {
                // Non puoi ottenere un percorso migliore da un nodo che avevi già
                // precedentemente visitato, quindi possiamo passare al successivo arco
                if (visited[edge.getDestinationNode().getId()]) continue;

                // Imposto la nuova distanza in base al modo in cui il team calcola il costo
                double newDist = dist[edge.getStartNode().getId()] + edge.getCost(team);

                // Se la nuova è minore rispetto alla migliore distanza che avevamo trovato precedentemente
                // allora aggiorno entrambe le mappe
                if (newDist < dist[edge.getDestinationNode().getId()]) {
                    // Aggiungo l'id del nodo di arrivo come key e il nodo di partenza come value in previousNodeMap
                    previousNodeMap.put(edge.getDestinationNode().getId(), edge.getStartNode());
                    // Aggiorno la distanza migliore nel array dist
                    dist[edge.getDestinationNode().getId()] = newDist;
                    // Aggiungo o aggiorno la minima distanza finora trovata nella minDistanceMap
                    minDistanceMap.put(edge.getDestinationNode().getId(), newDist);
                    // Inserisco l'id della destinazione nella priority queue
                    priorityQueue.offer(edge.getDestinationNode().getId());
                }
            }
            // Quando abbiamo visitato tutti i nodi collegati al nodo di arrivo sappiamo di per certo che
            // possiamo ritornare la distanza minima del nodo di arrivo perché non ci sarà distanza migliore di quella
            if (node.getId() == destination.getId()) {
                return dist[destination.getId()];
            }
        }

        // Il nodo di arrivo è irraggiungibile
        return Double.POSITIVE_INFINITY;
    }
}

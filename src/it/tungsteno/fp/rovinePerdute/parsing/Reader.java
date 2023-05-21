package it.tungsteno.fp.rovinePerdute.parsing;

import it.ayman.fp.lib.Menu;
import it.tungsteno.fp.rovinePerdute.pathfinding.Node;
import it.tungsteno.fp.rovinePerdute.pathfinding.RoadMap;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.*;

public class Reader {
    private static final String INPUT_DIRECTORY_PATH = "./InputData/";
    private static final String MENU_HEADER = "Select the map you want to input the data from:";
    private static final String EMPTY_DIRECTORY_MESSAGE = "No files found in the ./InputData/ directory, " +
            "add new files and rerun the program.";
    private static final String NOT_A_FILE = "Please remove the %s directory inside the ./InputData/ directory.";

    private static final String READER_ERROR = "Error during the reader initialization:";

    /**
     * Metodo per selezionare uno dei files presenti nella cartella InputData. La scelta è data dall'utente, che
     * può inserire quanti files desidera, per poi scegliere uno di essi a piacere.
     * @return una string che corrisponde al path del file scelto dall'utente
     */
    public static String getInputFilePath() {

        //File che crea il riferimento alla cartella in cui cercare i file
        File inputFolder = new File(INPUT_DIRECTORY_PATH);

        //lista dei file presenti nel file inputFolder, ovvero la cartella InputData
        File[] listOfFiles = inputFolder.listFiles();

        //se non ci sono file nella cartella, lancia un messaggio di errore e termina il programma
        if (listOfFiles.length == 0) throw new AssertionError(EMPTY_DIRECTORY_MESSAGE);

        //creo una lista di stringhe che servirà per creare il menù di scelta da stampare
        String[] fileNames = new String[listOfFiles.length];

        int i = 0;

        //Per ogni file nella cartella, controllo che l'oggetto controllato sia un file.
        for (File file : listOfFiles) {
            //In caso contrario, termino il programma e lancio un messaggio di errore.
            if (!file.isFile()) throw new AssertionError(String.format(NOT_A_FILE, file.getName()));
            //Se si tratta di un file, aggiungo il nome del file nella lista di nomi
            fileNames[i++] = file.getName();
        }

        //Creo il menù con i nomi dei files
        Menu fileMenu = new Menu(MENU_HEADER, fileNames);

        //Salvo il numero scelto dall'utente, corrispondente ad uno dei file
        int choice = fileMenu.choose(true, false) - 1;

        return listOfFiles[choice].getPath();
    }


    /**
     * Metodo che aggiunge ad una città un nuovo collegamento con un altra città, tramite indici
     * @param startNode nodo a cui aggiungere il nodo di destinazione
     * @param destinationNode nodo di destinazione
     * @param nodeRoutesMap mappa in cui sono presenti i nodi collegati
     */
    private static void addEdge(int startNode, int destinationNode, Map<Integer, List<Integer>> nodeRoutesMap) {
        try {
            nodeRoutesMap.get(startNode).add(destinationNode);
        } catch(NullPointerException e) {
            nodeRoutesMap.put(startNode, new LinkedList<>());
            nodeRoutesMap.get(startNode).add(destinationNode);
        }
    }

    /**
     * Metodo che crea un oggetto RoadMap leggendo il file XML fornito
     * @param xmlFilePath il path del file da leggere
     * @return un oggetto Roadmap con le città e i loro collegamenti
     * @throws XMLStreamException
     */
    public static RoadMap createRoadMapFromXML(String xmlFilePath) throws XMLStreamException {

        //Inizializzazione degli oggetti per il parsing di lettura
        XMLInputFactory xmlInputFactory;
        XMLStreamReader nodeXmlReader = null;

        try {
            xmlInputFactory = XMLInputFactory.newInstance();
            nodeXmlReader = xmlInputFactory.createXMLStreamReader(xmlFilePath, new FileInputStream(xmlFilePath));
        } catch (Exception e) {
            System.out.println(READER_ERROR);
            System.out.println(e.getMessage());
        }

        //Creazione delle mappe che creeranno l'oggetto RoadMap in uscita
        var nodesMap = new HashMap<Integer, Node>();
        var nodeRoutesMap = new HashMap<Integer, List<Integer>>();

        boolean running = true;

        //variabile che tiene conto dell'id del nodo in cui mi trovo, utile per tenere in memoria il nodo corrente
        int currentId = 0;

        while (running){
            //Controllo l'evento in cui mi trovo e agisco di conseguenza
            switch(nodeXmlReader.getEventType()) {
                case (XMLEvent.START_ELEMENT) -> {
                    String tag = nodeXmlReader.getLocalName();
                    switch (tag) {
                        //Nel caso sia uno start element e un tag "city", creo la nuova città (nodo) con i dati forniti
                        case ("city") -> {
                            currentId = Integer.parseInt(nodeXmlReader.getAttributeValue(0));
                            String name = nodeXmlReader.getAttributeValue(1);
                            int xCoordinate = Integer.parseInt(nodeXmlReader.getAttributeValue(2));
                            int yCoordinate = Integer.parseInt(nodeXmlReader.getAttributeValue(3));
                            int height = Integer.parseInt(nodeXmlReader.getAttributeValue(4));

                            //Una volta creato il nuovo nodo, lo aggiungo alla nodesMap, assieme al suo id
                            Node node = new Node(name, xCoordinate, yCoordinate, height);
                            nodesMap.put(currentId, node);
                        }
                        //Nel caso sia uno start element con tag "link", devo aggiungere il collegamento al nodo
                        //corrispondente al currentId
                        case ("link") -> {
                            //variabile che memorizza l'indice della destinazione (città) a cui è connesso il nodo con
                            //il currentId
                            int destinationNode = Integer.parseInt(nodeXmlReader.getAttributeValue(0));
                            addEdge(currentId, destinationNode, nodeRoutesMap);
                        }
                    }

                }
                //Se si tratta di un tag di chiusura
                case (XMLEvent.END_ELEMENT) -> {
                    //Se è il closing-tag del file, termina il parsing di lettura
                    if (nodeXmlReader.getLocalName().equals("map"))
                        running = false;
                }
            }
            //Questa condizione per evitare un exception perché ci si trova alla fine del file
            if (nodeXmlReader.hasNext())
                nodeXmlReader.next();
        }

        return new RoadMap(nodesMap, nodeRoutesMap);
    }

}

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

    public static String getInputFilePath() {
        File inputFolder = new File(INPUT_DIRECTORY_PATH);

        File[] listOfFiles = inputFolder.listFiles();

        if (listOfFiles.length == 0) throw new AssertionError(EMPTY_DIRECTORY_MESSAGE);

        String[] fileNames = new String[listOfFiles.length];

        int i = 0;
        for (File file : listOfFiles) {
            if (!file.isFile()) throw new AssertionError(String.format(NOT_A_FILE, file.getName()));
            fileNames[i++] = file.getName();
        }

        Menu fileMenu = new Menu(MENU_HEADER, fileNames);

        int choice = fileMenu.choose(true, false) - 1;

        return listOfFiles[choice].getPath();
    }


    /**
     *
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

    public static RoadMap createRoadMapFromXML(String xmlFilePath) throws XMLStreamException {

        XMLInputFactory xmlInputFactory;
        XMLStreamReader nodeXmlReader = null;

        try {
            xmlInputFactory = XMLInputFactory.newInstance();
            nodeXmlReader = xmlInputFactory.createXMLStreamReader(xmlFilePath, new FileInputStream(xmlFilePath));
        } catch (Exception e) {
            System.out.println(READER_ERROR);
            System.out.println(e.getMessage());
        }

        var nodesMap = new HashMap<Integer, Node>();
        var nodeRoutesMap = new HashMap<Integer, List<Integer>>();

        boolean running = true;

        int id = 0;

        while (running){
            switch(nodeXmlReader.getEventType()) {
                case (XMLEvent.START_ELEMENT) -> {
                    String tag = nodeXmlReader.getLocalName();
                    switch (tag) {
                        case ("city") -> {
                            id = Integer.parseInt(nodeXmlReader.getAttributeValue(0));
                            String name = nodeXmlReader.getAttributeValue(1);
                            int xCoordinate = Integer.parseInt(nodeXmlReader.getAttributeValue(2));
                            int yCoordinate = Integer.parseInt(nodeXmlReader.getAttributeValue(3));
                            int height = Integer.parseInt(nodeXmlReader.getAttributeValue(4));

                            Node node = new Node(id, name, xCoordinate, yCoordinate, height);
                            nodesMap.put(id, node);
                        }
                        case ("link") -> {
                            int destinationNode = Integer.parseInt(nodeXmlReader.getAttributeValue(0));
                            addEdge(id, destinationNode, nodeRoutesMap);
                        }
                    }

                }
                //Se si tratta di un tag di chiusura
                case (XMLEvent.END_ELEMENT) -> {
                    //Se è il closing-tag del comune allora termina di controllare per lo specifico comune
                    if (nodeXmlReader.getLocalName().equals("map"))
                        running = false;
                }
            }
            //Questa condizione serve in modo che non dia un errore perché ci si trova alla fine del file
            if (nodeXmlReader.hasNext())
                nodeXmlReader.next();
        }

        return new RoadMap(nodesMap, nodeRoutesMap);
    }
}

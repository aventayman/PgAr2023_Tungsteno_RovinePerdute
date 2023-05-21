package it.tungsteno.fp.rovinePerdute.parsing;

import it.ayman.fp.lib.Menu;
import it.tungsteno.fp.rovinePerdute.pathfinding.Node;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.XMLEvent;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
     * Metodo per aggiungere un nodo all'arraylist di linkedList.
     * Ad ogni nodo è assegnata una linkedList
     * @param node il nodo da aggiungere
     * @param list l'arraylist a cui aggiungere il nodo
     */

    private static void addNode(Node node, ArrayList<LinkedList<Node>> list) {
        LinkedList<Node> currentNodeList = new LinkedList<>();
        currentNodeList.add(node);
        list.add(currentNodeList);
    }


    /**
     * Metodo che aggiunge un nodo alla linkedList di un altro, ovvero ne crea il collegamento
     * @param start nodo a cui aggiungere il nodo di destinazione
     * @param destination nodo di destinazione
     * @param list arraylist in cui sono presenti i nodi
     */
    private static void addEdge(int start, int destination, ArrayList<LinkedList<Node>> list) {
        Node destinationNode = list.get(destination).get(0);
        list.get(start).add(destinationNode);
    }

    /**
     * Metodo che legge il file XML e crea l'adjacencyList con i nodi
     * @param xmlFilePath
     * @throws XMLStreamException
     */

    public static ArrayList<LinkedList<Node>> createNodesAdjacencyListFromXML (String xmlFilePath) throws XMLStreamException {
        XMLInputFactory xmlInputFactory = null;
        XMLStreamReader NodeXmlReader = null;

        try {
            xmlInputFactory = XMLInputFactory.newInstance();
            NodeXmlReader = xmlInputFactory.createXMLStreamReader(xmlFilePath, new FileInputStream(xmlFilePath));
        } catch (Exception e) {
            System.out.println(READER_ERROR);
            System.out.println(e.getMessage());
        }

        ArrayList<LinkedList<Node>> nodeList = new ArrayList<>();
        boolean running = true;

        while (running){
            switch(NodeXmlReader.getEventType()) {
                case (XMLEvent.START_ELEMENT) -> {
                    String tag = NodeXmlReader.getLocalName();
                    NodeXmlReader.next();
                    int id = 0;
                    switch (tag) {
                        case "city":
                            id = Integer.parseInt(NodeXmlReader.getAttributeValue(0));
                            String name = NodeXmlReader.getAttributeLocalName(1);
                            int xCoordinate = Integer.parseInt(NodeXmlReader.getAttributeValue(2));
                            int yCoordinate = Integer.parseInt(NodeXmlReader.getAttributeValue(3));
                            int height = Integer.parseInt(NodeXmlReader.getAttributeValue(4));
                            Node node = new Node(id, name, xCoordinate, yCoordinate, height);
                            addNode(node, nodeList);

                        case "link":
                            int destination = Integer.parseInt(NodeXmlReader.getText());
                            addEdge(id, destination, nodeList);
                    }

                }
                //Se si tratta di un tag di chiusura
                case (XMLEvent.END_ELEMENT) -> {
                    //Se è il closing-tag del comune allora termina di controllare per lo specifico comune
                    if (NodeXmlReader.getLocalName().equals("map"))
                        running = false;

                    //In ogni caso se è un closing-tag andare al successivo
                    NodeXmlReader.next();
                }
            }
            //Questa condizione serve in modo che non dia un errore perché ci si trova alla fine del file
            if (NodeXmlReader.hasNext())
                NodeXmlReader.next();
        }

        return nodeList;
    }

}

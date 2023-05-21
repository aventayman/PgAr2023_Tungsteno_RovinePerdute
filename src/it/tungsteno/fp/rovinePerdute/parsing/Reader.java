package it.tungsteno.fp.rovinePerdute.parsing;

import it.ayman.fp.lib.Menu;
import it.tungsteno.fp.rovinePerdute.pathfinding.Node;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;
import java.io.File;
import java.io.FileInputStream;
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

    public static List<List<Node>> createNodeAdjacencyList(String xmlFilePath) {
        try {
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
            XMLStreamReader NodeXmlReader =
                    xmlInputFactory.createXMLStreamReader(xmlFilePath, new FileInputStream(xmlFilePath));
        } catch (Exception e) {
            System.out.println(READER_ERROR);
            System.out.println(e.getMessage());
        }

        return null;
    }
}

package it.tungsteno.fp.rovinePerdute;

import it.tungsteno.fp.rovinePerdute.parsing.Reader;

import javax.xml.stream.XMLStreamException;

public class Main {
    public static void main(String[] args) throws XMLStreamException {
        System.out.println(Reader.createNodesAdjacencyListFromXML(Reader.getInputFilePath()));
    }
}

package it.tungsteno.fp.rovinePerdute.parsing;

import it.tungsteno.fp.rovinePerdute.pathfinding.Node;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;

public class Writer {
    public static void output (Node[] TonatiuhNodeList, Node[] MetztliNodeList, double gasTonatiuh, double gasMetztli) throws XMLStreamException {
        //Inizializzazione
        XMLOutputFactory xmlOutputFactory;
        XMLStreamWriter xmlStreamWriter = null;

        //Percorso del file di output
        String pathOutput = "./OutputData/Routes.xml";

        try {
            xmlOutputFactory = XMLOutputFactory.newInstance();
            xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(new FileOutputStream(pathOutput), "utf-8");
            xmlStreamWriter.writeStartDocument("utf-8", "1.0");
        } catch (Exception e) {
            System.out.println("Errore nell'inizializzazione del writer:");
            System.out.println(e.getMessage());
        }

        //try{
            assert xmlStreamWriter != null;
            xmlStreamWriter.writeStartElement("routes");//Scrittura del tag dell'xml
            xmlStreamWriter.writeStartElement("route");
            xmlStreamWriter.writeAttribute("team", "Tonatiuh");
            xmlStreamWriter.writeAttribute("cost", Integer.toString((int) gasTonatiuh));
            xmlStreamWriter.writeAttribute("cities", Integer.toString(TonatiuhNodeList.length));

            for (Node node : TonatiuhNodeList) {
                xmlStreamWriter.writeStartElement("city");
                xmlStreamWriter.writeAttribute("id", Integer.toString(node.getId()));
                xmlStreamWriter.writeAttribute("name", node.getName());
                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeEndElement();


            xmlStreamWriter.writeStartElement("route");
            xmlStreamWriter.writeAttribute("team", "Metztli");
            xmlStreamWriter.writeAttribute("cost", Integer.toString((int) gasMetztli));
            xmlStreamWriter.writeAttribute("cities", Integer.toString(MetztliNodeList.length));

            for (Node node : MetztliNodeList) {
                xmlStreamWriter.writeStartElement("city");
                xmlStreamWriter.writeAttribute("id", Integer.toString(node.getId()));
                xmlStreamWriter.writeAttribute("name", node.getName());
                xmlStreamWriter.writeEndElement();
            }
            xmlStreamWriter.writeEndElement();
            xmlStreamWriter.writeEndDocument();

            xmlStreamWriter.flush(); // svuota il buffer
            xmlStreamWriter.close(); // chiusura del documento e delle risorse impiegate

        /*
        } catch (Exception e){
            System.out.println("Errore nella scrittura");
        }

         */
    }
}

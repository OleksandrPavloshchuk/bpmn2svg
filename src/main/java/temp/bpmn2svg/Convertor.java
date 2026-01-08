package temp.bpmn2svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import temp.bpmn2svg.bpmn.Definitions;
import temp.bpmn2svg.bpmn.Process;
import temp.bpmn2svg.svg.SvgElementsSizes;
import temp.bpmn2svg.svg.SvgPoint;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public record Convertor(
        InputStream input,
        OutputStream output) {

    private static final String SVG_STYLE =
            "text {font-family: Arial, sans serif; font-size: 8pt; text-anchor:middle; dominant-baseline: middle;}";

    public void apply() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        writeDocumentToOutput(
                convert(
                        buildDocumentFromInput()
                )
        );
        input.close();
        output.close();
    }

    private Document buildDocumentFromInput() throws IOException, ParserConfigurationException, SAXException {

        try (InputStream in = Files.newInputStream(Path.of("/home/oleksandr/personal/java/bpmn2svg/samples/process.bpmn"))) {
            DocumentBuilder builder = getDocumentBuilder();
            return builder.parse(in);
        }
        // TODO restore it for CLI
        //final DocumentBuilder builder = getDocumentBuilder();
        //return builder.parse(input);
    }


    private void writeDocumentToOutput(Document src) throws TransformerException {
        final DOMSource domSource = new DOMSource(src);
        final StreamResult streamResult = new StreamResult(output);
        final TransformerFactory transformerFactory = TransformerFactory.newInstance();
        final Transformer transformer = transformerFactory.newTransformer();
        transformer.transform(domSource, streamResult);
    }

    private Document convert(Document src) throws ParserConfigurationException {

        final Definitions definitions = new ConvertFromDomToDefinitions().apply(src);
        final NodesDistributor nodesDistributor = new NodesDistributor(definitions);
        nodesDistributor.distribute();

        final Map<String, SvgPoint> coordinates = new TranslatorToSvgCoordinates().apply(nodesDistributor.getPositions());

        final Document doc = createSvgDocument(nodesDistributor);

        // TODO we process only the 1st process
        final Process process = definitions.processes().getFirst();

        new ConvertFromProcessToDom(doc, coordinates, process).convert();

        return doc;
    }

    private static Document createSvgDocument(NodesDistributor nodesDistributor) throws ParserConfigurationException {
        final DocumentBuilder documentBuilder = getDocumentBuilder();
        final Document doc = documentBuilder.newDocument();
        final Element root = doc.createElement("svg");
        root.setAttribute("xmlns", "http://www.w3.org/2000/svg");
        doc.appendChild(root);

        final int width = (nodesDistributor.getMaxCol() + 2) * SvgElementsSizes.X_STEP;
        final int height = (nodesDistributor.getMaxRow() + 2) * SvgElementsSizes.Y_STEP;

        final String viewBox = "0 0 " + width + " " + height;
        root.setAttribute("viewBox", viewBox);

        root.appendChild(createStyleElement(doc));
        root.appendChild(createDefsElement(doc));

        return doc;
    }

    private static Element createDefsElement(Document doc) {
        final Element elemDefs =  doc.createElement("defs");

        final Element elemMarker = doc.createElement("marker");
        elemDefs.appendChild(elemMarker);
        elemMarker.setAttribute("id", "arrow");
        elemMarker.setAttribute("viewBox", "0 0 20 20");
        elemMarker.setAttribute("refX", "20");
        elemMarker.setAttribute("refY", "10");
        elemMarker.setAttribute("markerWidth", "12");
        elemMarker.setAttribute("markerHeight","12");
        elemMarker.setAttribute("orient","auto-start-reverse");

        final Element elemPath = doc.createElement("path");
        elemMarker.appendChild(elemPath);
        elemPath.setAttribute("fill", "gray");
        elemPath.setAttribute("d", "M 0 0 L 20 10 L 0 20 Z");
        return elemDefs;
    }

    private static Element createStyleElement(Document doc) {
        final Element elemStyle = doc.createElement("style");
        elemStyle.setAttribute("type", "text/css");
        elemStyle.appendChild( doc.createTextNode(SVG_STYLE));
        return elemStyle;
    }

    private static DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        return factory.newDocumentBuilder();
    }

}


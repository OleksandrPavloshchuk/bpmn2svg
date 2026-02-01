package temp.bpmn2svg.translate;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import temp.bpmn2svg.bpmn.Definitions;
import temp.bpmn2svg.bpmn.Process;
import temp.bpmn2svg.svg.SvgBaseDocumentBuilder;
import temp.bpmn2svg.svg.SvgPoint;
import temp.bpmn2svg.utils.XmlUtils;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public record TranslateBpmn2Svg(
        InputStream input,
        OutputStream output) {

    public void apply() throws IOException, ParserConfigurationException, SAXException, TransformerException {
        XmlUtils.writeDocumentToOutput(
                translateBpmn2Svg(
                        buildDocumentFromInput()
                ),
                output
        );
        input.close();
        output.close();
    }

    private Document buildDocumentFromInput() throws IOException, ParserConfigurationException, SAXException {

        try (InputStream in = Files.newInputStream(Path.of("/home/oleksandr/personal/java/bpmn2svg/samples/process.bpmn"))) {
            return XmlUtils.getDocumentBuilder().parse(in);
        }
        // TODO restore it for CLI
        //return getDocumentBuilder().parse(input);
    }

    private Document translateBpmn2Svg(Document src) throws ParserConfigurationException {
        final Definitions definitions = new TranslateBpmn2DefinitionsJavaObject().apply(src);
        final DistributeNodes distributeNodes = new DistributeNodes(definitions).perform();
        // TODO we use only the 1st process
        final Process process = definitions.processes().getFirst();
        final Map<String, SvgPoint> coordinates = new TranslateDistribution2SvgCoordinates(
                process, distributeNodes
        ).apply();

        final Document result = new SvgBaseDocumentBuilder(distributeNodes.getMaxCol(), distributeNodes.getMaxRow())
                .build();

        new TranslateProcess2Svg(
                result,
                coordinates,
                distributeNodes.getLaneOffsets(),
                distributeNodes.getLaneWidths(),
                distributeNodes.getMaxRow(),
                process
        ).convert();
        return result;
    }

}


package temp.bpmn2svg.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static temp.bpmn2svg.svg.SvgElementsSizes.CROSS_SIZE;

public record SvgText(
        SvgPoint center,
        String text
) implements SvgObject {

    @Override
    public Element createElement(Document doc) {
        final Element result = doc.createElement("text");
        result.setAttribute("fill", "black");
        result.setAttribute("x", String.valueOf(center.x()));
        result.setAttribute("y", String.valueOf(center.y()));
        result.appendChild(doc.createTextNode(text));
        return result;
    }
}

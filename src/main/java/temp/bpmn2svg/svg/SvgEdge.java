package temp.bpmn2svg.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public record SvgEdge(
        SvgPoint source, SvgPoint target
) implements SvgObject {

    @Override
    public SvgPoint center() {
        return new SvgPoint(
                (source.x() + target.x()) / 2,
                (source.y() + target.y()) / 2
        );
    }

    @Override
    public Element createElement(Document doc) {
        final Element result = doc.createElement("line");
        result.setAttribute("x1", String.valueOf(source.x()));
        result.setAttribute("x2", String.valueOf(target.x()));
        result.setAttribute("y1", String.valueOf(source.y()));
        result.setAttribute("y2", String.valueOf(target.y()));
        result.setAttribute("stroke", "gray");
        result.setAttribute("marker-end", "url(#arrow)");
        return result;
    }
}

package temp.bpmn2svg.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static temp.bpmn2svg.svg.SvgElementsSizes.X_SIZE;

public record SvgX(
        SvgPoint center
) implements SvgObject {

    @Override
    public Element createElement(Document doc) {
        final int cx = center.x();
        final int cy = center.y();
        final Element result = doc.createElement("path");

        StringBuilder sb = new StringBuilder()
                .append("M")
                .append(cx + X_SIZE).append(" ").append(cy - X_SIZE).append(" ")
                .append("L")
                .append(cx - X_SIZE).append(" ").append(cy + X_SIZE).append(" ")
                .append("M")
                .append(cx - X_SIZE).append(" ").append(cy - X_SIZE).append(" ")
                .append("L")
                .append(cx + X_SIZE).append(" ").append(cy + X_SIZE).append(" ")
                .append("Z");

        result.setAttribute("d", sb.toString());
        result.setAttribute("stroke", "lightgray");
        result.setAttribute("stroke-width", "10");
        return result;
    }
}

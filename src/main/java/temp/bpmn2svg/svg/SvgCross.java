package temp.bpmn2svg.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static temp.bpmn2svg.svg.SvgElementsSizes.CROSS_SIZE;
import static temp.bpmn2svg.svg.SvgElementsSizes.X_SIZE;

public record SvgCross(
        SvgPoint center
) implements SvgObject {

    @Override
    public Element createElement(Document doc) {
        final int cx = center.x();
        final int cy = center.y();
        final Element elemX = doc.createElement("path");
        StringBuilder sb = new StringBuilder()
                .append("M")
                .append(cx).append(" ").append(cy - CROSS_SIZE).append(" ")
                .append("L")
                .append(cx).append(" ").append(cy + CROSS_SIZE).append(" ")
                .append("M")
                .append(cx - CROSS_SIZE).append(" ").append(cy).append(" ")
                .append("L")
                .append(cx + CROSS_SIZE).append(" ").append(cy).append(" ")
                .append("Z");

        elemX.setAttribute("d", sb.toString());
        elemX.setAttribute("stroke", "lightgray");
        elemX.setAttribute("stroke-width", "10");
        return elemX;
    }
}

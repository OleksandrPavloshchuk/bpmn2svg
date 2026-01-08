package temp.bpmn2svg.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static temp.bpmn2svg.svg.SvgElementsSizes.*;

public record SvgDiamond(
        SvgPoint center
) implements SvgShape {

    @Override
    public SvgPoint intersectLineFrom(SvgPoint start) {
        int y = center.y();
        int x = center.x();
        final int s = GATEWAY_SIZE / 2;
        if (x < start.x()) {
            x += s;
        } else if (x > start.x()) {
            x -= s;
        } else {
            if (y < start.y()) {
                y += s;
            } else if (y > start.y()) {
                y -= s;
            }
        }
        return new SvgPoint(x, y);
    }

    @Override
    public Element createElement(Document doc) {
        final Element result = doc.createElement("path");
        final int cx = center.x();
        final int cy = center.y();
        final int s = GATEWAY_SIZE / 2;
        final StringBuilder sb = new StringBuilder();
        sb.append("M")
                .append(cx).append(" ")
                .append(cy - s).append(" ");
        sb.append("L")
                .append(cx + s).append(" ")
                .append(cy).append(" ");
        sb.append("L")
                .append(cx).append(" ")
                .append(cy + s).append(" ");
        sb.append("L")
                .append(cx - s).append(" ")
                .append(cy).append(" ");
        sb.append("L")
                .append(cx).append(" ")
                .append(cy - s).append(" Z");

        result.setAttribute("d", sb.toString());
        result.setAttribute("stroke", "black");
        result.setAttribute("fill", "white");
        return result;
    }
}

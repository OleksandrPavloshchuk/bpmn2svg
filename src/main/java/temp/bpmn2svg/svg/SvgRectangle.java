package temp.bpmn2svg.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static temp.bpmn2svg.svg.SvgElementsSizes.TASK_HEIGHT;
import static temp.bpmn2svg.svg.SvgElementsSizes.TASK_WIDTH;

public record SvgRectangle(
        SvgPoint center
) implements SvgShape {

    @Override
    public SvgPoint intersectLineFrom(SvgPoint start) {
        int y = center.y();
        int x = center.x();
        final int h = TASK_HEIGHT / 2;
        if (y < start.y()) {
            y += h;
        } else if (y > start.y()) {
            y -= h;
        } else {
            final int w = TASK_WIDTH / 2;
            if (x < start.x()) {
                x += w;
            } else if (x > start.x()) {
                x -= w;
            }
        }
        return new SvgPoint(x, y);
    }

    @Override
    public Element createElement(Document doc) {
        final org.w3c.dom.Element result = doc.createElement("rect");
        result.setAttribute("x", String.valueOf(center.x() - TASK_WIDTH / 2));
        result.setAttribute("y", String.valueOf(center.y() - TASK_HEIGHT / 2));
        result.setAttribute("width", String.valueOf(TASK_WIDTH));
        result.setAttribute("height", String.valueOf(TASK_HEIGHT));
        result.setAttribute("stroke-width", "1");
        result.setAttribute("stroke", "black");
        result.setAttribute("fill", "white");
        return result;
    }

}

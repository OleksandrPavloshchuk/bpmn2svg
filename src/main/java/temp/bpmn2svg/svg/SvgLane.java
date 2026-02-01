package temp.bpmn2svg.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static temp.bpmn2svg.svg.SvgElementsSizes.TASK_HEIGHT;
import static temp.bpmn2svg.svg.SvgElementsSizes.TASK_WIDTH;

public record SvgLane(
        int offset,
        int width,
        int height
) {

    public Element createElement(Document doc) {
        final Element result = doc.createElement("rect");
        result.setAttribute("x", String.valueOf( (offset - 0.5) * SvgElementsSizes.X_STEP));
        result.setAttribute("y", String.valueOf( (-1) * SvgElementsSizes.Y_STEP));
        result.setAttribute("width", String.valueOf(width * SvgElementsSizes.X_STEP));
        result.setAttribute("height", String.valueOf((height + 2.5) * SvgElementsSizes.Y_STEP));
        result.setAttribute("stroke-width", "1");
        result.setAttribute("stroke", "lightgray");
        result.setAttribute("fill", "white");
        return result;
    }

}

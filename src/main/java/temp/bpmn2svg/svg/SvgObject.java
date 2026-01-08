package temp.bpmn2svg.svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public interface SvgObject {
    SvgPoint center();
    Element createElement(Document doc);
}

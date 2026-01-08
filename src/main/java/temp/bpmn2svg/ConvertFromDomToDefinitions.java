package temp.bpmn2svg;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import temp.bpmn2svg.bpmn.*;
import temp.bpmn2svg.bpmn.Process;

import java.util.*;
import java.util.function.Function;

public class ConvertFromDomToDefinitions
        implements Function<Document, Definitions> {

    @Override
    public Definitions apply(Document document) {
        final Element root = document.getDocumentElement();
        BpmnObjectType.DEFINITIONS.verify(root.getLocalName());
        return new Definitions(getId(root), getChildren(root, this::toProcess));
    }

    private <T> List<T> getChildren(Element root, Function<Element, T> mapper) {
        final List<T> result = new ArrayList<>();
        Node node = root.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                result.add(mapper.apply((Element) node));
            }
            node = node.getNextSibling();
        }
        return result;
    }

    private Process toProcess(Element root) {
        BpmnObjectType.PROCESS.verify(root.getLocalName());

        final List<BpmnObject> children = getChildren(root, this::toProcessObject);
        final Map<String, BpmnObject> map = new TreeMap<>();
        children.forEach(obj -> map.put(obj.id(), obj));

        return new Process(getId(root), getName(root), map);
    }

    private BpmnObject toProcessObject(Element root) {
        final BpmnObjectType type = BpmnObjectType.findByDiagramName(root.getLocalName())
                .orElseThrow(() -> new IllegalArgumentException("Unexpected type name: " + root.getLocalName()));

        return switch (type) {
            case PROCESS, DEFINITIONS -> throw new IllegalArgumentException("Unexpected type: " + type);
            case START_EVENT -> toStartEvent(root);
            case END_EVENT -> toEndEvent(root);
            case USER_TASK -> toUserTask(root);
            case SERVICE_TASK -> toServiceTask(root);
            case EXCLUSIVE_GATEWAY -> toExclusiveGateway(root);
            case PARALLEL_GATEWAY -> toParallelGateway(root);
            case SEQUENCE_FLOW -> toSequenceFlow(root);
        };
    }

    private StartEvent toStartEvent(Element root) {
        return new StartEvent(getId(root), getName(root));
    }

    private EndEvent toEndEvent(Element root) {
        return new EndEvent(getId(root), getName(root));
    }

    private UserTask toUserTask(Element root) {
        return new UserTask(getId(root), getName(root));
    }

    private ServiceTask toServiceTask(Element root) {
        return new ServiceTask(getId(root), getName(root),
                root.getAttribute("camunda:class")
        );
    }

    private ExclusiveGateway toExclusiveGateway(Element root) {
        return new ExclusiveGateway(getId(root), getName(root));
    }

    private ParallelGateway toParallelGateway(Element root) {
        return new ParallelGateway(getId(root));
    }

    private SequenceFlow toSequenceFlow(Element root) {
        final String id = getId(root);
        final String sourceRef = root.getAttribute("sourceRef");
        final String targetRef = root.getAttribute("targetRef");

        return getFirstChild(root, "conditionExpression")
                .map(elem -> elem.getTextContent().trim())
                .map(expr -> new SequenceFlow(id, sourceRef, targetRef, expr))
                .orElse(new SequenceFlow(id, sourceRef, targetRef));
    }

    private Optional<Element> getFirstChild(Element root, String tag) {
        Node node = root.getFirstChild();
        while (node != null) {
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element element = (Element) node;
                if (element.getLocalName().equals(tag)) {
                    return Optional.of(element);
                }
            }
            node = node.getNextSibling();
        }
        return Optional.empty();
    }

    private static String getId(Element elem) {
        return elem.getAttribute("id");
    }

    private static String getName(Element elem) {
        return elem.getAttribute("name");
    }

}

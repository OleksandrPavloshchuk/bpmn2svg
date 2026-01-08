package temp.bpmn2svg.translate;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import temp.bpmn2svg.bpmn.*;
import temp.bpmn2svg.bpmn.Process;
import temp.bpmn2svg.svg.*;

import java.util.*;
import java.util.List;
import java.util.function.Function;

public record TranslateProcess2Svg(
        Document doc,
        Map<String, SvgPoint> coordinates,
        Process process) {

    public void convert() {
        final Element root = doc.getDocumentElement();
        final Set<String> ids = process.bpmnObjects().keySet();
        ids.forEach(id -> {
            final List<Element> elements = convert(getBpmnObject(id));
            elements.forEach(root::appendChild);
        });
    }

    private BpmnObject getBpmnObject(String id) {
        return process.bpmnObjects().get(id);
    }

    private SvgShape getShape(String id) {
        final BpmnObject bpmnObject = getBpmnObject(id);
        final SvgPoint center = getCenter(bpmnObject);
        return switch (bpmnObject.getType()) {
            case DEFINITIONS, PROCESS, SEQUENCE_FLOW ->
                    throw new IllegalArgumentException("Unexpected type :" + bpmnObject.getType());
            case END_EVENT, START_EVENT -> new SvgCircle(center, 1);
            case USER_TASK, SERVICE_TASK -> new SvgRectangle(center);
            case EXCLUSIVE_GATEWAY, PARALLEL_GATEWAY -> new SvgDiamond(center);
        };
    }

    private List<Element> convert(BpmnObject bpmnObject) {
        return switch (bpmnObject.getType()) {
            case PROCESS, DEFINITIONS -> List.of();
            case SEQUENCE_FLOW -> convert((SequenceFlow) bpmnObject);
            case START_EVENT -> convert((StartEvent) bpmnObject);
            case END_EVENT -> convert((EndEvent) bpmnObject);
            case EXCLUSIVE_GATEWAY -> convert((ExclusiveGateway) bpmnObject);
            case PARALLEL_GATEWAY -> convert((ParallelGateway) bpmnObject);
            case SERVICE_TASK -> convert((ServiceTask) bpmnObject);
            case USER_TASK -> convert((UserTask) bpmnObject);
        };
    }

    private List<Element> convert(SequenceFlow src) {
        final SvgPoint targetCenter = getCenter(getBpmnObject(src.targetRef()));

        final SvgShape sourceShape = getShape(src.sourceRef());
        final SvgPoint source = sourceShape.intersectLineFrom(targetCenter);

        final SvgShape targetShape = getShape(src.targetRef());
        final SvgPoint target = targetShape.intersectLineFrom(source);

        final SvgEdge edge = new SvgEdge(source, target);
        final Element edgeElem = edge.createElement(doc);

        final List<Element> result = new ArrayList<>();
        result.add(edgeElem);
        String cond = src.conditionalExpression();
        if (cond != null) {
            cond = cond.replace("${", "").replace("}", "");
            result.add(convertToText(edge.center(), cond));
        }
        return result;
    }

    private List<Element> convert(EndEvent src) {
        return List.of(
                convertToCircle(src, 3),
                convertToText(src)
        );
    }

    private List<Element> convert(ExclusiveGateway src) {
        return List.of(
                convertTo(src, SvgDiamond::new),
                convertTo(src, SvgX::new),
                convertToText(src)
        );
    }

    private List<Element> convert(ParallelGateway src) {
        return List.of(
                convertTo(src, SvgDiamond::new),
                convertTo(src, SvgCross::new),
                convertToText(getCenter(src), src.id())
        );
    }

    private List<Element> convert(ServiceTask src) {
        return List.of(
                convertTo(src, SvgRectangle::new),
                convertToText(src)
        );
    }

    private List<Element> convert(UserTask src) {
        return List.of(
                convertTo(src, SvgRectangle::new),
                convertToText(src)
        );
    }

    private List<Element> convert(StartEvent src) {
        return List.of(
                convertToCircle(src, 1),
                convertToText(src)
        );
    }

    private Element convertToCircle(BpmnObject src, int borderWidth) {
        return new SvgCircle(getCenter(src), borderWidth).createElement(doc);
    }

    private <T extends SvgObject> Element convertTo(
            BpmnObject src, Function<SvgPoint, T> converter) {
        return converter.apply(getCenter(src)).createElement(doc);
    }

    private Element convertToText(NamedBpmnObject bpmnObject) {
        return convertToText(getCenter(bpmnObject), bpmnObject.name());
    }

    private Element convertToText(SvgPoint center, String text) {
        return new SvgText(center, text).createElement(doc);
    }

    private SvgPoint getCenter(BpmnObject bpmnObject) {
        return switch (bpmnObject.getType()) {
            case PROCESS, DEFINITIONS, SEQUENCE_FLOW
                    -> throw new IllegalArgumentException("Unexpected type: " + bpmnObject.getType());
            case END_EVENT, EXCLUSIVE_GATEWAY, PARALLEL_GATEWAY,
                 SERVICE_TASK, USER_TASK, START_EVENT
                    -> coordinates.get(bpmnObject.id());
        };
    }

}

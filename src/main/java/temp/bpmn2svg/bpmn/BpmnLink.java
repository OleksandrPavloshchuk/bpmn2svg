package temp.bpmn2svg.bpmn;

public interface BpmnLink extends BpmnObject {
    String sourceRef();
    String targetRef();
}

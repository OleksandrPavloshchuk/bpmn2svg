package temp.bpmn2svg.bpmn;

import java.util.List;

public record Definitions(
        String id,
        List<Process> processes
) implements BpmnObject {

    @Override
    public BpmnObjectType getType() {
        return BpmnObjectType.DEFINITIONS;
    }

}

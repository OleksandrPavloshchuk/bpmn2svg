package temp.bpmn2svg;

import temp.bpmn2svg.svg.SvgElementsSizes;
import temp.bpmn2svg.svg.SvgPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TranslatorToSvgCoordinates
        implements Function<Map<String, NodesDistributor.Position>, Map<String, SvgPoint>> {

    @Override
    public Map<String, SvgPoint> apply(Map<String, NodesDistributor.Position> src) {

        final Map<String, SvgPoint> result = new HashMap<>();
        src.forEach((id, position) -> {
            final int colsCount = getColsCount(position.row(), src);
            final double offset = ((double) colsCount) / 2;
            final double x = (position.col() + 2 - offset) * SvgElementsSizes.X_STEP;

            final SvgPoint svgPoint = new SvgPoint(
                    (int) x,
                    (1 +position.row()) * SvgElementsSizes.Y_STEP);
            result.put(id, svgPoint);
        });
        return result;
    }

    private int getColsCount(int row, Map<String, NodesDistributor.Position> src) {
        return src.values().stream()
                .filter(position -> position.row() == row)
                .map(NodesDistributor.Position::col)
                .max(Integer::compare)
                .orElse(-1) + 1;
    }
}

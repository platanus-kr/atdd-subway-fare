package subway.path.domain.handler;

import subway.line.domain.Section;
import subway.path.application.dto.PathFareCalculationInfo;

import java.util.List;

public class DistancePathFareHandler extends PathFareChain {
    private static final long FIRST_OVER_CHARGE_SECTION_BY_DISTANCE = 10L;
    private static final long FIRST_DIVISOR = 5L;
    private static final long SECOND_OVER_CHARGE_SECTION_BY_DISTANCE = 50L;
    private static final long SECOND_DIVISOR = 8L;
    private static final long REMAIN_SECTION_DISTANCE = SECOND_OVER_CHARGE_SECTION_BY_DISTANCE - FIRST_OVER_CHARGE_SECTION_BY_DISTANCE;

    @Override
    public PathFareCalculationInfo calculateFare(PathFareCalculationInfo calcInfo) {
        List<Section> sections = calcInfo.getSections();

        long totalFare = calcInfo.getFare();
        Long distance = getTotalDistanceInPath(sections);

        totalFare += calculateAdditionalFare(distance);
        PathFareCalculationInfo calcInfoResponse = calcInfo.withUpdatedFare(totalFare);

        return super.nextCalculateFare(calcInfoResponse);
    }

    private Long getTotalDistanceInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }

    private long calculateAdditionalFare(Long distance) {
        long additionalFare = 0L;

        if (distance > SECOND_OVER_CHARGE_SECTION_BY_DISTANCE) {
            additionalFare += calculateOverFare(REMAIN_SECTION_DISTANCE, FIRST_DIVISOR);
            additionalFare += calculateOverFare(distance - SECOND_OVER_CHARGE_SECTION_BY_DISTANCE, SECOND_DIVISOR);
        } else if (distance > FIRST_OVER_CHARGE_SECTION_BY_DISTANCE) {
            additionalFare += calculateOverFare(distance - FIRST_OVER_CHARGE_SECTION_BY_DISTANCE, FIRST_DIVISOR);
        }

        return additionalFare;
    }

    private long calculateOverFare(long distance, long divisor) {
        return (long) ((Math.ceil((distance - 1) / divisor) + 1) * 100);
    }
}

package subway.line.application.dto;

import lombok.Builder;
import lombok.Getter;
import subway.line.domain.Line;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class LineCreateRequest {

    private static final String DISTANCE_MIN_MESSAGE = "구간의 최소 거리는 1 이상이어야 합니다.";
    private static final String DURATION_MIN_MESSAGE = "구간의 최소 시간은 1 이상이어야 합니다.";

    @NotBlank
    private String name;

    @NotBlank
    private String color;

    @Min(value = 1)
    private Long upStationId;

    @Min(value = 1)
    private Long downStationId;

    @Min(value = 1L, message = DISTANCE_MIN_MESSAGE)
    private Long distance;

    @Min(value = 1L, message = DURATION_MIN_MESSAGE)
    private Long duration;

    @Builder.Default
    private long surcharge = 0L;

    public static Line to(LineCreateRequest request) {
        return Line.builder()
                .name(request.getName())
                .color(request.getColor())
                .surcharge(request.getSurcharge())
                .build();
    }
}

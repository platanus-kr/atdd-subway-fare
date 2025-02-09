package subway.unit.path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import subway.acceptance.station.StationFixture;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Line;
import subway.line.domain.Section;
import subway.path.domain.PathFinder;
import subway.path.domain.strategy.ShortestDistancePathFinderStrategy;
import subway.path.application.dto.PathFinderRequest;
import subway.path.domain.Path;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static subway.acceptance.station.StationFixture.getStation;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("ShortestDistancePathFinder 단위 테스트")
public class ShortestDistancePathTest {
    private PathFinder shortestDistancePathFinder;
    private Line 이호선;
    private Line 삼호선;
    private Line 신분당선;
    private List<Section> 구간목록;

    /**
     * <pre>
     * 교대역  ---- *2호선* --- dt:10, dr:5 ------  강남역
     * |                                             |
     * *3호선*                                    *신분당선*
     * dt:2, dr:3                                dt:10, dr:6
     * |                                             |
     * 남부터미널역  --- *3호선* -- dt:3, dr:15 ---- 양재역
     *
     * ex) 교대-양재
     * 최단거리 : 교대 - 남부터미널 - 양재
     * 최소시간 : 교대 - 강남 - 양재
     * </pre>
     */

    @BeforeEach
    void beforeEach() {
        ShortestDistancePathFinderStrategy shortestDistancePathStrategy = new ShortestDistancePathFinderStrategy();
        shortestDistancePathFinder = new PathFinder(shortestDistancePathStrategy);

        StationFixture.기본_역_생성();

        이호선 = Line.builder().name("2호선").color("random").build();
        Section 이호선_1구간 = Section.builder().upStation(getStation("교대역")).downStation(getStation("강남역")).distance(10L).duration(5L).build();
        이호선.addSection(이호선_1구간);

        삼호선 = Line.builder().name("2호선").color("random").build();
        Section 삼호선_1구간 = Section.builder().upStation(getStation("교대역")).downStation(getStation("남부터미널역")).distance(2L).duration(3L).build();
        삼호선.addSection(삼호선_1구간);
        Section 삼호선_2구간 = Section.builder().upStation(getStation("남부터미널역")).downStation(getStation("양재역")).distance(3L).duration(15L).build();
        삼호선.addSection(삼호선_2구간);

        신분당선 = Line.builder().name("2호선").color("random").build();
        Section 신분당선_1구간 = Section.builder().upStation(getStation("강남역")).downStation(getStation("양재역")).distance(10L).duration(6L).build();
        신분당선.addSection(신분당선_1구간);

        List<Line> lines = List.of(이호선, 삼호선, 신분당선);
        구간목록 = lines.stream()
                .flatMap(line -> line.getLineSections().getSections().stream())
                .collect(Collectors.toList());

    }


    /**
     * Given 구간이 있을 때
     * When 경로 조회를 하면
     * Then 경로 내 역이 목록으로 반환된다
     * Then 경로의 총 길이가 반환된다
     */
    @DisplayName("최단거리 경로 조회")
    @Test
    void getShortestDistancePath() {
        // when
        var pathFinderRequest = PathFinderRequest.builder().sourceStation(getStation("교대역")).targetStation(getStation("양재역")).sections(구간목록).build();
        Path shortestPath = shortestDistancePathFinder.findPath(pathFinderRequest);

        // then
        assertThat(shortestPath.getStations())
                .extracting(Station::getName)
                .containsExactlyInAnyOrder("교대역", "남부터미널역", "양재역");

        // then
        assertThat(shortestPath.getTotalDistance()).isEqualTo(5L);
    }

    /**
     * Given 구간이 있을 때
     * When 동일한 구간을 조회하면
     * Then 조회 되지 않는다.
     */
    @DisplayName("최단거리 경로 조회 : 요청 구간 동일")
    @Test
    void getShortestDistancePathWithSameOrigin() {
        // when/then
        var pathFinderRequest = PathFinderRequest.builder().sourceStation(getStation("교대역")).targetStation(getStation("교대역")).sections(구간목록).build();
        assertThatThrownBy(() -> shortestDistancePathFinder.findPath(pathFinderRequest))
                .isInstanceOf(SubwayBadRequestException.class);
    }

    /**
     * Given 구간이 있을 때
     * When 연결되지 않은 구간을 조회하면
     * Then 조회 되지 않는다.
     */
    @DisplayName("최단거리 경로 조회 : 연결되지 않은 구간")
    @Test
    void getShortestDistancePathNotConnectedSection() {
        // when/then
        var pathFinderRequest = PathFinderRequest.builder().sourceStation(getStation("교대역")).targetStation(getStation("왕십리역")).sections(구간목록).build();
        assertThatThrownBy(() -> shortestDistancePathFinder.findPath(pathFinderRequest))
                .isInstanceOf(SubwayBadRequestException.class);
    }

    /**
     * Given 구간이 있을 때
     * When 존재하지 않는 역을 조회하면
     * Then 조회 되지 않는다.
     */
    @DisplayName("최단거리 경로 조회 : 존재하지 않는 역")
    @Test
    void getShortestDistancePathNotExistStation() {
        // when/then
        var pathFinderRequest = PathFinderRequest.builder().sourceStation(new Station(99L, "그런역")).targetStation(new Station(98L, "저런역")).sections(구간목록).build();
        assertThatThrownBy(() -> shortestDistancePathFinder.findPath(pathFinderRequest))
                .isInstanceOf(SubwayBadRequestException.class);
    }

}

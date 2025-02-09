package subway.documentation;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import subway.acceptance.member.MemberFixture;
import subway.acceptance.path.PathSteps;
import subway.path.application.PathService;
import subway.path.application.dto.PathRetrieveRequest;
import subway.path.application.dto.PathRetrieveResponse;
import subway.station.application.dto.StationResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SuppressWarnings("NonAsciiCharacters")
@DisplayName("경로 기능 인터페이스 문서 테스트")
public class PathDocumentation extends Documentation {
    private static String accessToken;
    @MockBean
    private PathService pathService;

    @BeforeEach
    void beforeEach() {
        accessToken = MemberFixture.일반_회원_생성_로그인_후_토큰();
    }
    @Test
    void shortestPath() {
        // given
        StationResponse 강남역 = StationResponse.builder().id(1L).name("강남역").build();
        StationResponse 역삼역 = StationResponse.builder().id(2L).name("역삼역").build();
        PathRetrieveResponse response = PathRetrieveResponse.builder()
                .stations(List.of(강남역, 역삼역))
                .distance(10)
                .duration(10)
                .fare(1250L)
                .build();
        when(pathService.getPath(any(PathRetrieveRequest.class))).thenReturn(response);

        // when
        var apiResponse = PathSteps.getShortestPathForDocument(강남역.getId(),
                역삼역.getId(),
                this.spec,
                PathSteps.최단거리경로_필터(),
                accessToken);

        // then
        var list = apiResponse.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("강남역", "역삼역");
    }


    @Test
    void minimumTimePath() {
        // given
        StationResponse 강남역 = StationResponse.builder().id(1L).name("강남역").build();
        StationResponse 역삼역 = StationResponse.builder().id(2L).name("역삼역").build();
        PathRetrieveResponse response = PathRetrieveResponse.builder()
                .stations(List.of(강남역, 역삼역))
                .distance(10)
                .duration(10)
                .fare(1250L)
                .build();
        when(pathService.getPath(any(PathRetrieveRequest.class))).thenReturn(response);

        // when
        var apiResponse = PathSteps.getMinimumTimePathForDocument(강남역.getId(),
                역삼역.getId(),
                this.spec,
                PathSteps.최소시간경로_필터(),
                accessToken);

        // then
        var list = apiResponse.jsonPath().getList("stations.name", String.class);
        assertThat(list).containsExactlyInAnyOrder("강남역", "역삼역");
    }


}

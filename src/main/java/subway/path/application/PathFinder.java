package subway.path.application;

import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.WeightedMultigraph;
import subway.constant.SubwayMessage;
import subway.exception.SubwayBadRequestException;
import subway.line.domain.Section;
import subway.path.application.dto.PathRetrieveResponse;
import subway.path.domain.SectionEdge;
import subway.station.application.dto.StationResponse;
import subway.station.domain.Station;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathFinder {
    private static final long BASIC_FARE = 1250L;
    private final PathStrategy strategy;

    public PathFinder(PathStrategy strategy) {
        this.strategy = strategy;
    }

    public PathRetrieveResponse findPath(List<Section> sections, Station sourceStation, Station targetStation) {
        validIsSameOriginStation(sourceStation, targetStation);
        WeightedMultigraph<Station, SectionEdge> graph = getGraph(sections);
        List<Section> sectionsInPath = getPath(graph, sourceStation, targetStation);

        Long totalDistance = getTotalDistanceInPath(sectionsInPath);
        Long totalDuration = getTotalDurationInPath(sectionsInPath);
        long totalFareFromDistance = calculateFare(totalDistance);

        return PathRetrieveResponse.builder()
                .stations(StationResponse.from(sectionsInPath))
                .distance(totalDistance)
                .duration(totalDuration)
                .fare(totalFareFromDistance)
                .build();
    }


    private void validIsSameOriginStation(Station sourceStation, Station targetStation) {
        if (sourceStation.equals(targetStation)) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_REQUEST_STATION_IS_SAME_ORIGIN);
        }
    }

    private WeightedMultigraph<Station, SectionEdge> getGraph(List<Section> sections) {
        WeightedMultigraph<Station, SectionEdge> graph = new WeightedMultigraph<>(SectionEdge.class);
        List<Station> stations = getStations(sections);

        stations.forEach(graph::addVertex);
        sections.forEach(section -> {
            SectionEdge sectionEdge = new SectionEdge(section);
            graph.addEdge(section.getUpStation(), section.getDownStation(), sectionEdge);
            strategy.setEdgeWeight(graph, section, sectionEdge);
        });

        return graph;
    }

    private List<Section> getPath(WeightedMultigraph<Station, SectionEdge> graph,
                                  Station sourceStation,
                                  Station targetStation) {
        try {
            final DijkstraShortestPath<Station, SectionEdge> dijkstraShortestPath = new DijkstraShortestPath<>(graph);
            final List<SectionEdge> edgeList = dijkstraShortestPath.getPath(sourceStation, targetStation).getEdgeList();
            return edgeList.stream()
                    .map(SectionEdge::getSection)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException | NullPointerException e) {
            throw new SubwayBadRequestException(SubwayMessage.PATH_NOT_CONNECTED_IN_SECTION);
        }
    }

    private Long getTotalDurationInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDuration)
                .reduce(0L, Long::sum);
    }

    private Long getTotalDistanceInPath(List<Section> sections) {
        return sections.stream()
                .map(Section::getDistance)
                .reduce(0L, Long::sum);
    }

    private List<Station> getStations(List<Section> sections) {
        return sections.stream()
                .flatMap(section -> Stream.of(section.getUpStation(), section.getDownStation()))
                .distinct()
                .collect(Collectors.toList());
    }

    private long calculateFare(final long distance) {
        long totalFare = BASIC_FARE;
        if (distance > 10 && distance <= 50) {
            totalFare += calculateOverFareWithTenDistance(distance - 10);
        }
        if (distance > 50) {
            totalFare += calculateOverFareWithTenDistance(40);
            totalFare += calculateOverFareWithFiftyDistance(distance - 50);
        }
        return totalFare;
    }

    private long calculateOverFareWithTenDistance(long distance) {
        return (long) ((Math.ceil((distance - 1) / 5) + 1) * 100);
    }

    private long calculateOverFareWithFiftyDistance(long distance) {
        return (long) ((Math.ceil((distance - 1) / 8) + 1) * 100);
    }
}

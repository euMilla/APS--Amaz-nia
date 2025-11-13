import java.util.List;

public interface SortingAlgorithm {
    void sort(List<SatelliteImage> images);
    String getName();
    String getDescription();
    String getTimeComplexity();
    String getSpaceComplexity();
}
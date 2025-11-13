import java.util.List;

public class QuickSort implements SortingAlgorithm {
    
    @Override
    public void sort(List<SatelliteImage> images) {
        if (images == null || images.size() <= 1) return;
        quickSort(images, 0, images.size() - 1);
    }
    
    private void quickSort(List<SatelliteImage> images, int low, int high) {
        if (low < high) {
            int pi = partition(images, low, high);
            quickSort(images, low, pi - 1);
            quickSort(images, pi + 1, high);
        }
    }
    
    private int partition(List<SatelliteImage> images, int low, int high) {
        SatelliteImage pivot = images.get(high);
        int i = low - 1;
        
        for (int j = low; j < high; j++) {
            if (images.get(j).compareTo(pivot) <= 0) {
                i++;
                swap(images, i, j);
            }
        }
        swap(images, i + 1, high);
        return i + 1;
    }
    
    private void swap(List<SatelliteImage> images, int i, int j) {
        SatelliteImage temp = images.get(i);
        images.set(i, images.get(j));
        images.set(j, temp);
    }
    
    @Override
    public String getName() {
        return "QuickSort";
    }
    
    @Override
    public String getDescription() {
        return "Algoritmo de ordenação por divisão e conquista que seleciona um pivô e particiona o array";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n) médio, O(n²) pior caso";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(log n)";
    }
}
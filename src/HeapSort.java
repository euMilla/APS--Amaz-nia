import java.util.List;

public class HeapSort implements SortingAlgorithm {
    
    @Override
    public void sort(List<SatelliteImage> images) {
        int n = images.size();
        
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(images, n, i);
        }
        
        for (int i = n - 1; i > 0; i--) {
            SatelliteImage temp = images.get(0);
            images.set(0, images.get(i));
            images.set(i, temp);
            
            heapify(images, i, 0);
        }
    }
    
    private void heapify(List<SatelliteImage> images, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < n && images.get(left).compareTo(images.get(largest)) > 0) {
            largest = left;
        }
        
        if (right < n && images.get(right).compareTo(images.get(largest)) > 0) {
            largest = right;
        }
        
        if (largest != i) {
            SatelliteImage swap = images.get(i);
            images.set(i, images.get(largest));
            images.set(largest, swap);
            
            heapify(images, n, largest);
        }
    }
    
    @Override
    public String getName() {
        return "HeapSort";
    }
    
    @Override
    public String getDescription() {
        return "Algoritmo de ordenação baseado em estrutura de dados heap";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n) em todos os casos";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(1)";
    }
}
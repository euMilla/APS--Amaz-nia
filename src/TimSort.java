import java.util.ArrayList;
import java.util.List;

public class TimSort implements SortingAlgorithm {
    private static final int RUN = 32;
    
    @Override
    public void sort(List<SatelliteImage> images) {
        if (images == null || images.size() <= 1) return;
        
        int n = images.size();
        
        for (int i = 0; i < n; i += RUN) {
            insertionSort(images, i, Math.min((i + RUN - 1), (n - 1)));
        }
        
        for (int size = RUN; size < n; size = 2 * size) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min((left + 2 * size - 1), (n - 1));
                
                if (mid < right) {
                    merge(images, left, mid, right);
                }
            }
        }
    }
    
    private void insertionSort(List<SatelliteImage> images, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            SatelliteImage temp = images.get(i);
            int j = i - 1;
            while (j >= left && images.get(j).compareTo(temp) > 0) {
                images.set(j + 1, images.get(j));
                j--;
            }
            images.set(j + 1, temp);
        }
    }
    
    private void merge(List<SatelliteImage> images, int l, int m, int r) {
        List<SatelliteImage> left = new ArrayList<>(images.subList(l, m + 1));
        List<SatelliteImage> right = new ArrayList<>(images.subList(m + 1, r + 1));
        
        int i = 0, j = 0, k = l;
        
        while (i < left.size() && j < right.size()) {
            if (left.get(i).compareTo(right.get(j)) <= 0) {
                images.set(k++, left.get(i++));
            } else {
                images.set(k++, right.get(j++));
            }
        }
        
        while (i < left.size()) {
            images.set(k++, left.get(i++));
        }
        
        while (j < right.size()) {
            images.set(k++, right.get(j++));
        }
    }
    
    @Override
    public String getName() {
        return "TimSort";
    }
    
    @Override
    public String getDescription() {
        return "Algoritmo híbrido derivado do merge sort e insertion sort, usado no Python e Java";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n)";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }
}
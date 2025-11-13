import java.util.ArrayList;
import java.util.List;

public class MergeSort implements SortingAlgorithm {
    
    @Override
    public void sort(List<SatelliteImage> images) {
        if (images == null || images.size() <= 1) return;
        mergeSort(images, 0, images.size() - 1);
    }
    
    private void mergeSort(List<SatelliteImage> images, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(images, left, mid);
            mergeSort(images, mid + 1, right);
            merge(images, left, mid, right);
        }
    }
    
    private void merge(List<SatelliteImage> images, int left, int mid, int right) {
        List<SatelliteImage> leftList = new ArrayList<>(images.subList(left, mid + 1));
        List<SatelliteImage> rightList = new ArrayList<>(images.subList(mid + 1, right + 1));
        
        int i = 0, j = 0, k = left;
        
        while (i < leftList.size() && j < rightList.size()) {
            if (leftList.get(i).compareTo(rightList.get(j)) <= 0) {
                images.set(k++, leftList.get(i++));
            } else {
                images.set(k++, rightList.get(j++));
            }
        }
        
        while (i < leftList.size()) {
            images.set(k++, leftList.get(i++));
        }
        
        while (j < rightList.size()) {
            images.set(k++, rightList.get(j++));
        }
    }
    
    @Override
    public String getName() {
        return "MergeSort";
    }
    
    @Override
    public String getDescription() {
        return "Algoritmo de ordenação estável que divide o array em metades, ordena e depois combina";
    }
    
    @Override
    public String getTimeComplexity() {
        return "O(n log n) em todos os casos";
    }
    
    @Override
    public String getSpaceComplexity() {
        return "O(n)";
    }
}
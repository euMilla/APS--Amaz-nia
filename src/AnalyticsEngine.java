import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class AnalyticsEngine {
    private Map<String, AlgorithmStats> algorithmStats;
    private List<TestResult> testHistory;
    private ExecutorService executor;
    
    public AnalyticsEngine() {
        this.algorithmStats = new ConcurrentHashMap<>();
        this.testHistory = Collections.synchronizedList(new ArrayList<>());
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        initializeAlgorithms();
    }
    
    private void initializeAlgorithms() {
        String[] algorithms = {
            "Quick Sort", "Merge Sort", "Heap Sort", 
            "Tim Sort", "Radix Sort", "Intro Sort",
            "Dual-Pivot Quick Sort", "Block Sort"
        };
        
        for (String algo : algorithms) {
            algorithmStats.put(algo, new AlgorithmStats(algo));
        }
    }
    
    public TestResult runAlgorithmTest(String algorithmName, int[] data, TestConfig config) {
        long startTime = System.nanoTime();
        long memoryBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        int[] sortedData = executeAlgorithm(algorithmName, Arrays.copyOf(data, data.length));
        
        long endTime = System.nanoTime();
        long memoryAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        boolean success = isSorted(sortedData);
        long executionTime = endTime - startTime;
        long memoryUsed = memoryAfter - memoryBefore;
        
        TestResult result = new TestResult(
            algorithmName,
            executionTime,
            memoryUsed,
            data.length,
            success,
            config.getScenario(),
            new Date()
        );
        
        // Atualizar estatísticas
        algorithmStats.get(algorithmName).addResult(result);
        testHistory.add(result);
        
        return result;
    }
    
    public BenchmarkResult runCompleteBenchmark(int[] data, TestConfig config) {
        List<TestResult> results = new ArrayList<>();
        List<Future<TestResult>> futures = new ArrayList<>();
        
        for (String algorithm : algorithmStats.keySet()) {
            futures.add(executor.submit(() -> 
                runAlgorithmTest(algorithm, data, config)
            ));
        }
        
        for (Future<TestResult> future : futures) {
            try {
                results.add(future.get(30, TimeUnit.SECONDS));
            } catch (Exception e) {
                System.err.println("Error executing algorithm: " + e.getMessage());
            }
        }
        
        return new BenchmarkResult(results, config, new Date());
    }
    
    private int[] executeAlgorithm(String algorithmName, int[] data) {
        switch (algorithmName) {
            case "Quick Sort":
                return quickSort(data);
            case "Merge Sort":
                return mergeSort(data);
            case "Heap Sort":
                return heapSort(data);
            case "Tim Sort":
                return timSort(data);
            case "Radix Sort":
                return radixSort(data);
            case "Intro Sort":
                return introSort(data);
            case "Dual-Pivot Quick Sort":
                return dualPivotQuickSort(data);
            case "Block Sort":
                return blockSort(data);
            default:
                throw new IllegalArgumentException("Unknown algorithm: " + algorithmName);
        }
    }
    
    // Implementações otimizadas dos algoritmos
    private int[] quickSort(int[] arr) {
        quickSort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // Use median-of-three for pivot selection
            int pivot = medianOfThree(arr, low, high);
            int pi = partition(arr, low, high, pivot);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }
    
    private int medianOfThree(int[] arr, int low, int high) {
        int mid = low + (high - low) / 2;
        
        if (arr[low] > arr[mid]) swap(arr, low, mid);
        if (arr[low] > arr[high]) swap(arr, low, high);
        if (arr[mid] > arr[high]) swap(arr, mid, high);
        
        return mid;
    }
    
    private int partition(int[] arr, int low, int high, int pivotIndex) {
        int pivotValue = arr[pivotIndex];
        swap(arr, pivotIndex, high);
        
        int i = low;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivotValue) {
                swap(arr, i, j);
                i++;
            }
        }
        swap(arr, i, high);
        return i;
    }
    
    private int[] mergeSort(int[] arr) {
        if (arr.length <= 1) return arr;
        
        int[] temp = new int[arr.length];
        mergeSort(arr, temp, 0, arr.length - 1);
        return arr;
    }
    
    private void mergeSort(int[] arr, int[] temp, int left, int right) {
        if (left < right) {
            int mid = left + (right - left) / 2;
            mergeSort(arr, temp, left, mid);
            mergeSort(arr, temp, mid + 1, right);
            merge(arr, temp, left, mid, right);
        }
    }
    
    private void merge(int[] arr, int[] temp, int left, int mid, int right) {
        System.arraycopy(arr, left, temp, left, right - left + 1);
        
        int i = left, j = mid + 1, k = left;
        while (i <= mid && j <= right) {
            if (temp[i] <= temp[j]) {
                arr[k++] = temp[i++];
            } else {
                arr[k++] = temp[j++];
            }
        }
        
        while (i <= mid) arr[k++] = temp[i++];
        while (j <= right) arr[k++] = temp[j++];
    }
    
    private int[] heapSort(int[] arr) {
        int n = arr.length;
        
        // Build max heap
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i);
        }
        
        // Extract elements from heap
        for (int i = n - 1; i > 0; i--) {
            swap(arr, 0, i);
            heapify(arr, i, 0);
        }
        return arr;
    }
    
    private void heapify(int[] arr, int n, int i) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < n && arr[left] > arr[largest]) largest = left;
        if (right < n && arr[right] > arr[largest]) largest = right;
        
        if (largest != i) {
            swap(arr, i, largest);
            heapify(arr, n, largest);
        }
    }
    
    private int[] timSort(int[] arr) {
        // Simplified TimSort implementation
        int RUN = 32;
        int n = arr.length;
        
        // Sort individual subarrays of size RUN
        for (int i = 0; i < n; i += RUN) {
            insertionSort(arr, i, Math.min(i + RUN - 1, n - 1));
        }
        
        // Merge sorted subarrays
        for (int size = RUN; size < n; size = 2 * size) {
            for (int left = 0; left < n; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min(left + 2 * size - 1, n - 1);
                if (mid < right) {
                    merge(arr, new int[arr.length], left, mid, right);
                }
            }
        }
        return arr;
    }
    
    private int[] radixSort(int[] arr) {
        if (arr.length == 0) return arr;
        
        int max = Arrays.stream(arr).max().getAsInt();
        
        for (int exp = 1; max / exp > 0; exp *= 10) {
            countingSort(arr, exp);
        }
        return arr;
    }
    
    private void countingSort(int[] arr, int exp) {
        int n = arr.length;
        int[] output = new int[n];
        int[] count = new int[10];
        
        for (int value : arr) {
            count[(value / exp) % 10]++;
        }
        
        for (int i = 1; i < 10; i++) {
            count[i] += count[i - 1];
        }
        
        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % 10] - 1] = arr[i];
            count[(arr[i] / exp) % 10]--;
        }
        
        System.arraycopy(output, 0, arr, 0, n);
    }
    
    private int[] introSort(int[] arr) {
        int depthLimit = (int) (2 * Math.log(arr.length) / Math.log(2));
        introSort(arr, 0, arr.length - 1, depthLimit);
        return arr;
    }
    
    private void introSort(int[] arr, int low, int high, int depthLimit) {
        if (high - low > 16) {
            if (depthLimit == 0) {
                heapSort(arr, low, high);
                return;
            }
            
            int pivot = partition(arr, low, high, medianOfThree(arr, low, high));
            introSort(arr, low, pivot - 1, depthLimit - 1);
            introSort(arr, pivot + 1, high, depthLimit - 1);
        } else {
            insertionSort(arr, low, high);
        }
    }
    
    private void heapSort(int[] arr, int low, int high) {
        int n = high - low + 1;
        
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(arr, n, i, low);
        }
        
        for (int i = n - 1; i > 0; i--) {
            swap(arr, low, low + i);
            heapify(arr, i, 0, low);
        }
    }
    
    private void heapify(int[] arr, int n, int i, int offset) {
        int largest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < n && arr[offset + left] > arr[offset + largest]) largest = left;
        if (right < n && arr[offset + right] > arr[offset + largest]) largest = right;
        
        if (largest != i) {
            swap(arr, offset + i, offset + largest);
            heapify(arr, n, largest, offset);
        }
    }
    
    private int[] dualPivotQuickSort(int[] arr) {
        dualPivotQuickSort(arr, 0, arr.length - 1);
        return arr;
    }
    
    private void dualPivotQuickSort(int[] arr, int low, int high) {
        if (low < high) {
            int[] pivots = dualPivotPartition(arr, low, high);
            dualPivotQuickSort(arr, low, pivots[0] - 1);
            dualPivotQuickSort(arr, pivots[0] + 1, pivots[1] - 1);
            dualPivotQuickSort(arr, pivots[1] + 1, high);
        }
    }
    
    private int[] dualPivotPartition(int[] arr, int low, int high) {
        if (arr[low] > arr[high]) swap(arr, low, high);
        
        int pivot1 = arr[low], pivot2 = arr[high];
        int i = low + 1, j = high - 1;
        int k = low + 1;
        
        while (k <= j) {
            if (arr[k] < pivot1) {
                swap(arr, k, i);
                i++;
            } else if (arr[k] >= pivot2) {
                while (arr[j] > pivot2 && k < j) j--;
                swap(arr, k, j);
                j--;
                if (arr[k] < pivot1) {
                    swap(arr, k, i);
                    i++;
                }
            }
            k++;
        }
        i--; j++;
        
        swap(arr, low, i);
        swap(arr, high, j);
        
        return new int[]{i, j};
    }
    
    private int[] blockSort(int[] arr) {
        // Simplified block sort implementation
        int blockSize = (int) Math.sqrt(arr.length);
        
        for (int i = 0; i < arr.length; i += blockSize) {
            insertionSort(arr, i, Math.min(i + blockSize - 1, arr.length - 1));
        }
        
        for (int size = blockSize; size < arr.length; size = 2 * size) {
            for (int left = 0; left < arr.length; left += 2 * size) {
                int mid = left + size - 1;
                int right = Math.min(left + 2 * size - 1, arr.length - 1);
                if (mid < right) {
                    merge(arr, new int[arr.length], left, mid, right);
                }
            }
        }
        return arr;
    }
    
    private void insertionSort(int[] arr, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = arr[i];
            int j = i - 1;
            
            while (j >= left && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }
    
    private void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    
    private boolean isSorted(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                return false;
            }
        }
        return true;
    }
    
    public int[] generateTestData(String dataType, int size) {
        Random random = new Random();
        int[] data = new int[size];
        
        switch (dataType) {
            case "Random Data":
                for (int i = 0; i < size; i++) {
                    data[i] = random.nextInt(size * 10);
                }
                break;
                
            case "Sorted Data":
                for (int i = 0; i < size; i++) {
                    data[i] = i;
                }
                break;
                
            case "Reverse Sorted":
                for (int i = 0; i < size; i++) {
                    data[i] = size - i;
                }
                break;
                
            case "Nearly Sorted":
                for (int i = 0; i < size; i++) {
                    data[i] = i;
                }
                // Add slight disorder (5%)
                for (int i = 0; i < size / 20; i++) {
                    int idx1 = random.nextInt(size);
                    int idx2 = random.nextInt(size);
                    swap(data, idx1, idx2);
                }
                break;
                
            case "Gaussian Distribution":
                for (int i = 0; i < size; i++) {
                    data[i] = (int) (random.nextGaussian() * size / 4 + size / 2);
                }
                break;
                
            default:
                for (int i = 0; i < size; i++) {
                    data[i] = random.nextInt(size * 10);
                }
        }
        
        return data;
    }
    
    public Map<String, AlgorithmStats> getAlgorithmStats() {
        return Collections.unmodifiableMap(algorithmStats);
    }
    
    public List<TestResult> getTestHistory() {
        return Collections.unmodifiableList(testHistory);
    }
    
    public void exportResults(String filename) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Algorithm,ExecutionTime(ns),MemoryUsed(bytes),DataSize,Success,Timestamp\n");
        
        for (TestResult result : testHistory) {
            sb.append(String.format("%s,%d,%d,%d,%s,%s\n",
                result.getAlgorithmName(),
                result.getExecutionTime(),
                result.getMemoryUsed(),
                result.getDataSize(),
                result.isSuccess(),
                result.getTimestamp()
            ));
        }
        
        Files.write(Paths.get(filename), sb.toString().getBytes());
    }
    
    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}

class AlgorithmStats {
    private String algorithmName;
    private List<Double> executionTimes;
    private long totalTests;
    private long successfulTests;
    
    public AlgorithmStats(String algorithmName) {
        this.algorithmName = algorithmName;
        this.executionTimes = new ArrayList<>();
        this.totalTests = 0;
        this.successfulTests = 0;
    }
    
    public void addResult(TestResult result) {
        executionTimes.add(result.getExecutionTime() / 1_000_000_000.0); // Convert to seconds
        totalTests++;
        if (result.isSuccess()) successfulTests++;
    }
    
    public double getAverageTime() {
        return executionTimes.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    }
    
    public double getSuccessRate() {
        return totalTests > 0 ? (double) successfulTests / totalTests * 100 : 0.0;
    }
    
    // Getters
    public String getAlgorithmName() { return algorithmName; }
    public List<Double> getExecutionTimes() { return Collections.unmodifiableList(executionTimes); }
    public long getTotalTests() { return totalTests; }
    public long getSuccessfulTests() { return successfulTests; }
}

class TestResult {
    private String algorithmName;
    private long executionTime; // nanoseconds
    private long memoryUsed;    // bytes
    private int dataSize;
    private boolean success;
    private String scenario;
    private Date timestamp;
    
    public TestResult(String algorithmName, long executionTime, long memoryUsed, 
                     int dataSize, boolean success, String scenario, Date timestamp) {
        this.algorithmName = algorithmName;
        this.executionTime = executionTime;
        this.memoryUsed = memoryUsed;
        this.dataSize = dataSize;
        this.success = success;
        this.scenario = scenario;
        this.timestamp = timestamp;
    }
    
    // Getters
    public String getAlgorithmName() { return algorithmName; }
    public long getExecutionTime() { return executionTime; }
    public long getMemoryUsed() { return memoryUsed; }
    public int getDataSize() { return dataSize; }
    public boolean isSuccess() { return success; }
    public String getScenario() { return scenario; }
    public Date getTimestamp() { return timestamp; }
}

class BenchmarkResult {
    private List<TestResult> results;
    private TestConfig config;
    private Date timestamp;
    
    public BenchmarkResult(List<TestResult> results, TestConfig config, Date timestamp) {
        this.results = results;
        this.config = config;
        this.timestamp = timestamp;
    }
    
    public TestResult getFastestResult() {
        return results.stream()
            .filter(TestResult::isSuccess)
            .min(Comparator.comparingLong(TestResult::getExecutionTime))
            .orElse(null);
    }
    
    public TestResult getSlowestResult() {
        return results.stream()
            .filter(TestResult::isSuccess)
            .max(Comparator.comparingLong(TestResult::getExecutionTime))
            .orElse(null);
    }
    
    // Getters
    public List<TestResult> getResults() { return Collections.unmodifiableList(results); }
    public TestConfig getConfig() { return config; }
    public Date getTimestamp() { return timestamp; }
}

class TestConfig {
    private String scenario;
    private int dataSize;
    private int iterations;
    private boolean enableMemoryTracking;
    private boolean enableDetailedLogging;
    
    public TestConfig(String scenario, int dataSize, int iterations, 
                     boolean enableMemoryTracking, boolean enableDetailedLogging) {
        this.scenario = scenario;
        this.dataSize = dataSize;
        this.iterations = iterations;
        this.enableMemoryTracking = enableMemoryTracking;
        this.enableDetailedLogging = enableDetailedLogging;
    }
    
    // Getters
    public String getScenario() { return scenario; }
    public int getDataSize() { return dataSize; }
    public int getIterations() { return iterations; }
    public boolean isEnableMemoryTracking() { return enableMemoryTracking; }
    public boolean isEnableDetailedLogging() { return enableDetailedLogging; }
}
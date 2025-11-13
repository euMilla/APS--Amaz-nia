import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PerformanceResult implements Comparable<PerformanceResult> {
    private String algorithmName;
    private String dataType;
    private int dataSize;
    private long executionTime;
    private long memoryUsed;
    private boolean success;
    private String timestamp;
    private String sortCriteria;
    
    public PerformanceResult(String algorithmName, String dataType, int dataSize, 
                           long executionTime, long memoryUsed, boolean success, String sortCriteria) {
        this.algorithmName = algorithmName;
        this.dataType = dataType;
        this.dataSize = dataSize;
        this.executionTime = executionTime;
        this.memoryUsed = memoryUsed;
        this.success = success;
        this.sortCriteria = sortCriteria;
        this.timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
    
    // Getters
    public String getAlgorithmName() { return algorithmName; }
    public String getDataType() { return dataType; }
    public int getDataSize() { return dataSize; }
    public long getExecutionTime() { return executionTime; }
    public long getMemoryUsed() { return memoryUsed; }
    public boolean isSuccess() { return success; }
    public String getTimestamp() { return timestamp; }
    public String getSortCriteria() { return sortCriteria; }
    
    @Override
    public int compareTo(PerformanceResult other) {
        return Long.compare(this.executionTime, other.executionTime);
    }
    
    @Override
    public String toString() {
        return String.format("🔹 %-12s | %-8s | %,6d elem | ⏱️ %,5d ms | 💾 %,5d KB | %s | Critério: %s", 
                           algorithmName, dataType, dataSize, executionTime, memoryUsed,
                           success ? "✅" : "❌", sortCriteria);
    }
    
    public String toDetailedString() {
        return String.format(
            "Algoritmo: %s\n" +
            "Tipo de Dados: %s\n" +
            "Tamanho do Dataset: %,d elementos\n" +
            "Tempo de Execução: %,d ms\n" +
            "Memória Utilizada: %,d KB\n" +
            "Critério de Ordenação: %s\n" +
            "Ordenação Bem-sucedida: %s\n" +
            "Timestamp: %s\n" +
            "----------------------------------------",
            algorithmName, dataType, dataSize, executionTime, memoryUsed, sortCriteria,
            success ? "Sim" : "Não", timestamp
        );
    }
    
    public String toCSV() {
        return String.format("%s,%s,%d,%d,%d,%s,%s,%s",
            algorithmName, dataType, dataSize, executionTime, memoryUsed, success, timestamp, sortCriteria);
    }
}
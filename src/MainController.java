import java.io.*;
import java.util.*;
import javax.swing.*;

public class MainController {
    private MainFrame mainFrame;
    private List<SatelliteImage> satelliteImages;
    private Map<String, SortingAlgorithm> algorithms;
    private List<PerformanceResult> testHistory;
    
    public MainController() {
        this.satelliteImages = new ArrayList<>();
        this.algorithms = new HashMap<>();
        this.testHistory = new ArrayList<>();
        initializeAlgorithms();
        loadSatelliteData();
    }
    
    public void initialize() {
        mainFrame = new MainFrame(this);
        mainFrame.setVisible(true);
        System.out.println("🌍 SISTEMA DE ANÁLISE DE ALGORITMOS DE ORDENAÇÃO");
        System.out.println("📊 Contexto: Ordenação de imagens de satélite da Amazônia");
        System.out.println("⚡ Algoritmos carregados: " + algorithms.size());
        System.out.println("🛰️  Imagens carregadas: " + satelliteImages.size());
        System.out.println("🎯 Foco: Comparação de desempenho entre técnicas de ordenação\n");
    }
    
    private void initializeAlgorithms() {
        // 4 algoritmos conforme requisito (3+ técnicas)
        algorithms.put("QuickSort", new QuickSort());
        algorithms.put("MergeSort", new MergeSort());
        algorithms.put("HeapSort", new HeapSort());
        algorithms.put("TimSort", new TimSort());
        
        System.out.println("🔧 ALGORITMOS DE ORDENAÇÃO CARREGADOS:");
        for (String algo : algorithms.keySet()) {
            SortingAlgorithm algorithm = algorithms.get(algo);
            System.out.println("   • " + algo + ": " + algorithm.getDescription());
        }
        System.out.println();
    }
    
    private void loadSatelliteData() {
        // Carregar dados EXTERNOS conforme requisito 3
        satelliteImages = ImageLoader.loadFromFile("satellite_images.txt");
        
        if (satelliteImages.isEmpty()) {
            // Gerar 100.000 imagens conforme contexto do problema
            satelliteImages = ImageLoader.generateSampleData(100000);
            System.out.println("📁 GERADOS " + satelliteImages.size() + " IMAGENS DE EXEMPLO");
        } else {
            System.out.println("📁 CARREGADAS " + satelliteImages.size() + " IMAGENS DO ARQUIVO");
        }
        
        // Estatísticas iniciais
        Map<String, Integer> qualityStats = getQualityStats();
        System.out.println("📊 ESTATÍSTICAS DAS IMAGENS:");
        for (Map.Entry<String, Integer> entry : qualityStats.entrySet()) {
            System.out.println("   • Qualidade " + entry.getKey() + ": " + entry.getValue() + " imagens");
        }
        System.out.println();
    }
    
    // MÉTODO PRINCIPAL PARA TESTES CONFORME REQUISITO 2
    public PerformanceResult runSortingTest(String algorithmName, String dataType, int dataSize, String sortCriteria) {
        SortingAlgorithm algorithm = algorithms.get(algorithmName);
        if (algorithm == null) {
            throw new IllegalArgumentException("Algoritmo não encontrado: " + algorithmName);
        }
        
        // Preparar dados conforme requisito 3 (externos e aleatórios)
        List<SatelliteImage> testData = prepareTestData(dataType, dataSize);
        
        // MEDIR APENAS TEMPO DE ORDENAÇÃO (conforme requisito 2)
        Runtime runtime = Runtime.getRuntime();
        runtime.gc(); // Coleta de lixo antes da medição
        
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
        long startTime = System.nanoTime();
        
        // ⚡ EXECUTAR ORDENAÇÃO (apenas isso é medido - requisito 2)
        algorithm.sort(testData);
        
        long endTime = System.nanoTime();
        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        
        long executionTime = (endTime - startTime) / 1000000; // Converter para milissegundos
        long memoryUsed = (memoryAfter - memoryBefore) / 1024; // Converter para KB
        
        // Verificar se a ordenação foi bem-sucedida
        boolean success = isSorted(testData, sortCriteria);
        
        PerformanceResult result = new PerformanceResult(
            algorithmName, 
            dataType, 
            dataSize, 
            executionTime, 
            memoryUsed,
            success,
            sortCriteria
        );
        
        testHistory.add(result);
        
        System.out.println("✅ " + algorithmName + " | " + dataType + " | " + dataSize + 
                          " elementos | " + executionTime + " ms | Critério: " + sortCriteria +
                          " | " + (success ? "ORDENAÇÃO OK" : "FALHA NA ORDENAÇÃO"));
        
        return result;
    }
    
    // TESTE COMPARATIVO ENTRE TODOS OS ALGORITMOS
    public List<PerformanceResult> performComparativeTest(int dataSize, String dataType) {
        System.out.println("\n🔬 INICIANDO TESTE COMPARATIVO");
        System.out.println("📝 Tipo de Dados: " + dataType + " | Tamanho: " + dataSize + " elementos");
        System.out.println("=" .repeat(60));
        
        List<PerformanceResult> results = new ArrayList<>();
        
        for (String algorithmName : algorithms.keySet()) {
            try {
                PerformanceResult result = runSortingTest(algorithmName, dataType, dataSize, "id");
                results.add(result);
            } catch (Exception e) {
                System.err.println("❌ Erro no algoritmo " + algorithmName + ": " + e.getMessage());
            }
        }
        
        // Ordenar resultados por tempo de execução (do mais rápido para o mais lento)
        Collections.sort(results);
        
        System.out.println("=" .repeat(60));
        System.out.println("🏁 TESTE COMPARATIVO CONCLUÍDO");
        System.out.println("🥇 Mais rápido: " + results.get(0).getAlgorithmName() + 
                         " (" + results.get(0).getExecutionTime() + " ms)");
        System.out.println("🎯 Total de algoritmos testados: " + results.size());
        
        return results;
    }
    
    // TESTE DE ESCALABILIDADE
    public List<PerformanceResult> performScalabilityTest(String algorithmName, int maxSize) {
        System.out.println("\n📈 INICIANDO TESTE DE ESCALABILIDADE: " + algorithmName);
        System.out.println("📊 Tamanho máximo: " + maxSize + " elementos");
        
        List<PerformanceResult> results = new ArrayList<>();
        int[] sizes = {100, 500, 1000, 5000, 10000, 50000, Math.min(maxSize, 100000)};
        
        for (int size : sizes) {
            if (size <= maxSize) {
                try {
                    PerformanceResult result = runSortingTest(algorithmName, "Aleatório", size, "id");
                    results.add(result);
                } catch (Exception e) {
                    System.err.println("❌ Erro no tamanho " + size + ": " + e.getMessage());
                }
            }
        }
        
        System.out.println("📈 TESTE DE ESCALABILIDADE CONCLUÍDO");
        return results;
    }
    
    // PREPARAR DADOS CONFORME REQUISITO 3
    private List<SatelliteImage> prepareTestData(String dataType, int size) {
        List<SatelliteImage> testData = new ArrayList<>();
        
        switch (dataType) {
            case "Externo":
                // Dados EXTERNOS do arquivo (requisito 3)
                testData = new ArrayList<>(satelliteImages.subList(0, Math.min(size, satelliteImages.size())));
                Collections.shuffle(testData); // Embaralhar para teste justo
                break;
                
            case "Aleatório":
                // Dados INTERNOS aleatórios (requisito 3)
                testData = ImageLoader.generateRandomData(size);
                break;
                
            case "Ordenado":
                // Dados já ordenados (para teste de melhor caso)
                testData = ImageLoader.generateRandomData(size);
                Collections.sort(testData);
                break;
                
            case "Reverso":
                // Dados em ordem reversa (para teste de pior caso)
                testData = ImageLoader.generateRandomData(size);
                testData.sort(Collections.reverseOrder());
                break;
                
            case "Quase Ordenado":
                // Dados quase ordenados
                testData = ImageLoader.generateRandomData(size);
                Collections.sort(testData);
                // Adicionar 10% de desordem
                Random random = new Random();
                int swaps = size / 10;
                for (int i = 0; i < swaps; i++) {
                    int idx1 = random.nextInt(size);
                    int idx2 = random.nextInt(size);
                    Collections.swap(testData, idx1, idx2);
                }
                break;
                
            default:
                throw new IllegalArgumentException("Tipo de dados não suportado: " + dataType);
        }
        
        return testData;
    }
    
    // VERIFICAR SE A LISTA ESTÁ ORDENADA CORRETAMENTE
    private boolean isSorted(List<SatelliteImage> images, String sortCriteria) {
        for (int i = 0; i < images.size() - 1; i++) {
            int comparison;
            switch (sortCriteria) {
                case "timestamp":
                    comparison = Long.compare(images.get(i).getTimestamp(), images.get(i + 1).getTimestamp());
                    break;
                case "latitude":
                    comparison = Double.compare(images.get(i).getLatitude(), images.get(i + 1).getLatitude());
                    break;
                case "longitude":
                    comparison = Double.compare(images.get(i).getLongitude(), images.get(i + 1).getLongitude());
                    break;
                case "quality":
                    comparison = images.get(i).getQuality().compareTo(images.get(i + 1).getQuality());
                    break;
                default: // "id" ou padrão
                    comparison = images.get(i).compareTo(images.get(i + 1));
            }
            
            if (comparison > 0) {
                return false;
            }
        }
        return true;
    }
    
    // GETTERS PARA A INTERFACE
    public List<SatelliteImage> getSatelliteImages() {
        return Collections.unmodifiableList(satelliteImages);
    }
    
    public Set<String> getAlgorithmNames() {
        return Collections.unmodifiableSet(algorithms.keySet());
    }
    
    public int getTotalImages() {
        return satelliteImages.size();
    }
    
    public Map<String, Integer> getQualityStats() {
        Map<String, Integer> stats = new HashMap<>();
        for (SatelliteImage image : satelliteImages) {
            stats.put(image.getQuality(), stats.getOrDefault(image.getQuality(), 0) + 1);
        }
        return stats;
    }
    
    public Map<String, Integer> getRegionStats() {
        Map<String, Integer> stats = new HashMap<>();
        for (SatelliteImage image : satelliteImages) {
            stats.put(image.getRegion(), stats.getOrDefault(image.getRegion(), 0) + 1);
        }
        return stats;
    }
    
    public List<PerformanceResult> getTestHistory() {
        return Collections.unmodifiableList(testHistory);
    }
    
    public SortingAlgorithm getAlgorithm(String name) {
        return algorithms.get(name);
    }
    
    public void exportResultsToFile(String filename) {
        try {
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            writer.println("Algorithm,DataType,DataSize,ExecutionTime(ms),MemoryUsed(KB),Success,Timestamp,SortCriteria");
            for (PerformanceResult result : testHistory) {
                writer.println(result.toCSV());
            }
            writer.close();
            
            JOptionPane.showMessageDialog(mainFrame, 
                "📊 Resultados exportados para: " + filename + 
                "\n\nTotal de testes: " + testHistory.size() +
                "\nAlgoritmos: " + String.join(", ", algorithms.keySet()),
                "Exportação Concluída", 
                JOptionPane.INFORMATION_MESSAGE);
                
        } catch (IOException e) {
            JOptionPane.showMessageDialog(mainFrame,
                "❌ Erro ao exportar resultados: " + e.getMessage(),
                "Erro na Exportação", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
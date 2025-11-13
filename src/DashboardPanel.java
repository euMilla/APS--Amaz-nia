import java.awt.*;
import java.util.Map;
import javax.swing.*;

public class DashboardPanel extends JPanel {
    private MainController controller;
    private JLabel totalImagesLabel, highQualityLabel, mediumQualityLabel, lowQualityLabel;
    private JLabel algorithmsLabel, lastTestLabel, systemStatusLabel;
    private JLabel memoryUsageLabel, cpuUsageLabel, activeTestsLabel;
    private Timer refreshTimer;
    
    // Cores do tema dark moderno
    private final Color BACKGROUND = new Color(18, 18, 24);
    private final Color CARD_BG = new Color(30, 30, 40);
    private final Color ACCENT = new Color(0, 150, 255);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 190);
    private final Color SUCCESS = new Color(0, 200, 150);
    private final Color WARNING = new Color(255, 180, 0);
    private final Color DANGER = new Color(255, 80, 80);
    private final Color PURPLE = new Color(160, 100, 255);
    private final Color CYAN = new Color(0, 200, 200);
    
    public DashboardPanel(MainController controller) {
        this.controller = controller;
        setLayout(new BorderLayout());
        setBackground(BACKGROUND);
        initializePanel();
        updateData();
        setupAutoRefresh();
    }
    
    private void initializePanel() {
        // Container principal com padding
        JPanel mainContainer = new JPanel(new BorderLayout(0, 20));
        mainContainer.setBackground(BACKGROUND);
        mainContainer.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        
        mainContainer.add(createHeader(), BorderLayout.NORTH);
        mainContainer.add(createContent(), BorderLayout.CENTER);
        
        add(mainContainer, BorderLayout.CENTER);
    }
    
    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        
        // Left side - Title and subtitle
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(BACKGROUND);
        titlePanel.setLayout(new BoxLayout(titlePanel, BoxLayout.Y_AXIS));
        
        JLabel title = new JLabel("DASHBOARD PRINCIPAL");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel subtitle = new JLabel("Visão Geral do Sistema • Métricas em Tempo Real");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        subtitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        titlePanel.add(title);
        titlePanel.add(Box.createRigidArea(new Dimension(0, 5)));
        titlePanel.add(subtitle);
        
        // Right side - Status
        JPanel statusPanel = new JPanel();
        statusPanel.setBackground(BACKGROUND);
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
        
        systemStatusLabel = new JLabel("● SISTEMA ATIVO");
        systemStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        systemStatusLabel.setForeground(SUCCESS);
        systemStatusLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        JLabel timestampLabel = new JLabel("Atualizado: " + java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm:ss")));
        timestampLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        timestampLabel.setForeground(TEXT_SECONDARY);
        timestampLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);
        
        statusPanel.add(systemStatusLabel);
        statusPanel.add(Box.createRigidArea(new Dimension(0, 2)));
        statusPanel.add(timestampLabel);
        
        header.add(titlePanel, BorderLayout.WEST);
        header.add(statusPanel, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 20));
        content.setBackground(BACKGROUND);
        
        content.add(createMetricsGrid(), BorderLayout.CENTER);
        content.add(createPerformanceSection(), BorderLayout.SOUTH);
        
        return content;
    }
    
    private JPanel createMetricsGrid() {
        JPanel grid = new JPanel(new GridLayout(2, 4, 20, 20));
        grid.setBackground(BACKGROUND);
        
        // Inicializar labels
        totalImagesLabel = createMetricValue("0");
        algorithmsLabel = createMetricValue("0");
        highQualityLabel = createMetricValue("0");
        mediumQualityLabel = createMetricValue("0");
        lowQualityLabel = createMetricValue("0");
        lastTestLabel = createMetricValue("--");
        memoryUsageLabel = createMetricValue("0 MB");
        activeTestsLabel = createMetricValue("0");
        
        // Linha 1
        grid.add(createMetricCard("📊 DADOS", "Total de Imagens", totalImagesLabel, ACCENT));
        grid.add(createMetricCard("⚡ ALGORITMOS", "Carregados", algorithmsLabel, SUCCESS));
        grid.add(createMetricCard("⭐ QUALIDADE", "Grau Alto", highQualityLabel, new Color(255, 215, 0)));
        grid.add(createMetricCard("🔸 QUALIDADE", "Grau Médio", mediumQualityLabel, WARNING));
        
        // Linha 2
        grid.add(createMetricCard("🔹 QUALIDADE", "Grau Baixo", lowQualityLabel, new Color(180, 180, 180)));
        grid.add(createMetricCard("🕒 TESTES", "Último Tempo", lastTestLabel, PURPLE));
        grid.add(createMetricCard("💾 MEMÓRIA", "Em Uso", memoryUsageLabel, CYAN));
        grid.add(createMetricCard("🎯 ATIVIDADE", "Testes Ativos", activeTestsLabel, new Color(255, 100, 100)));
        
        return grid;
    }
    
    private JPanel createMetricCard(String category, String title, JLabel value, Color color) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(50, 50, 60), 1),
            BorderFactory.createEmptyBorder(25, 20, 25, 20)
        ));
        
        // Header com categoria e título
        JPanel header = new JPanel();
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBackground(CARD_BG);
        
        JLabel categoryLabel = new JLabel(category);
        categoryLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        categoryLabel.setForeground(TEXT_SECONDARY);
        categoryLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        header.add(categoryLabel);
        header.add(Box.createRigidArea(new Dimension(0, 2)));
        header.add(titleLabel);
        
        // Valor principal
        value.setFont(new Font("Segoe UI", Font.BOLD, 28));
        value.setForeground(color);
        value.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Efeito de brilho no hover
        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(color, 2),
                    BorderFactory.createEmptyBorder(24, 19, 24, 19)
                ));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                card.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(50, 50, 60), 1),
                    BorderFactory.createEmptyBorder(25, 20, 25, 20)
                ));
            }
        });
        
        card.add(header, BorderLayout.NORTH);
        card.add(value, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createPerformanceSection() {
        JPanel performancePanel = new JPanel(new BorderLayout());
        performancePanel.setBackground(BACKGROUND);
        performancePanel.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        // Header da seção
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND);
        
        JLabel titleLabel = new JLabel("📈 DESEMPENHO DO SISTEMA");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(ACCENT);
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Cards de performance
        JPanel performanceGrid = new JPanel(new GridLayout(1, 3, 15, 0));
        performanceGrid.setBackground(BACKGROUND);
        
        performanceGrid.add(createPerformanceCard("🚀 VELOCIDADE MÉDIA", "0 ms", "Tempo de execução", SUCCESS));
        performanceGrid.add(createPerformanceCard("📊 TESTES EXECUTADOS", "0", "Total de testes", PURPLE));
        performanceGrid.add(createPerformanceCard("✅ TAXA DE SUCESSO", "100%", "Ordenações válidas", CYAN));
        
        performancePanel.add(headerPanel, BorderLayout.NORTH);
        performancePanel.add(performanceGrid, BorderLayout.CENTER);
        
        return performancePanel;
    }
    
    private JPanel createPerformanceCard(String title, String value, String description, Color color) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
            BorderFactory.createEmptyBorder(20, 25, 20, 25)
        ));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_SECONDARY);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        valueLabel.setForeground(color);
        valueLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        valueLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        descLabel.setForeground(TEXT_SECONDARY);
        descLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        card.add(titleLabel);
        card.add(valueLabel);
        card.add(descLabel);
        
        return card;
    }
    
    private JLabel createMetricValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }
    
    private void setupAutoRefresh() {
        refreshTimer = new Timer(3000, e -> updateData());
        refreshTimer.start();
    }
    
    public void updateData() {
        updateMetrics();
        updateSystemStatus();
        updatePerformanceMetrics();
        repaint();
    }
    
    private void updateMetrics() {
        try {
            int total = controller.getTotalImages();
            Map<String, Integer> qualityStats = controller.getQualityStats();
            int algoCount = controller.getAlgorithmNames().size();
            int testCount = controller.getTestHistory().size();
            
            totalImagesLabel.setText(String.format("%,d", total));
            algorithmsLabel.setText(String.valueOf(algoCount));
            highQualityLabel.setText(String.format("%,d", qualityStats.getOrDefault("Alta", 0)));
            mediumQualityLabel.setText(String.format("%,d", qualityStats.getOrDefault("Média", 0)));
            lowQualityLabel.setText(String.format("%,d", qualityStats.getOrDefault("Baixa", 0)));
            activeTestsLabel.setText(String.valueOf(testCount));
            
            java.util.List<PerformanceResult> history = controller.getTestHistory();
            if (!history.isEmpty()) {
                PerformanceResult last = history.get(history.size()-1);
                lastTestLabel.setText(last.getExecutionTime() + "ms");
            } else {
                lastTestLabel.setText("--");
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar métricas: " + e.getMessage());
        }
    }
    
    private void updatePerformanceMetrics() {
        try {
            // Atualizar uso de memória
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            memoryUsageLabel.setText(String.format("%.1f MB", usedMemory / (1024.0 * 1024.0)));
            
            // Atualizar métricas de performance
            java.util.List<PerformanceResult> history = controller.getTestHistory();
            if (!history.isEmpty()) {
                double avgTime = history.stream()
                    .mapToLong(PerformanceResult::getExecutionTime)
                    .average()
                    .orElse(0.0);
                
                long successCount = history.stream()
                    .filter(PerformanceResult::isSuccess)
                    .count();
                
                double successRate = history.size() > 0 ? (successCount * 100.0) / history.size() : 100.0;
                
                // Atualizar os cards de performance
                updatePerformanceCard(0, String.format("%.0f ms", avgTime));
                updatePerformanceCard(1, String.valueOf(history.size()));
                updatePerformanceCard(2, String.format("%.1f%%", successRate));
            } else {
                updatePerformanceCard(0, "0 ms");
                updatePerformanceCard(1, "0");
                updatePerformanceCard(2, "100%");
            }
        } catch (Exception e) {
            System.err.println("Erro ao atualizar métricas de performance: " + e.getMessage());
        }
    }
    
    private void updatePerformanceCard(int index, String value) {
        JPanel performanceSection = (JPanel) ((BorderLayout) getLayout()).getLayoutComponent(BorderLayout.SOUTH);
        if (performanceSection != null) {
            JPanel performanceGrid = (JPanel) performanceSection.getComponent(1);
            if (performanceGrid.getComponentCount() > index) {
                JPanel card = (JPanel) performanceGrid.getComponent(index);
                JLabel valueLabel = (JLabel) card.getComponent(1);
                valueLabel.setText(value);
            }
        }
    }
    
    private void updateSystemStatus() {
        try {
            int totalImages = controller.getTotalImages();
            int testCount = controller.getTestHistory().size();
            
            if (totalImages == 0) {
                systemStatusLabel.setText("● SISTEMA INATIVO");
                systemStatusLabel.setForeground(TEXT_SECONDARY);
            } else if (testCount == 0) {
                systemStatusLabel.setText("● PRONTO PARA TESTES");
                systemStatusLabel.setForeground(WARNING);
            } else {
                systemStatusLabel.setText("● PROCESSANDO TESTES");
                systemStatusLabel.setForeground(SUCCESS);
            }
        } catch (Exception e) {
            systemStatusLabel.setText("● ERRO NO SISTEMA");
            systemStatusLabel.setForeground(DANGER);
        }
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (refreshTimer != null) {
            refreshTimer.stop();
        }
    }
}
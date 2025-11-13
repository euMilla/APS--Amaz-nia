import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ChartPanel extends JPanel {
    private MainController controller;
    private List<PerformanceResult> currentResults;
    private String currentChartType = "COMPARATIVO";
    private Timer animationTimer;
    private float animationProgress = 0f;
    
    // Elementos interativos
    private Map<String, Rectangle> barAreas = new HashMap<>();
    private Map<Point, String> pointInfo = new HashMap<>();
    private String hoveredAlgorithm = null;
    private String hoveredPointInfo = null;
    private Point lastMousePoint = new Point(-1, -1);
    
    // Cores modernas - Tema Preto
    private final Color BACKGROUND = new Color(18, 18, 18);
    private final Color CARD_BG = new Color(30, 30, 30);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 180);
    private final Color ACCENT = new Color(0, 150, 255);
    private final Color GRID_COLOR = new Color(60, 60, 60);
    
    // Cores para gráficos - Tons mais vibrantes no tema escuro
    private final Color[] CHART_COLORS = {
        new Color(0, 150, 255),     // Azul vibrante
        new Color(46, 204, 113),    // Verde
        new Color(255, 94, 87),     // Vermelho
        new Color(255, 159, 67),    // Laranja
        new Color(155, 89, 182),    // Roxo
        new Color(52, 152, 219),    // Azul claro
        new Color(22, 160, 133),    // Verde água
        new Color(241, 196, 15)     // Amarelo
    };
    
    private JPanel controlPanel;
    private JPanel chartDisplayPanel;
    private JPanel statsPanel;

    public ChartPanel(MainController controller) {
        this.controller = controller;
        this.currentResults = new ArrayList<>();
        initializeUI();
        setupAnimation();
        setupInteractivity();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout(0, 0));
        setBackground(BACKGROUND);
        
        // Painel de controle superior
        add(createControlPanel(), BorderLayout.NORTH);
        
        // Painel principal com gráfico e estatísticas
        JPanel mainPanel = new JPanel(new BorderLayout(20, 0));
        mainPanel.setBackground(BACKGROUND);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Painel do gráfico (70%)
        chartDisplayPanel = createChartPanel();
        mainPanel.add(chartDisplayPanel, BorderLayout.CENTER);
        
        // Painel de estatísticas (30%)
        statsPanel = createStatsPanel();
        mainPanel.add(statsPanel, BorderLayout.EAST);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // Carregar dados iniciais
        updateCharts();
    }
    
    private JPanel createControlPanel() {
        controlPanel = new JPanel();
        controlPanel.setBackground(CARD_BG);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(60, 60, 60)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        controlPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 0));
        
        // Título
        JLabel titleLabel = new JLabel("📊 Dashboard de Performance");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        controlPanel.add(titleLabel);
        
        // Separador
        controlPanel.add(createSeparator());
        
        // Botões de tipo de gráfico
        String[] chartTypes = {"COMPARATIVO", "ESCALABILIDADE", "MEMORIA"};
        for (String type : chartTypes) {
            JButton btn = createChartTypeButton(type);
            controlPanel.add(btn);
        }
        
        // Separador
        controlPanel.add(createSeparator());
        
        // Botão de atualizar
        JButton refreshBtn = createModernButton("🔄 Atualizar", ACCENT);
        refreshBtn.addActionListener(e -> updateCharts());
        controlPanel.add(refreshBtn);
        
        // Separador
        controlPanel.add(createSeparator());
        
        // Botões de exportação (substituindo o botão de animar)
        addExportButtons();
        
        return controlPanel;
    }
    
    private void addExportButtons() {
        // Botão de exportar gráfico
        JButton exportChartBtn = createModernButton("💾 Exportar Gráfico", new Color(155, 89, 182));
        exportChartBtn.addActionListener(e -> exportChartAsImage());
        controlPanel.add(exportChartBtn);
        
        // Botão de exportar dados
        JButton exportDataBtn = createModernButton("📊 Exportar Dados", new Color(241, 196, 15));
        exportDataBtn.addActionListener(e -> exportDataAsCSV());
        controlPanel.add(exportDataBtn);
        
        // Botão de relatório
        JButton reportBtn = createModernButton("📄 Relatório HTML", new Color(52, 152, 219));
        reportBtn.addActionListener(e -> exportDetailedReport());
        controlPanel.add(reportBtn);
    }
    
    private JSeparator createSeparator() {
        JSeparator separator = new JSeparator(JSeparator.VERTICAL);
        separator.setPreferredSize(new Dimension(1, 30));
        separator.setBackground(new Color(60, 60, 60));
        return separator;
    }
    
    private JButton createChartTypeButton(String type) {
        JButton button = new JButton(getIconForType(type) + " " + getDisplayName(type));
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Destacar o botão ativo
        if (type.equals(currentChartType)) {
            button.setBackground(ACCENT);
            button.setForeground(Color.WHITE);
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(ACCENT.brighter(), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
        } else {
            button.setForeground(TEXT_SECONDARY);
            button.setBackground(new Color(50, 50, 50));
            button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 80), 1),
                BorderFactory.createEmptyBorder(8, 15, 8, 15)
            ));
        }
        
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        button.addActionListener(e -> {
            currentChartType = type;
            startAnimation();
            updateStatsPanel();
            repaint();
        });
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                if (!type.equals(currentChartType)) {
                    button.setBackground(new Color(70, 70, 70));
                    button.setForeground(ACCENT);
                }
            }
            public void mouseExited(MouseEvent e) {
                if (!type.equals(currentChartType)) {
                    button.setBackground(new Color(50, 50, 50));
                    button.setForeground(TEXT_SECONDARY);
                }
            }
        });
        
        return button;
    }
    
    private String getDisplayName(String type) {
        switch (type) {
            case "COMPARATIVO": return "Comparativo";
            case "ESCALABILIDADE": return "Escalabilidade";
            case "MEMORIA": return "Memória";
            default: return type;
        }
    }
    
    private String getIconForType(String type) {
        switch (type) {
            case "COMPARATIVO": return "📈";
            case "ESCALABILIDADE": return "🚀";
            case "MEMORIA": return "💾";
            default: return "📊";
        }
    }
    
    private JButton createModernButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.brighter(), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setFocusPainted(false);
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    private JPanel createChartPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                paintChart(g);
            }
        };
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setPreferredSize(new Dimension(600, 400));
        
        return panel;
    }
    
    private JPanel createStatsPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setPreferredSize(new Dimension(250, 0));
        
        return panel;
    }
    
    private void updateStatsPanel() {
        if (statsPanel == null) return;
        
        statsPanel.removeAll();
        
        // Título
        JLabel statsTitle = new JLabel("📋 Estatísticas");
        statsTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        statsTitle.setForeground(TEXT_PRIMARY);
        statsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        statsPanel.add(statsTitle);
        
        statsPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        if (currentResults == null || currentResults.isEmpty()) {
            JLabel noData = new JLabel("<html><div style='text-align: center; color: #999;'>Execute testes para ver estatísticas</div></html>");
            noData.setAlignmentX(Component.LEFT_ALIGNMENT);
            statsPanel.add(noData);
        } else {
            // Estatísticas gerais
            addStatCard(statsPanel, "Total de Testes", String.valueOf(currentResults.size()), "📊");
            addStatCard(statsPanel, "Algoritmos", String.valueOf(getUniqueAlgorithms()), "⚡");
            
            // Estatísticas específicas do gráfico atual
            switch (currentChartType) {
                case "COMPARATIVO":
                    addComparativeStats(statsPanel);
                    break;
                case "ESCALABILIDADE":
                    addScalabilityStats(statsPanel);
                    break;
                case "MEMORIA":
                    addMemoryStats(statsPanel);
                    break;
            }
        }
        
        statsPanel.revalidate();
        statsPanel.repaint();
    }
    
    private void addStatCard(JPanel parent, String title, String value, String icon) {
        JPanel card = new JPanel();
        card.setLayout(new BorderLayout(10, 0));
        card.setBackground(new Color(40, 40, 40));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(70, 70, 70), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 80));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        iconLabel.setForeground(TEXT_PRIMARY);
        
        JPanel textPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        textPanel.setBackground(new Color(40, 40, 40));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel.setForeground(TEXT_SECONDARY);
        
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueLabel.setForeground(TEXT_PRIMARY);
        
        textPanel.add(titleLabel);
        textPanel.add(valueLabel);
        
        card.add(iconLabel, BorderLayout.WEST);
        card.add(textPanel, BorderLayout.CENTER);
        
        parent.add(card);
        parent.add(Box.createRigidArea(new Dimension(0, 10)));
    }
    
    private void setupInteractivity() {
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                lastMousePoint = e.getPoint();
                checkHover(e.getPoint());
                repaint();
            }
        });
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    JPopupMenu contextMenu = createContextMenu();
                    contextMenu.show(ChartPanel.this, e.getX(), e.getY());
                } else {
                    handleClick(e.getPoint());
                }
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                hoveredAlgorithm = null;
                hoveredPointInfo = null;
                repaint();
            }
        });
    }
    
    private JPopupMenu createContextMenu() {
        JPopupMenu contextMenu = new JPopupMenu();
        
        JMenuItem exportChartItem = new JMenuItem("📷 Exportar Gráfico como Imagem");
        exportChartItem.addActionListener(e -> exportChartAsImage());
        
        JMenuItem exportDataItem = new JMenuItem("📊 Exportar Dados como CSV");
        exportDataItem.addActionListener(e -> exportDataAsCSV());
        
        JMenuItem exportReportItem = new JMenuItem("📄 Gerar Relatório HTML");
        exportReportItem.addActionListener(e -> exportDetailedReport());
        
        contextMenu.add(exportChartItem);
        contextMenu.add(exportDataItem);
        contextMenu.add(exportReportItem);
        
        return contextMenu;
    }
    
    private void checkHover(Point point) {
        hoveredAlgorithm = null;
        hoveredPointInfo = null;
        
        if (chartDisplayPanel.getBounds().contains(point)) {
            Point chartPoint = SwingUtilities.convertPoint(this, point, chartDisplayPanel);
            
            for (Map.Entry<String, Rectangle> entry : barAreas.entrySet()) {
                if (entry.getValue().contains(chartPoint)) {
                    hoveredAlgorithm = entry.getKey();
                    break;
                }
            }
            
            for (Map.Entry<Point, String> entry : pointInfo.entrySet()) {
                if (new Rectangle(entry.getKey().x - 6, entry.getKey().y - 6, 12, 12).contains(chartPoint)) {
                    hoveredPointInfo = entry.getValue();
                    break;
                }
            }
        }
    }
    
    private void handleClick(Point point) {
        if (hoveredAlgorithm != null) {
            Map<String, Double> averages = calculateAlgorithmAverages();
            double time = averages.get(hoveredAlgorithm);
            
            JOptionPane.showMessageDialog(this, 
                "Algoritmo: " + hoveredAlgorithm + "\n" +
                "Tempo Médio: " + String.format("%.0fms", time) + "\n" +
                "Testes realizados: " + getTestCountForAlgorithm(hoveredAlgorithm), 
                "Informações do Algoritmo", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private int getTestCountForAlgorithm(String algorithm) {
        if (currentResults == null) return 0;
        return (int) currentResults.stream()
            .filter(r -> r.getAlgorithmName().equals(algorithm))
            .count();
    }
    
    private void paintChart(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Limpar áreas interativas
        barAreas.clear();
        pointInfo.clear();
        
        Dimension size = chartDisplayPanel.getSize();
        int margin = 60;
        int chartWidth = size.width - 2 * margin;
        int chartHeight = size.height - 2 * margin;
        int chartX = margin;
        int chartY = margin;
        
        if (currentResults == null || currentResults.isEmpty()) {
            drawNoDataMessage(g2d);
            return;
        }
        
        // Desenhar a moldura do gráfico
        drawChartFrame(g2d, chartX, chartY, chartWidth, chartHeight, getChartTitle(currentChartType));
        
        switch (currentChartType) {
            case "COMPARATIVO":
                drawComparativeChart(g2d, chartX, chartY, chartWidth, chartHeight);
                break;
            case "ESCALABILIDADE":
                drawScalabilityChart(g2d, chartX, chartY, chartWidth, chartHeight);
                break;
            case "MEMORIA":
                drawMemoryChart(g2d, chartX, chartY, chartWidth, chartHeight);
                break;
        }
        
        // Desenhar tooltip se hover ativo
        if (hoveredAlgorithm != null || hoveredPointInfo != null) {
            Point relativePoint = SwingUtilities.convertPoint(this, lastMousePoint, chartDisplayPanel);
            drawTooltip(g2d, relativePoint);
        }
    }

    private String getChartTitle(String type) {
        switch (type) {
            case "COMPARATIVO": return "Comparativo de Performance (Tempo Médio)";
            case "ESCALABILIDADE": return "Teste de Escalabilidade (Tamanho vs Tempo)";
            case "MEMORIA": return "Uso de Memória por Algoritmo";
            default: return "Dashboard de Performance";
        }
    }
    
    private void drawComparativeChart(Graphics2D g2d, int x, int y, int width, int height) {
        Map<String, Double> averages = calculateAlgorithmAverages();
        if (averages.isEmpty()) return;
        
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(averages.entrySet());
        sorted.sort(Map.Entry.comparingByValue());
        
        double maxTime = sorted.stream().mapToDouble(Map.Entry::getValue).max().orElse(100.0) * 1.1;
        
        int barCount = sorted.size();
        // Ajuste especial para quando há apenas 1 algoritmo
        int barWidth = barCount == 1 ? Math.min(80, width / 3) : Math.max(20, width / (barCount * 2));
        int spacing = barCount == 1 ? (width - barWidth) / 2 : (width - (barCount * barWidth)) / (barCount + 1);
        
        for (int i = 0; i < sorted.size(); i++) {
            String algorithm = sorted.get(i).getKey();
            double time = sorted.get(i).getValue();
            
            int barX = barCount == 1 ? x + spacing : x + spacing + i * (barWidth + spacing);
            int chartAreaHeight = height - 40;
            int targetHeight = (int) ((time / maxTime) * chartAreaHeight);
            int animatedHeight = (int) (targetHeight * animationProgress);
            int barY = y + chartAreaHeight - animatedHeight;
            
            Color color = CHART_COLORS[i % CHART_COLORS.length];
            
            // Barra com gradiente mais suave
            GradientPaint gradient = new GradientPaint(
                barX, barY, color.brighter(),
                barX, barY + animatedHeight, color
            );
            g2d.setPaint(gradient);
            g2d.fillRect(barX, barY, barWidth, animatedHeight);
            
            // Borda sutil
            g2d.setColor(color.darker());
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(barX, barY, barWidth, animatedHeight);
            
            // Área interativa
            barAreas.put(algorithm, new Rectangle(barX, barY, barWidth, animatedHeight));
            
            // Destaque no hover
            if (algorithm.equals(hoveredAlgorithm)) {
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fillRect(barX, barY, barWidth, animatedHeight);
                g2d.setColor(ACCENT);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(barX, barY, barWidth, animatedHeight);
            }
            
            // Valor no topo
            if (animationProgress > 0.7f) {
                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String timeText = String.format("%.0fms", time);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(timeText);
                g2d.drawString(timeText, barX + (barWidth - textWidth) / 2, barY - 5);
            }
            
            // Label
            g2d.setColor(TEXT_SECONDARY);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            drawRotatedText(g2d, algorithm, barX + barWidth / 2, y + height - 30, -45);
        }

        // Desenhar labels do eixo Y (Tempo)
        g2d.setColor(TEXT_SECONDARY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        DecimalFormat df = new DecimalFormat("#.0");
        int chartAreaHeight = height - 40;
        for (int i = 0; i <= 5; i++) {
            double value = maxTime * i / 5.0;
            int lineY = y + chartAreaHeight - (int) ((value / maxTime) * chartAreaHeight);
            g2d.drawString(df.format(value) + "ms", x - 40, lineY + 4);
        }
    }
    
    private void drawScalabilityChart(Graphics2D g2d, int x, int y, int width, int height) {
        // Agrupar resultados por algoritmo e tamanho
        Map<String, Map<Integer, Double>> algorithmData = new HashMap<>();
        
        for (PerformanceResult result : currentResults) {
            String algo = result.getAlgorithmName();
            int size = result.getDataSize();
            double time = result.getExecutionTime();
            
            algorithmData.computeIfAbsent(algo, k -> new HashMap<>())
                        .put(size, time);
        }
        
        if (algorithmData.isEmpty()) {
            drawNoScalabilityData(g2d, x, y, width, height);
            return;
        }
        
        // Encontrar valores máximos para escalas
        int maxSize = algorithmData.values().stream()
            .flatMap(map -> map.keySet().stream())
            .max(Integer::compareTo)
            .orElse(1000);
        
        double maxTime = algorithmData.values().stream()
            .flatMap(map -> map.values().stream())
            .max(Double::compareTo)
            .orElse(1000.0) * 1.1;
        
        // Desenhar linhas para cada algoritmo
        int colorIndex = 0;
        pointInfo.clear();
        
        for (Map.Entry<String, Map<Integer, Double>> entry : algorithmData.entrySet()) {
            String algorithm = entry.getKey();
            Map<Integer, Double> data = entry.getValue();
            
            // Ordenar por tamanho
            List<Map.Entry<Integer, Double>> sortedData = new ArrayList<>(data.entrySet());
            sortedData.sort(Map.Entry.comparingByKey());
            
            Color color = CHART_COLORS[colorIndex % CHART_COLORS.length];
            g2d.setColor(color);
            g2d.setStroke(new BasicStroke(3f));
            
            // Desenhar linha
            Point2D prevPoint = null;
            for (Map.Entry<Integer, Double> dataPoint : sortedData) {
                int dataSize = dataPoint.getKey();
                double time = dataPoint.getValue();
                
                int pointX = x + (int) ((double) dataSize / maxSize * width * animationProgress);
                int pointY = y + height - 40 - (int) ((time / maxTime) * (height - 40));
                
                Point2D currentPoint = new Point2D.Double(pointX, pointY);
                
                if (prevPoint != null) {
                    g2d.drawLine((int)prevPoint.getX(), (int)prevPoint.getY(), 
                                (int)currentPoint.getX(), (int)currentPoint.getY());
                }
                
                // Desenhar ponto
                g2d.fillOval((int)currentPoint.getX() - 4, (int)currentPoint.getY() - 4, 8, 8);
                
                // Armazenar informação para tooltip
                String info = algorithm + "|" + dataSize + "|" + (long)time;
                pointInfo.put(new Point((int)currentPoint.getX(), (int)currentPoint.getY()), info);
                
                prevPoint = currentPoint;
            }
            
            // Legenda
            g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
            g2d.drawString(algorithm, x + 10, y + 20 + colorIndex * 20);
            g2d.fillRect(x, y + 15 + colorIndex * 20, 8, 8);
            
            colorIndex++;
        }
        
        // Eixos e labels
        drawScalabilityAxes(g2d, x, y, width, height, maxSize, maxTime);
    }
    
    private void drawScalabilityAxes(Graphics2D g2d, int x, int y, int width, int height, int maxSize, double maxTime) {
        g2d.setColor(TEXT_SECONDARY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        // Eixo Y (Tempo)
        DecimalFormat df = new DecimalFormat("#.0");
        for (int i = 0; i <= 5; i++) {
            double value = maxTime * i / 5.0;
            int lineY = y + height - 40 - (int) ((value / maxTime) * (height - 40));
            g2d.drawString(df.format(value) + "ms", x - 40, lineY + 4);
        }
        
        // Eixo X (Tamanho)
        for (int i = 0; i <= 5; i++) {
            int value = maxSize * i / 5;
            int lineX = x + (int) ((double) value / maxSize * width);
            g2d.drawString(formatSize(value), lineX - 15, y + height - 20);
        }
        
        // Labels dos eixos
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2d.drawString("Tempo (ms)", x - 50, y + height / 2);
        g2d.drawString("Tamanho dos Dados", x + width / 2 - 40, y + height - 5);
    }
    
    private void drawMemoryChart(Graphics2D g2d, int x, int y, int width, int height) {
        Map<String, Double> memoryAverages = calculateMemoryAverages();
        
        // Verifica se há dados válidos de memória
        boolean hasValidData = !memoryAverages.isEmpty() && 
                              memoryAverages.values().stream().anyMatch(avg -> avg > 0);
        
        if (!hasValidData) {
            drawNoMemoryData(g2d, x, y, width, height);
            return;
        }
        
        List<Map.Entry<String, Double>> sorted = new ArrayList<>(memoryAverages.entrySet());
        sorted.sort(Map.Entry.comparingByValue());
        
        double maxMemory = sorted.stream().mapToDouble(Map.Entry::getValue).max().orElse(100.0) * 1.1;
        
        int barCount = sorted.size();
        // Ajuste especial para quando há apenas 1 algoritmo
        int barWidth = barCount == 1 ? Math.min(80, width / 3) : Math.max(20, width / (barCount * 2));
        int spacing = barCount == 1 ? (width - barWidth) / 2 : (width - (barCount * barWidth)) / (barCount + 1);
        
        for (int i = 0; i < sorted.size(); i++) {
            String algorithm = sorted.get(i).getKey();
            double memory = sorted.get(i).getValue();
            
            // Pula algoritmos sem dados de memória
            if (memory <= 0) continue;
            
            int barX = barCount == 1 ? x + spacing : x + spacing + i * (barWidth + spacing);
            int chartAreaHeight = height - 40;
            int targetHeight = (int) ((memory / maxMemory) * chartAreaHeight);
            int animatedHeight = (int) (targetHeight * animationProgress);
            int barY = y + chartAreaHeight - animatedHeight;
            
            Color color = CHART_COLORS[i % CHART_COLORS.length];
            
            // Barra com gradiente mais suave
            GradientPaint gradient = new GradientPaint(
                barX, barY, color.brighter(),
                barX, barY + animatedHeight, color
            );
            g2d.setPaint(gradient);
            g2d.fillRect(barX, barY, barWidth, animatedHeight);
            
            // Borda sutil
            g2d.setColor(color.darker());
            g2d.setStroke(new BasicStroke(1));
            g2d.drawRect(barX, barY, barWidth, animatedHeight);
            
            // Área interativa
            barAreas.put(algorithm, new Rectangle(barX, barY, barWidth, animatedHeight));
            
            // Destaque no hover
            if (algorithm.equals(hoveredAlgorithm)) {
                g2d.setColor(new Color(255, 255, 255, 80));
                g2d.fillRect(barX, barY, barWidth, animatedHeight);
                g2d.setColor(ACCENT);
                g2d.setStroke(new BasicStroke(2));
                g2d.drawRect(barX, barY, barWidth, animatedHeight);
            }
            
            // Valor no topo
            if (animationProgress > 0.7f && animatedHeight > 20) {
                g2d.setColor(TEXT_PRIMARY);
                g2d.setFont(new Font("Segoe UI", Font.BOLD, 11));
                String memoryText = formatMemory((long)memory);
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(memoryText);
                g2d.drawString(memoryText, barX + (barWidth - textWidth) / 2, barY - 5);
            }
            
            // Label
            g2d.setColor(TEXT_SECONDARY);
            g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            drawRotatedText(g2d, algorithm, barX + barWidth / 2, y + height - 30, -45);
        }

        // Desenhar labels do eixo Y (Memória)
        g2d.setColor(TEXT_SECONDARY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        int chartAreaHeight = height - 40;
        for (int i = 0; i <= 5; i++) {
            double value = maxMemory * i / 5.0;
            int lineY = y + chartAreaHeight - (int) ((value / maxMemory) * chartAreaHeight);
            g2d.drawString(formatMemory((long)value), x - 45, lineY + 4);
        }
    }
    
    private void drawNoScalabilityData(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(TEXT_SECONDARY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String message = "Execute testes de escalabilidade para visualizar este gráfico";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(message, x + (width - fm.stringWidth(message)) / 2, y + height / 2);
    }
    
    private void drawNoMemoryData(Graphics2D g2d, int x, int y, int width, int height) {
        g2d.setColor(TEXT_SECONDARY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        String message = "Dados de memória não disponíveis";
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(message, x + (width - fm.stringWidth(message)) / 2, y + height / 2);
        
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        String subMessage = "Execute testes com monitoramento de memória ativado";
        fm = g2d.getFontMetrics();
        g2d.drawString(subMessage, x + (width - fm.stringWidth(subMessage)) / 2, y + height / 2 + 20);
    }
    
    private void drawChartFrame(Graphics2D g2d, int x, int y, int width, int height, String title) {
        // Grade
        g2d.setColor(GRID_COLOR);
        for (int i = 0; i <= 5; i++) {
            int lineY = y + (int)(height * 0.9) - (i * (int)(height * 0.9) / 5);
            g2d.drawLine(x, lineY, x + width, lineY);
        }
        
        // Eixos
        g2d.setColor(new Color(100, 100, 100));
        g2d.drawLine(x, y + (int)(height * 0.9), x + width, y + (int)(height * 0.9));
        g2d.drawLine(x, y, x, y + (int)(height * 0.9));
        
        // Título
        g2d.setColor(TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 16));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(title, x + (width - fm.stringWidth(title)) / 2, y - 20);
    }
    
    private void drawNoDataMessage(Graphics2D g2d) {
        String[] messages = {
            "📊 Bem-vindo ao Dashboard de Performance",
            "Nenhum dado disponível no momento",
            "Execute alguns testes para visualizar os gráficos"
        };
        
        g2d.setColor(TEXT_SECONDARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(messages[0], (chartDisplayPanel.getWidth() - fm.stringWidth(messages[0])) / 2, chartDisplayPanel.getHeight() / 2 - 30);
        
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        fm = g2d.getFontMetrics();
        for (int i = 1; i < messages.length; i++) {
            g2d.drawString(messages[i], (chartDisplayPanel.getWidth() - fm.stringWidth(messages[i])) / 2, chartDisplayPanel.getHeight() / 2 + i * 25);
        }
    }
    
    private void drawTooltip(Graphics2D g2d, Point position) {
        if (hoveredAlgorithm != null) {
            if (currentChartType.equals("MEMORIA")) {
                Map<String, Double> memoryAverages = calculateMemoryAverages();
                double memory = memoryAverages.get(hoveredAlgorithm);
                
                String[] lines = {
                    "Algoritmo: " + hoveredAlgorithm,
                    "Memória Média: " + formatMemory((long)memory),
                    "Testes: " + getTestCountForAlgorithm(hoveredAlgorithm)
                };
                drawTooltipBox(g2d, position, lines);
            } else {
                Map<String, Double> averages = calculateAlgorithmAverages();
                double time = averages.get(hoveredAlgorithm);
                
                String[] lines = {
                    "Algoritmo: " + hoveredAlgorithm,
                    "Tempo Médio: " + String.format("%.0fms", time),
                    "Testes: " + getTestCountForAlgorithm(hoveredAlgorithm)
                };
                drawTooltipBox(g2d, position, lines);
            }
        } else if (hoveredPointInfo != null) {
            // Parse das informações do ponto
            String[] parts = hoveredPointInfo.split("\\|");
            if (parts.length >= 3) {
                String algorithm = parts[0];
                int size = Integer.parseInt(parts[1]);
                long time = Long.parseLong(parts[2]);
                
                String[] lines = {
                    "Algoritmo: " + algorithm,
                    "Tamanho: " + formatSize(size),
                    "Tempo: " + time + "ms"
                };
                drawTooltipBox(g2d, position, lines);
            }
        }
    }
    
    private void drawTooltipBox(Graphics2D g2d, Point position, String[] lines) {
        Font font = new Font("Segoe UI", Font.PLAIN, 11);
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        
        int padding = 8;
        int lineHeight = fm.getHeight();
        int width = 0;
        for (String line : lines) {
            width = Math.max(width, fm.stringWidth(line));
        }
        width += padding * 2;
        int height = lines.length * lineHeight + padding * 2;
        
        int tooltipX = Math.min(position.x + 10, chartDisplayPanel.getWidth() - width - 10);
        int tooltipY = Math.min(position.y - height - 10, chartDisplayPanel.getHeight() - height - 10);
        
        // Fundo do tooltip (mais escuro)
        g2d.setColor(new Color(20, 20, 20, 240));
        g2d.fillRoundRect(tooltipX, tooltipY, width, height, 5, 5);
        
        // Borda do tooltip
        g2d.setColor(new Color(80, 80, 80));
        g2d.drawRoundRect(tooltipX, tooltipY, width, height, 5, 5);
        
        // Texto
        g2d.setColor(TEXT_PRIMARY);
        for (int i = 0; i < lines.length; i++) {
            g2d.drawString(lines[i], tooltipX + padding, tooltipY + padding + (i + 1) * lineHeight - fm.getDescent());
        }
    }
    
    private void drawRotatedText(Graphics2D g2d, String text, int x, int y, double angle) {
        AffineTransform original = g2d.getTransform();
        g2d.translate(x, y);
        g2d.rotate(Math.toRadians(angle));
        g2d.setColor(TEXT_SECONDARY);
        g2d.drawString(text, 0, 0);
        g2d.setTransform(original);
    }
    
    private Map<String, Double> calculateAlgorithmAverages() {
        Map<String, Double> averages = new HashMap<>();
        if (currentResults == null) return averages;
        
        Map<String, List<Long>> times = new HashMap<>();
        
        for (PerformanceResult result : currentResults) {
            times.computeIfAbsent(result.getAlgorithmName(), k -> new ArrayList<>())
                .add(result.getExecutionTime());
        }
        
        for (Map.Entry<String, List<Long>> entry : times.entrySet()) {
            double avg = entry.getValue().stream().mapToLong(Long::longValue).average().orElse(0);
            averages.put(entry.getKey(), avg);
        }
        
        return averages;
    }
    
    private Map<String, Double> calculateMemoryAverages() {
        Map<String, Double> averages = new HashMap<>();
        if (currentResults == null || currentResults.isEmpty()) return averages;
        
        Map<String, List<Long>> memoryUsage = new HashMap<>();
        
        for (PerformanceResult result : currentResults) {
            // Usa o método getMemoryUsed() da classe PerformanceResult
            long memory = result.getMemoryUsed();
            
            memoryUsage.computeIfAbsent(result.getAlgorithmName(), k -> new ArrayList<>())
                      .add(memory);
        }
        
        for (Map.Entry<String, List<Long>> entry : memoryUsage.entrySet()) {
            double avg = entry.getValue().stream()
                .mapToLong(Long::longValue)
                .average()
                .orElse(0.0);
            averages.put(entry.getKey(), avg);
        }
        
        return averages;
    }
    
    private int getUniqueAlgorithms() {
        if (currentResults == null) return 0;
        return (int) currentResults.stream()
            .map(PerformanceResult::getAlgorithmName)
            .distinct()
            .count();
    }
    
    private PerformanceResult getBestPerformance() {
        if (currentResults == null || currentResults.isEmpty()) return null;
        return currentResults.stream()
            .min(Comparator.comparingLong(PerformanceResult::getExecutionTime))
            .orElse(null);
    }
    
    private void addComparativeStats(JPanel panel) {
        Map<String, Double> averages = calculateAlgorithmAverages();
        if (!averages.isEmpty()) {
            double max = averages.values().stream().max(Double::compare).orElse(0.0);
            double min = averages.values().stream().min(Double::compare).orElse(0.0);
            double avg = averages.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            addStatCard(panel, "Tempo Máximo", String.format("%.0fms", max), "⏱️");
            addStatCard(panel, "Tempo Mínimo", String.format("%.0fms", min), "⚡");
            addStatCard(panel, "Média Geral", String.format("%.0fms", avg), "📊");
            
            // Melhor algoritmo
            String bestAlgo = averages.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
            addStatCard(panel, "Melhor Algoritmo", bestAlgo, "🏆");
        }
    }
    
    private void addScalabilityStats(JPanel panel) {
        if (currentResults != null && !currentResults.isEmpty()) {
            // Encontrar maior tamanho testado
            int maxSize = currentResults.stream()
                .mapToInt(PerformanceResult::getDataSize)
                .max()
                .orElse(0);
            
            // Contar algoritmos únicos testados em escalabilidade
            long scalabilityAlgos = currentResults.stream()
                .map(PerformanceResult::getAlgorithmName)
                .distinct()
                .count();
            
            addStatCard(panel, "Maior Tamanho", formatSize(maxSize), "📈");
            addStatCard(panel, "Algoritmos Testados", String.valueOf(scalabilityAlgos), "⚡");
            addStatCard(panel, "Pontos de Dados", String.valueOf(currentResults.size()), "📊");
        }
    }
    
    private void addMemoryStats(JPanel panel) {
        Map<String, Double> memoryAverages = calculateMemoryAverages();
        
        // Verifica se há dados válidos de memória
        boolean hasValidData = !memoryAverages.isEmpty() && 
                              memoryAverages.values().stream().anyMatch(avg -> avg > 0);
        
        if (hasValidData) {
            double max = memoryAverages.values().stream().max(Double::compare).orElse(0.0);
            double min = memoryAverages.values().stream().min(Double::compare).orElse(0.0);
            double avg = memoryAverages.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            
            addStatCard(panel, "Memória Máxima", formatMemory((long)max), "💾");
            addStatCard(panel, "Memória Mínima", formatMemory((long)min), "⚡");
            addStatCard(panel, "Média de Memória", formatMemory((long)avg), "📊");
            
            // Algoritmo mais eficiente em memória
            String bestAlgo = memoryAverages.entrySet().stream()
                .min(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("N/A");
            addStatCard(panel, "Mais Eficiente", bestAlgo, "🏆");
        } else {
            addStatCard(panel, "Status Memória", "Dados Indisponíveis", "💾");
            addStatCard(panel, "Ação Necessária", "Ativar Monitoramento", "⚡");
        }
    }
    
    private String formatMemory(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return (bytes / 1024) + " KB";
        return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
    }
    
    private String formatSize(int size) {
        if (size < 1000) return size + "";
        if (size < 1000000) return (size / 1000) + "K";
        return String.format("%.1fM", size / 1000000.0);
    }
    
    private void setupAnimation() {
        animationTimer = new Timer(16, e -> {
            animationProgress += 0.05f;
            if (animationProgress >= 1.0f) {
                animationProgress = 1.0f;
                animationTimer.stop();
            }
            repaint();
        });
    }
    
    public void updateCharts() {
        if (controller != null) {
            currentResults = controller.getTestHistory();
        }
        if (currentResults == null) {
            currentResults = new ArrayList<>();
        }
        updateStatsPanel();
        startAnimation();
    }
    
    private void startAnimation() {
        animationProgress = 0f;
        if (animationTimer != null && !animationTimer.isRunning()) {
            animationTimer.start();
        }
    }
    
    // ===== MÉTODOS DE EXPORTAÇÃO =====
    
    private void exportChartAsImage() {
        if (currentResults == null || currentResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum dado disponível para exportar.\nExecute alguns testes primeiro.", 
                "Exportar Gráfico", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Gráfico como Imagem");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos PNG (*.png)", "png"));
        fileChooser.setSelectedFile(new File("grafico_performance_" + currentChartType.toLowerCase() + ".png"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".png")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".png");
            }
            
            try {
                // Criar uma imagem em alta resolução do gráfico
                BufferedImage chartImage = createHighResChartImage();
                ImageIO.write(chartImage, "PNG", fileToSave);
                
                JOptionPane.showMessageDialog(this, 
                    "Gráfico exportado com sucesso!\n" + fileToSave.getName(), 
                    "Exportação Concluída", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao exportar gráfico:\n" + ex.getMessage(), 
                    "Erro na Exportação", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private BufferedImage createHighResChartImage() {
        // Criar uma imagem com resolução maior para melhor qualidade
        int width = 1200;
        int height = 800;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        
        // Configurar qualidade de renderização
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        // Preencher fundo
        g2d.setColor(BACKGROUND);
        g2d.fillRect(0, 0, width, height);
        
        // Desenhar título principal
        g2d.setColor(TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 24));
        String mainTitle = "Dashboard de Performance - " + getChartTitle(currentChartType);
        FontMetrics fm = g2d.getFontMetrics();
        g2d.drawString(mainTitle, (width - fm.stringWidth(mainTitle)) / 2, 40);
        
        // Desenhar informações de data/hora
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2d.setColor(TEXT_SECONDARY);
        String timestamp = "Exportado em: " + new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date());
        g2d.drawString(timestamp, width - 250, 70);
        
        // Desenhar o gráfico principal
        int chartWidth = width - 100;
        int chartHeight = height - 150;
        int chartX = 50;
        int chartY = 100;
        
        // Salvar progresso da animação e restaurar depois
        float savedProgress = animationProgress;
        animationProgress = 1.0f; // Forçar gráfico completo
        
        // Desenhar o gráfico baseado no tipo atual
        Graphics2D chartG2d = (Graphics2D) g2d.create(chartX, chartY, chartWidth, chartHeight);
        drawChartFrame(chartG2d, 0, 0, chartWidth, chartHeight, "");
        
        switch (currentChartType) {
            case "COMPARATIVO":
                drawComparativeChart(chartG2d, 0, 0, chartWidth, chartHeight);
                break;
            case "ESCALABILIDADE":
                drawScalabilityChart(chartG2d, 0, 0, chartWidth, chartHeight);
                break;
            case "MEMORIA":
                drawMemoryChart(chartG2d, 0, 0, chartWidth, chartHeight);
                break;
        }
        
        // Restaurar progresso da animação
        animationProgress = savedProgress;
        
        // Desenhar legenda de estatísticas
        drawExportStats(g2d, width, height);
        
        g2d.dispose();
        return image;
    }
    
    private void drawExportStats(Graphics2D g2d, int totalWidth, int totalHeight) {
        int statsX = 50;
        int statsY = totalHeight - 30;
        
        g2d.setColor(TEXT_SECONDARY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        String statsText = String.format("Total de Testes: %d | Algoritmos: %d | Última Atualização: %s",
            currentResults.size(),
            getUniqueAlgorithms(),
            new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
        
        g2d.drawString(statsText, statsX, statsY);
    }
    
    private void exportDataAsCSV() {
        if (currentResults == null || currentResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum dado disponível para exportar.\nExecute alguns testes primeiro.", 
                "Exportar Dados", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Exportar Dados como CSV");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos CSV (*.csv)", "csv"));
        fileChooser.setSelectedFile(new File("dados_performance.csv"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }
            
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(fileToSave), StandardCharsets.UTF_8))) {
                
                // Escrever cabeçalho
                writer.println("Algoritmo,Tamanho_Dados,Tempo_Execucao(ms),Memoria_Utilizada(bytes),Data_Execucao");
                
                // Escrever dados
                for (PerformanceResult result : currentResults) {
                    writer.printf("%s,%d,%d,%d,%s\n",
                        result.getAlgorithmName(),
                        result.getDataSize(),
                        result.getExecutionTime(),
                        result.getMemoryUsed(),
                        new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(result.getTimestamp()))
                    );
                }
                
                // Escrever estatísticas resumidas
                writer.println();
                writer.println("ESTATÍSTICAS RESUMIDAS");
                writer.println("Algoritmo,Media_Tempo(ms),Media_Memoria(bytes),Total_Testes");
                
                Map<String, Double> timeAverages = calculateAlgorithmAverages();
                Map<String, Double> memoryAverages = calculateMemoryAverages();
                
                for (String algorithm : timeAverages.keySet()) {
                    double avgTime = timeAverages.get(algorithm);
                    double avgMemory = memoryAverages.getOrDefault(algorithm, 0.0);
                    int testCount = getTestCountForAlgorithm(algorithm);
                    
                    writer.printf("%s,%.2f,%.0f,%d\n",
                        algorithm, avgTime, avgMemory, testCount);
                }
                
                JOptionPane.showMessageDialog(this, 
                    "Dados exportados com sucesso!\n" + fileToSave.getName() + 
                    "\n\nTotal de registros: " + currentResults.size(), 
                    "Exportação Concluída", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao exportar dados:\n" + ex.getMessage(), 
                    "Erro na Exportação", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    public void exportDetailedReport() {
        if (currentResults == null || currentResults.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum dado disponível para gerar relatório.", 
                "Relatório Detalhado", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Salvar Relatório Detalhado");
        fileChooser.setFileFilter(new FileNameExtensionFilter("Arquivos HTML (*.html)", "html"));
        fileChooser.setSelectedFile(new File("relatorio_performance.html"));
        
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            if (!fileToSave.getName().toLowerCase().endsWith(".html")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".html");
            }
            
            try (PrintWriter writer = new PrintWriter(new OutputStreamWriter(
                    new FileOutputStream(fileToSave), StandardCharsets.UTF_8))) {
                
                generateHTMLReport(writer);
                
                JOptionPane.showMessageDialog(this, 
                    "Relatório HTML gerado com sucesso!\n" + fileToSave.getName(), 
                    "Relatório Concluído", 
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, 
                    "Erro ao gerar relatório:\n" + ex.getMessage(), 
                    "Erro na Geração", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void generateHTMLReport(PrintWriter writer) {
        String timestamp = new java.text.SimpleDateFormat("dd/MM/yyyy 'às' HH:mm:ss").format(new Date());
        
        writer.println("<!DOCTYPE html>");
        writer.println("<html lang='pt-BR'>");
        writer.println("<head>");
        writer.println("    <meta charset='UTF-8'>");
        writer.println("    <title>Relatório de Performance</title>");
        writer.println("    <style>");
        writer.println("        body { font-family: Arial, sans-serif; margin: 40px; background: #f5f5f5; }");
        writer.println("        .header { background: #2c3e50; color: white; padding: 20px; border-radius: 5px; }");
        writer.println("        .card { background: white; padding: 20px; margin: 10px 0; border-radius: 5px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }");
        writer.println("        table { width: 100%; border-collapse: collapse; margin: 10px 0; }");
        writer.println("        th, td { padding: 12px; text-align: left; border-bottom: 1px solid #ddd; }");
        writer.println("        th { background: #34495e; color: white; }");
        writer.println("        .stats { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 10px; }");
        writer.println("        .stat-item { background: #ecf0f1; padding: 15px; border-radius: 5px; text-align: center; }");
        writer.println("        .algorithm-best { background: #d4edda; }");
        writer.println("        .algorithm-worst { background: #f8d7da; }");
        writer.println("    </style>");
        writer.println("</head>");
        writer.println("<body>");
        
        writer.println("<div class='header'>");
        writer.println("    <h1>📊 Relatório de Performance</h1>");
        writer.println("    <p>Gerado em: " + timestamp + "</p>");
        writer.println("</div>");
        
        // Estatísticas gerais
        writer.println("<div class='card'>");
        writer.println("    <h2>📈 Estatísticas Gerais</h2>");
        writer.println("    <div class='stats'>");
        writer.println("        <div class='stat-item'><h3>" + currentResults.size() + "</h3><p>Total de Testes</p></div>");
        writer.println("        <div class='stat-item'><h3>" + getUniqueAlgorithms() + "</h3><p>Algoritmos</p></div>");
        writer.println("        <div class='stat-item'><h3>" + getTotalExecutionTime() + "ms</h3><p>Tempo Total</p></div>");
        writer.println("    </div>");
        writer.println("</div>");
        
        // Tabela de desempenho por algoritmo
        writer.println("<div class='card'>");
        writer.println("    <h2>⚡ Desempenho por Algoritmo</h2>");
        writer.println("    <table>");
        writer.println("        <tr><th>Algoritmo</th><th>Tempo Médio (ms)</th><th>Memória Média</th><th>Testes</th><th>Melhor Tempo</th><th>Pior Tempo</th></tr>");
        
        Map<String, Double> timeAverages = calculateAlgorithmAverages();
        Map<String, Double> memoryAverages = calculateMemoryAverages();
        Map<String, Long> bestTimes = getBestTimes();
        Map<String, Long> worstTimes = getWorstTimes();
        
        for (String algorithm : timeAverages.keySet()) {
            String rowClass = "";
            if (algorithm.equals(getBestAlgorithm(timeAverages))) {
                rowClass = "class='algorithm-best'";
            } else if (algorithm.equals(getWorstAlgorithm(timeAverages))) {
                rowClass = "class='algorithm-worst'";
            }
            
            writer.printf("        <tr %s><td>%s</td><td>%.2f</td><td>%s</td><td>%d</td><td>%d</td><td>%d</td></tr>\n",
                rowClass,
                algorithm,
                timeAverages.get(algorithm),
                formatMemory((long)memoryAverages.getOrDefault(algorithm, 0.0).doubleValue()),
                getTestCountForAlgorithm(algorithm),
                bestTimes.get(algorithm),
                worstTimes.get(algorithm)
            );
        }
        
        writer.println("    </table>");
        writer.println("</div>");
        
        // Dados detalhados
        writer.println("<div class='card'>");
        writer.println("    <h2>📋 Dados Detalhados</h2>");
        writer.println("    <table>");
        writer.println("        <tr><th>Algoritmo</th><th>Tamanho</th><th>Tempo (ms)</th><th>Memória</th><th>Data/Hora</th></tr>");
        
        for (PerformanceResult result : currentResults) {
            writer.printf("        <tr><td>%s</td><td>%d</td><td>%d</td><td>%s</td><td>%s</td></tr>\n",
                result.getAlgorithmName(),
                result.getDataSize(),
                result.getExecutionTime(),
                formatMemory(result.getMemoryUsed()),
                new java.text.SimpleDateFormat("dd/MM/yy HH:mm").format(new Date(result.getTimestamp()))
            );
        }
        
        writer.println("    </table>");
        writer.println("</div>");
        
        writer.println("</body>");
        writer.println("</html>");
    }
    
    // Métodos auxiliares para o relatório
    private long getTotalExecutionTime() {
        return currentResults.stream().mapToLong(PerformanceResult::getExecutionTime).sum();
    }
    
    private Map<String, Long> getBestTimes() {
        Map<String, Long> bestTimes = new HashMap<>();
        for (PerformanceResult result : currentResults) {
            bestTimes.merge(result.getAlgorithmName(), result.getExecutionTime(), Math::min);
        }
        return bestTimes;
    }
    
    private Map<String, Long> getWorstTimes() {
        Map<String, Long> worstTimes = new HashMap<>();
        for (PerformanceResult result : currentResults) {
            worstTimes.merge(result.getAlgorithmName(), result.getExecutionTime(), Math::max);
        }
        return worstTimes;
    }
    
    private String getBestAlgorithm(Map<String, Double> averages) {
        return averages.entrySet().stream()
            .min(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
    }
    
    private String getWorstAlgorithm(Map<String, Double> averages) {
        return averages.entrySet().stream()
            .max(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .orElse("N/A");
    }
}
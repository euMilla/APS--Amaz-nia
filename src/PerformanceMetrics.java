import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import java.util.List;

public class PerformanceMetrics extends JPanel {
    private Map<String, Double> algorithmPerformance;
    private List<Double> historicalData;
    private String currentMetric;
    private boolean showTrendLine;
    
    private static final Color GRID_COLOR = new Color(60, 60, 70);
    private static final Color TEXT_COLOR = new Color(240, 240, 240);
    private static final Color CHART_BG = new Color(30, 30, 38);
    private static final Color TREND_COLOR = new Color(255, 100, 100, 150);
    
    public PerformanceMetrics() {
        this.algorithmPerformance = new LinkedHashMap<>();
        this.historicalData = new ArrayList<>();
        this.currentMetric = "Execution Time";
        this.showTrendLine = true;
        setBackground(CHART_BG);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    }
    
    public void updateMetrics(Map<String, Double> newData) {
        this.algorithmPerformance.clear();
        this.algorithmPerformance.putAll(newData);
        
        // Adicionar aos dados históricos para tendência
        double average = newData.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        historicalData.add(average);
        if (historicalData.size() > 50) historicalData.remove(0);
        
        repaint();
    }
    
    public void setCurrentMetric(String metric) {
        this.currentMetric = metric;
        repaint();
    }
    
    public void setShowTrendLine(boolean show) {
        this.showTrendLine = show;
        repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        drawBackground(g2);
        
        if (algorithmPerformance.isEmpty()) {
            drawPlaceholder(g2);
            return;
        }
        
        drawGrid(g2);
        drawChart(g2);
        drawLegend(g2);
        if (showTrendLine) drawTrendLine(g2);
    }
    
    private void drawBackground(Graphics2D g2) {
        // Gradiente de fundo
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(25, 25, 32),
            getWidth(), getHeight(), new Color(35, 35, 45)
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        // Borda decorativa
        g2.setColor(new Color(70, 70, 80));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(5, 5, getWidth()-10, getHeight()-10, 15, 15);
    }
    
    private void drawPlaceholder(Graphics2D g2) {
        g2.setColor(TEXT_COLOR);
        g2.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        
        String message = "No performance data available";
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(message)) / 2;
        int y = getHeight() / 2;
        
        g2.drawString(message, x, y);
        
        String instruction = "Run tests to see performance metrics";
        fm = g2.getFontMetrics();
        x = (getWidth() - fm.stringWidth(instruction)) / 2;
        g2.drawString(instruction, x, y + 25);
    }
    
    private void drawGrid(Graphics2D g2) {
        g2.setColor(GRID_COLOR);
        g2.setStroke(new BasicStroke(1));
        
        int padding = 80;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding;
        
        // Linhas horizontais
        int horizontalLines = 10;
        for (int i = 0; i <= horizontalLines; i++) {
            int y = padding + (i * chartHeight) / horizontalLines;
            g2.drawLine(padding, y, padding + chartWidth, y);
        }
        
        // Linhas verticais
        int verticalLines = algorithmPerformance.isEmpty() ? 10 : algorithmPerformance.size();
        for (int i = 0; i <= verticalLines; i++) {
            int x = padding + (i * chartWidth) / verticalLines;
            g2.drawLine(x, padding, x, padding + chartHeight);
        }
    }
    
    private void drawChart(Graphics2D g2) {
        if (algorithmPerformance.isEmpty()) return;
        
        int padding = 80;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding;
        
        // Encontrar valores máximo e mínimo
        double maxValue = algorithmPerformance.values().stream().max(Double::compare).orElse(1.0);
        double minValue = algorithmPerformance.values().stream().min(Double::compare).orElse(0.0);
        double range = Math.max(maxValue - minValue, 0.1); // Evitar divisão por zero
        
        // Desenhar barras
        int barCount = algorithmPerformance.size();
        int barWidth = Math.max(chartWidth / (barCount * 2), 20);
        int spacing = (chartWidth - (barCount * barWidth)) / (barCount + 1);
        
        int index = 0;
        for (Map.Entry<String, Double> entry : algorithmPerformance.entrySet()) {
            double value = entry.getValue();
            double normalizedValue = (value - minValue) / range;
            
            int barHeight = (int) (normalizedValue * chartHeight);
            int x = padding + spacing + index * (barWidth + spacing);
            int y = padding + chartHeight - barHeight;
            
            // Cor baseada no desempenho (verde = bom, vermelho = ruim)
            Color barColor = getPerformanceColor(value, minValue, maxValue);
            
            // Gradiente para a barra
            GradientPaint gradient = new GradientPaint(
                x, y, barColor.brighter(),
                x, y + barHeight, barColor.darker()
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
            
            // Borda da barra
            g2.setColor(barColor.darker());
            g2.setStroke(new BasicStroke(1.5f));
            g2.drawRoundRect(x, y, barWidth, barHeight, 8, 8);
            
            // Valor numérico
            g2.setColor(TEXT_COLOR);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
            String valueText = String.format("%.4f", value);
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(valueText);
            g2.drawString(valueText, x + (barWidth - textWidth) / 2, y - 5);
            
            // Nome do algoritmo
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            String algoName = entry.getKey();
            textWidth = fm.stringWidth(algoName);
            
            // Rotacionar texto se necessário
            if (textWidth > barWidth) {
                AffineTransform original = g2.getTransform();
                g2.rotate(-Math.PI / 4, x + barWidth / 2, padding + chartHeight + 20);
                g2.drawString(algoName, x + barWidth / 2 - textWidth / 2, padding + chartHeight + 20);
                g2.setTransform(original);
            } else {
                g2.drawString(algoName, x + (barWidth - textWidth) / 2, padding + chartHeight + 15);
            }
            
            index++;
        }
        
        // Eixos e labels
        drawAxes(g2, padding, chartWidth, chartHeight, minValue, maxValue);
    }
    
    private Color getPerformanceColor(double value, double min, double max) {
        // Verde para bons valores (baixos), vermelho para ruins (altos)
        double ratio = (value - min) / (max - min);
        
        int r = (int) (255 * ratio);
        int g = (int) (255 * (1 - ratio));
        int b = 0;
        
        return new Color(Math.min(r, 255), Math.min(g, 255), b);
    }
    
    private void drawAxes(Graphics2D g2, int padding, int chartWidth, int chartHeight, double min, double max) {
        g2.setColor(TEXT_COLOR);
        g2.setStroke(new BasicStroke(2));
        
        // Eixos
        g2.drawLine(padding, padding, padding, padding + chartHeight); // Y
        g2.drawLine(padding, padding + chartHeight, padding + chartWidth, padding + chartHeight); // X
        
        // Labels do eixo Y
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        int yLabels = 5;
        for (int i = 0; i <= yLabels; i++) {
            double value = min + (max - min) * i / yLabels;
            int y = padding + chartHeight - (i * chartHeight) / yLabels;
            
            String label = String.format("%.2f", value);
            FontMetrics fm = g2.getFontMetrics();
            int labelWidth = fm.stringWidth(label);
            
            g2.drawString(label, padding - labelWidth - 5, y + 4);
            g2.drawLine(padding - 5, y, padding, y);
        }
        
        // Título do eixo Y
        AffineTransform original = g2.getTransform();
        g2.rotate(-Math.PI / 2, padding - 40, padding + chartHeight / 2);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        String yTitle = currentMetric + " (seconds)";
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(yTitle, padding - 40 - fm.stringWidth(yTitle) / 2, padding + chartHeight / 2);
        g2.setTransform(original);
        
        // Título do gráfico
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        String title = "Algorithm Performance - " + currentMetric;
        fm = g2.getFontMetrics();
        g2.drawString(title, (getWidth() - fm.stringWidth(title)) / 2, padding - 20);
    }
    
    private void drawTrendLine(Graphics2D g2) {
        if (historicalData.size() < 2) return;
        
        int padding = 80;
        int chartWidth = getWidth() - 2 * padding;
        int chartHeight = getHeight() - 2 * padding;
        
        // Encontrar escala para dados históricos
        double maxHist = historicalData.stream().max(Double::compare).orElse(1.0);
        double minHist = historicalData.stream().min(Double::compare).orElse(0.0);
        double histRange = Math.max(maxHist - minHist, 0.1);
        
        g2.setColor(TREND_COLOR);
        g2.setStroke(new BasicStroke(2, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        
        // Desenhar linha de tendência
        Path2D path = new Path2D.Double();
        for (int i = 0; i < historicalData.size(); i++) {
            double value = historicalData.get(i);
            double normalized = (value - minHist) / histRange;
            
            int x = padding + (i * chartWidth) / Math.max(historicalData.size() - 1, 1);
            int y = padding + chartHeight - (int) (normalized * chartHeight);
            
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        g2.draw(path);
        
        // Pontos na linha de tendência
        g2.setColor(new Color(255, 100, 100));
        for (int i = 0; i < historicalData.size(); i++) {
            double value = historicalData.get(i);
            double normalized = (value - minHist) / histRange;
            
            int x = padding + (i * chartWidth) / Math.max(historicalData.size() - 1, 1);
            int y = padding + chartHeight - (int) (normalized * chartHeight);
            
            g2.fillOval(x - 3, y - 3, 6, 6);
        }
    }
    
    private void drawLegend(Graphics2D g2) {
        int legendX = getWidth() - 150;
        int legendY = 100;
        
        g2.setColor(new Color(40, 40, 50, 200));
        g2.fillRoundRect(legendX - 10, legendY - 20, 140, 100, 10, 10);
        
        g2.setColor(TEXT_COLOR);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2.drawString("Performance Scale", legendX, legendY);
        
        // Gradiente da legenda
        int gradientHeight = 20;
        for (int i = 0; i < 100; i++) {
            double ratio = i / 100.0;
            Color color = getPerformanceColor(ratio, 0, 1);
            g2.setColor(color);
            g2.drawLine(legendX + i, legendY + 20, legendX + i, legendY + 20 + gradientHeight);
        }
        
        g2.setColor(TEXT_COLOR);
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        g2.drawString("Fast", legendX - 5, legendY + 35);
        g2.drawString("Slow", legendX + 95, legendY + 35);
        
        // Informações estatísticas
        if (!algorithmPerformance.isEmpty()) {
            double avg = algorithmPerformance.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            double stdDev = calculateStdDev();
            
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            g2.drawString(String.format("Avg: %.4f", avg), legendX, legendY + 60);
            g2.drawString(String.format("StdDev: %.4f", stdDev), legendX, legendY + 75);
        }
    }
    
    private double calculateStdDev() {
        double avg = algorithmPerformance.values().stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        double variance = algorithmPerformance.values().stream()
            .mapToDouble(v -> Math.pow(v - avg, 2))
            .average().orElse(0.0);
        return Math.sqrt(variance);
    }
    
    public void exportChart(String filename) {
    }
}
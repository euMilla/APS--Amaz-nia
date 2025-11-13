import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class SatelliteMap extends JPanel {
    private List<SatelliteDataPoint> dataPoints;
    private Map<String, Region> regions;
    private boolean showHeatMap;
    private boolean showDataPoints;
    private String currentView;
    
    public SatelliteMap() {
        this.dataPoints = new ArrayList<>();
        this.regions = new HashMap<>();
        this.showHeatMap = true;
        this.showDataPoints = true;
        this.currentView = "Satellite";
        initializeRegions();
        generateSampleData();
        setBackground(new Color(10, 20, 30));
    }
    
    private void initializeRegions() {
        // Regiões da Amazônia com coordenadas normalizadas
        regions.put("Amazonas", new Region(0.3, 0.4, 0.4, 0.6, new Color(34, 153, 84)));
        regions.put("Pará", new Region(0.5, 0.3, 0.3, 0.5, new Color(40, 180, 99)));
        regions.put("Mato Grosso", new Region(0.4, 0.6, 0.3, 0.4, new Color(46, 204, 113)));
        regions.put("Rondônia", new Region(0.2, 0.5, 0.2, 0.3, new Color(52, 152, 219)));
        regions.put("Acre", new Region(0.1, 0.4, 0.1, 0.2, new Color(155, 89, 182)));
    }
    
    private void generateSampleData() {
        Random random = new Random();
        
        // Gerar pontos de dados de satélite realistas
        for (int i = 0; i < 500; i++) {
            String region = getRandomRegion();
            Region reg = regions.get(region);
            
            double x = reg.x + random.nextDouble() * reg.width;
            double y = reg.y + random.nextDouble() * reg.height;
            double value = random.nextDouble(); // Valor normalizado
            
            dataPoints.add(new SatelliteDataPoint(x, y, value, region));
        }
    }
    
    private String getRandomRegion() {
        String[] regionNames = regions.keySet().toArray(new String[0]);
        return regionNames[new Random().nextInt(regionNames.length)];
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        drawMapBackground(g2);
        drawRegions(g2);
        
        if (showHeatMap) {
            drawHeatMap(g2);
        }
        
        if (showDataPoints) {
            drawDataPoints(g2);
        }
        
        drawMapOverlay(g2);
        drawLegend(g2);
    }
    
    private void drawMapBackground(Graphics2D g2) {
        // Fundo do mapa com gradiente
        GradientPaint gradient = new GradientPaint(
            0, 0, new Color(15, 30, 45),
            getWidth(), getHeight(), new Color(25, 50, 75)
        );
        g2.setPaint(gradient);
        g2.fillRect(0, 0, getWidth(), getHeight());
        
        // Grade de coordenadas
        g2.setColor(new Color(40, 80, 120, 50));
        g2.setStroke(new BasicStroke(1));
        
        int gridSize = 50;
        for (int x = 0; x < getWidth(); x += gridSize) {
            g2.drawLine(x, 0, x, getHeight());
        }
        for (int y = 0; y < getHeight(); y += gridSize) {
            g2.drawLine(0, y, getWidth(), y);
        }
    }
    
    private void drawRegions(Graphics2D g2) {
        for (Map.Entry<String, Region> entry : regions.entrySet()) {
            Region region = entry.getValue();
            
            int x = (int) (region.x * getWidth());
            int y = (int) (region.y * getHeight());
            int width = (int) (region.width * getWidth());
            int height = (int) (region.height * getHeight());
            
            // Preenchimento da região
            GradientPaint gradient = new GradientPaint(
                x, y, region.color.brighter(),
                x + width, y + height, region.color.darker()
            );
            g2.setPaint(gradient);
            g2.fillRoundRect(x, y, width, height, 20, 20);
            
            // Borda da região
            g2.setColor(region.color.darker().darker());
            g2.setStroke(new BasicStroke(2));
            g2.drawRoundRect(x, y, width, height, 20, 20);
            
            // Nome da região
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(entry.getKey());
            g2.drawString(entry.getKey(), x + (width - textWidth) / 2, y + height / 2);
        }
    }
    
    private void drawHeatMap(Graphics2D g2) {
        // Criar um mapa de calor baseado na densidade de pontos
        int heatMapResolution = 50;
        double[][] heatMap = new double[heatMapResolution][heatMapResolution];
        
        // Calcular densidade
        for (SatelliteDataPoint point : dataPoints) {
            int gridX = (int) (point.x * heatMapResolution);
            int gridY = (int) (point.y * heatMapResolution);
            if (gridX >= 0 && gridX < heatMapResolution && gridY >= 0 && gridY < heatMapResolution) {
                heatMap[gridX][gridY] += point.value;
            }
        }
        
        // Normalizar valores
        double maxDensity = 0;
        for (int i = 0; i < heatMapResolution; i++) {
            for (int j = 0; j < heatMapResolution; j++) {
                maxDensity = Math.max(maxDensity, heatMap[i][j]);
            }
        }
        
        if (maxDensity > 0) {
            // Desenhar mapa de calor
            int cellWidth = getWidth() / heatMapResolution;
            int cellHeight = getHeight() / heatMapResolution;
            
            for (int i = 0; i < heatMapResolution; i++) {
                for (int j = 0; j < heatMapResolution; j++) {
                    double density = heatMap[i][j] / maxDensity;
                    if (density > 0) {
                        Color heatColor = getHeatColor(density);
                        g2.setColor(new Color(heatColor.getRed(), heatColor.getGreen(), 
                                            heatColor.getBlue(), (int) (density * 150)));
                        g2.fillRect(i * cellWidth, j * cellHeight, cellWidth, cellHeight);
                    }
                }
            }
        }
    }
    
    private void drawDataPoints(Graphics2D g2) {
        for (SatelliteDataPoint point : dataPoints) {
            int x = (int) (point.x * getWidth());
            int y = (int) (point.y * getHeight());
            
            // Tamanho baseado no valor
            int size = 3 + (int) (point.value * 7);
            
            // Cor baseada no valor e região
            Color pointColor = getDataPointColor(point);
            
            // Ponto com gradiente
            RadialGradientPaint gradient = new RadialGradientPaint(
                x, y, size,
                new float[] {0.0f, 1.0f},
                new Color[] {pointColor.brighter(), pointColor.darker()}
            );
            g2.setPaint(gradient);
            g2.fillOval(x - size/2, y - size/2, size, size);
            
            // Borda do ponto
            g2.setColor(pointColor.darker());
            g2.setStroke(new BasicStroke(1));
            g2.drawOval(x - size/2, y - size/2, size, size);
        }
    }
    
    private Color getHeatColor(double intensity) {
        // Gradiente: azul (baixo) -> verde -> amarelo -> vermelho (alto)
        if (intensity < 0.25) {
            return new Color(0, 0, (int) (255 * intensity * 4));
        } else if (intensity < 0.5) {
            return new Color(0, (int) (255 * (intensity - 0.25) * 4), 255);
        } else if (intensity < 0.75) {
            return new Color((int) (255 * (intensity - 0.5) * 4), 255, 0);
        } else {
            return new Color(255, (int) (255 * (1 - intensity) * 4), 0);
        }
    }
    
    private Color getDataPointColor(SatelliteDataPoint point) {
        // Cor baseada na região
        switch (point.region) {
            case "Amazonas": return new Color(231, 76, 60);   // Vermelho
            case "Pará": return new Color(46, 204, 113);      // Verde
            case "Mato Grosso": return new Color(52, 152, 219); // Azul
            case "Rondônia": return new Color(155, 89, 182);  // Roxo
            case "Acre": return new Color(241, 196, 15);      // Amarelo
            default: return Color.WHITE;
        }
    }
    
    private void drawMapOverlay(Graphics2D g2) {
        // Informações do mapa
        g2.setColor(new Color(255, 255, 255, 180));
        g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
        g2.drawString("🛰️ Amazon Satellite Data", 20, 30);
        
        g2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2.drawString("View: " + currentView, 20, 50);
        g2.drawString("Data Points: " + dataPoints.size(), 20, 70);
        g2.drawString("Regions: " + regions.size(), 20, 90);
        
        // Escala
        g2.drawString("Scale: 1:1,000,000", getWidth() - 150, getHeight() - 20);
    }
    
    private void drawLegend(Graphics2D g2) {
        int legendX = getWidth() - 200;
        int legendY = 120;
        int legendWidth = 180;
        int legendHeight = 200;
        
        // Fundo da legenda
        g2.setColor(new Color(30, 30, 40, 220));
        g2.fillRoundRect(legendX, legendY, legendWidth, legendHeight, 15, 15);
        g2.setColor(new Color(100, 100, 120));
        g2.setStroke(new BasicStroke(2));
        g2.drawRoundRect(legendX, legendY, legendWidth, legendHeight, 15, 15);
        
        // Título
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
        g2.drawString("Region Legend", legendX + 10, legendY + 25);
        
        // Itens da legenda
        int itemY = legendY + 50;
        for (Map.Entry<String, Region> entry : regions.entrySet()) {
            g2.setColor(entry.getValue().color);
            g2.fillRect(legendX + 15, itemY - 8, 12, 12);
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            g2.drawString(entry.getKey(), legendX + 35, itemY);
            
            itemY += 20;
        }
        
        // Legenda do mapa de calor
        if (showHeatMap) {
            g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
            g2.drawString("Heat Map Intensity", legendX + 10, itemY + 10);
            
            int gradientWidth = 100;
            int gradientHeight = 15;
            for (int i = 0; i < gradientWidth; i++) {
                double ratio = i / (double) gradientWidth;
                Color color = getHeatColor(ratio);
                g2.setColor(color);
                g2.drawLine(legendX + 40 + i, itemY + 25, legendX + 40 + i, itemY + 25 + gradientHeight);
            }
            
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 9));
            g2.drawString("Low", legendX + 35, itemY + 37);
            g2.drawString("High", legendX + 145, itemY + 37);
        }
    }
    
    public void setShowHeatMap(boolean show) {
        this.showHeatMap = show;
        repaint();
    }
    
    public void setShowDataPoints(boolean show) {
        this.showDataPoints = show;
        repaint();
    }
    
    public void setCurrentView(String view) {
        this.currentView = view;
        repaint();
    }
    
    public void addDataPoint(SatelliteDataPoint point) {
        dataPoints.add(point);
        repaint();
    }
    
    public void clearData() {
        dataPoints.clear();
        repaint();
    }
    
    // Classes auxiliares
    static class Region {
        double x, y, width, height;
        Color color;
        
        Region(double x, double y, double width, double height, Color color) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.color = color;
        }
    }
    
    static class SatelliteDataPoint {
        double x, y, value;
        String region;
        
        SatelliteDataPoint(double x, double y, double value, String region) {
            this.x = x;
            this.y = y;
            this.value = value;
            this.region = region;
        }
    }
}
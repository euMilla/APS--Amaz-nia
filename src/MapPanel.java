import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class MapPanel extends JPanel {
    private MainController controller;
    private List<SatelliteImage> images;
    // Limites geográficos do mapa (região da Amazônia Legal)
    private double minLat = -12.0, maxLat = 6.0;
    private double minLon = -74.0, maxLon = -44.0;
    private SatelliteImage selectedImage;
    private javax.swing.Timer animationTimer;
    private float pulseAnimation = 0f;
    private Point mousePosition = null;
    
    // Cores do tema dark
    private final Color BACKGROUND = new Color(18, 18, 24);
    private final Color CARD_BG = new Color(30, 30, 40);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 190);
    private final Color ACCENT = new Color(0, 150, 255);
    private final Color SUCCESS = new Color(0, 200, 150);
    
    // Cores para os estados da Amazônia Legal
    private final Color AMAZONAS = new Color(34, 153, 84, 180);
    private final Color PARA = new Color(46, 204, 113, 180);
    private final Color MATO_GROSSO = new Color(52, 152, 219, 180);
    private final Color RONDONIA = new Color(155, 89, 182, 180);
    private final Color ACRE = new Color(241, 196, 15, 180);
    private final Color RORAIMA = new Color(230, 126, 34, 180);
    private final Color AMAPA = new Color(231, 76, 60, 180);
    private final Color TOCANTINS = new Color(149, 165, 166, 180);
    private final Color MARANHAO = new Color(52, 73, 94, 180);
    
    /**
     * DADOS CORRIGIDOS:
     * Coordenadas Lat/Lon esquemáticas dos estados para garantir a escalabilidade.
     * Estrutura: {minLon, maxLon, minLat, maxLat}
     */
    private final double[][] stateLatLonBounds = {
        {-73.5, -55.0, -8.5, 5.5},   // Amazonas (AM)
        {-60.0, -45.0, -8.0, 4.5},   // Pará (PA)
        {-60.0, -50.0, -12.0, -7.0}, // Mato Grosso (MT)
        {-65.0, -60.0, -12.0, -8.0}, // Rondônia (RO)
        {-73.0, -68.0, -11.0, -7.0}, // Acre (AC)
        {-63.0, -58.0, 0.0, 5.5},    // Roraima (RR)
        {-52.0, -48.0, 0.0, 4.5},    // Amapá (AP)
        {-50.0, -45.0, -12.0, -5.0}, // Tocantins (TO)
        {-48.0, -44.0, -10.0, -1.0}  // Maranhão (MA)
    };
    
    /**
     * DADOS CORRIGIDOS:
     * Coordenadas Lat/Lon reais das capitais da Amazônia Legal.
     * Estrutura: {longitude, latitude, nome}
     */
    private final Object[][] cityLatLon = {
        {-60.02, -3.11, "Manaus"},
        {-48.49, -1.45, "Belém"},
        {-63.90, -8.76, "Porto Velho"},
        {-56.09, -15.60, "Cuiabá"},
        {-67.81, -9.97, "Rio Branco"},
        {-60.67, 2.81, "Boa Vista"},
        {-51.07, 0.03, "Macapá"},
        {-48.33, -10.27, "Palmas"},
        {-44.30, -2.53, "São Luís"}
    };

    public MapPanel(MainController controller) {
        this.controller = controller;
        this.images = controller.getSatelliteImages();
        initializePanel();
        setupAnimations();
    }
    
    /**
     * MÉTODOS DE PROJEÇÃO DE COORDENADAS CORRIGIDOS
     * Convertem Latitude/Longitude em coordenadas de tela (pixels)
     */
    private int getMapX(double lon) {
        int padding = 50;
        int mapWidth = getWidth() - 2 * padding;
        // Normaliza a longitude para o intervalo [0, 1]
        double normalizedLon = (lon - minLon) / (maxLon - minLon);
        // Converte para coordenada X da tela
        return padding + (int)(normalizedLon * mapWidth);
    }

    private int getMapY(double lat) {
        int padding = 50;
        int mapHeight = getHeight() - 2 * padding;
        // Normaliza a latitude para o intervalo [0, 1] (invertido porque Y cresce para baixo)
        double normalizedLat = (lat - minLat) / (maxLat - minLat);
        // Inverte a coordenada Y porque na tela Y=0 é no topo
        return padding + (int)((1 - normalizedLat) * mapHeight);
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1000, 700)); 
        setBackground(BACKGROUND);
        
        // Criar borda com título
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(ACCENT, 2, true),
            BorderFactory.createTitledBorder(
                "🗺️  MAPA DA AMAZÔNIA LEGAL - IMAGENS DE SATÉLITE"
            )
        ));
        
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMapClick(e.getX(), e.getY());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                mousePosition = null;
                repaint();
            }
        });
        
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mousePosition = e.getPoint();
                repaint();
            }
        });
        
        JPanel infoPanel = createInfoPanel();
        add(infoPanel, BorderLayout.SOUTH);
    }
    
    private void setupAnimations() {
        animationTimer = new javax.swing.Timer(50, e -> {
            pulseAnimation += 0.1f;
            if (pulseAnimation >= 2 * Math.PI) {
                pulseAnimation = 0;
            }
            repaint();
        });
        animationTimer.start();
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        JLabel infoLabel = new JLabel("💡 Clique nos pontos coloridos para ver detalhes das imagens de satélite");
        infoLabel.setForeground(TEXT_PRIMARY);
        infoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JLabel statsLabel = new JLabel("📊 " + String.format("%,d", images.size()) + " imagens carregadas");
        statsLabel.setForeground(SUCCESS);
        statsLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        panel.add(infoLabel, BorderLayout.WEST);
        panel.add(statsLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        if (getWidth() <= 0 || getHeight() <= 0) return;
        
        // Desenhar fundo escuro
        drawDarkBackground(g2d);
        
        // Desenhar mapa estilizado
        drawStyledMap(g2d);
        
        // Desenhar estados da Amazônia Legal
        drawAmazonStates(g2d);
        
        // Desenhar cidades
        drawCities(g2d);
        
        // Desenhar pontos das imagens de satélite
        drawSatelliteImagePoints(g2d);
        
        // Efeito de realce no mouse
        if (mousePosition != null) {
            drawMouseHighlight(g2d);
        }
        
        // Legenda
        drawLegend(g2d);
        
        // Imagem selecionada
        if (selectedImage != null) {
            drawSelectedImageHighlight(g2d);
        }
        
        // Título
        drawTitle(g2d);
    }
    
    private void drawDarkBackground(Graphics2D g2d) {
        // Fundo com gradiente escuro
        GradientPaint backgroundGradient = new GradientPaint(
            0, 0, new Color(15, 15, 20),
            getWidth(), getHeight(), new Color(25, 25, 35)
        );
        g2d.setPaint(backgroundGradient);
        g2d.fillRect(0, 0, getWidth(), getHeight());
        
        // Grade sutil
        g2d.setColor(new Color(40, 40, 50));
        g2d.setStroke(new BasicStroke(1));
        
        int gridSize = 50;
        for (int x = gridSize; x < getWidth(); x += gridSize) {
            g2d.drawLine(x, 0, x, getHeight());
        }
        for (int y = gridSize; y < getHeight(); y += gridSize) {
            g2d.drawLine(0, y, getWidth(), y);
        }
    }
    
    private void drawStyledMap(Graphics2D g2d) {
        int padding = 50;
        int mapWidth = getWidth() - 2 * padding;
        int mapHeight = getHeight() - 2 * padding;
        
        // Área do mapa
        g2d.setColor(new Color(40, 40, 50));
        g2d.fillRoundRect(padding, padding, mapWidth, mapHeight, 20, 20);
        
        // Borda do mapa
        g2d.setColor(ACCENT);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRoundRect(padding, padding, mapWidth, mapHeight, 20, 20);
    }
    
    private void drawAmazonStates(Graphics2D g2d) {
        Color[] stateColors = {AMAZONAS, PARA, MATO_GROSSO, RONDONIA, ACRE, RORAIMA, AMAPA, TOCANTINS, MARANHAO};
        String[] stateNames = {"AMAZONAS", "PARÁ", "MATO GROSSO", "RONDÔNIA", "ACRE", "RORAIMA", "AMAPÁ", "TOCANTINS", "MARANHÃO"};
        
        for (int i = 0; i < stateLatLonBounds.length; i++) {
            double[] bounds = stateLatLonBounds[i];
            drawState(g2d, bounds[0], bounds[1], bounds[2], bounds[3], stateColors[i], stateNames[i]);
        }
    }
    
    private void drawState(Graphics2D g2d, double minLon, double maxLon, double minLat, double maxLat, Color color, String name) {
        // Converte coordenadas geográficas para tela
        int x1 = getMapX(minLon);
        int y1 = getMapY(maxLat);
        int x2 = getMapX(maxLon);
        int y2 = getMapY(minLat);
        
        int width = x2 - x1;
        int height = y2 - y1;
        
        // Evitar desenhar estados absurdamente pequenos ou invertidos
        if (width <= 0 || height <= 0) return;
        
        // Preenchimento do estado com gradiente
        GradientPaint stateGradient = new GradientPaint(
            x1, y1, color.brighter(),
            x1 + width, y1 + height, color.darker()
        );
        g2d.setPaint(stateGradient);
        
        // Forma irregular do estado (polígono com bordas arredondadas)
        int curve = 12;
        g2d.fillRoundRect(x1, y1, width, height, curve, curve);
        
        // Borda do estado
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(x1, y1, width, height, curve, curve);
        
        // Nome do estado
        g2d.setColor(TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        
        // Centralizar o nome no estado
        int textX = x1 + (width - textWidth) / 2;
        int textY = y1 + height / 2;
        
        // Fundo para o texto para melhor legibilidade
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(textX - 8, textY - 15, textWidth + 16, 24, 8, 8);
        
        g2d.setColor(TEXT_PRIMARY);
        g2d.drawString(name, textX, textY + 4);
    }
    
    private void drawCities(Graphics2D g2d) {
        for (Object[] city : cityLatLon) {
            double lon = (Double) city[0];
            double lat = (Double) city[1];
            String name = (String) city[2];
            
            // Converte Lat/Lon para coordenadas de tela
            int x = getMapX(lon);
            int y = getMapY(lat);
            
            drawCity(g2d, x, y, name, new Color(255, 255, 100));
        }
    }
    
    private void drawCity(Graphics2D g2d, int x, int y, String name, Color color) {
        // Ponto da cidade (amarelo para destacar)
        g2d.setColor(color);
        g2d.fillOval(x - 5, y - 5, 10, 10);
        
        // Brilho interno
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x - 2, y - 2, 4, 4);
        
        // Borda
        g2d.setColor(color.darker());
        g2d.setStroke(new BasicStroke(1.5f));
        g2d.drawOval(x - 5, y - 5, 10, 10);
        
        // Nome da cidade
        g2d.setColor(TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(name);
        
        // Fundo para o texto da cidade
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(x - textWidth/2 - 5, y - 22, textWidth + 10, 16, 6, 6);
        
        g2d.setColor(TEXT_PRIMARY);
        g2d.drawString(name, x - textWidth / 2, y - 10);
    }
    
    private void drawSatelliteImagePoints(Graphics2D g2d) {
        if (images == null || images.isEmpty()) return;
        
        // Mostrar apenas uma amostra das imagens para não poluir o mapa
        int displayedImages = Math.min(images.size(), 80);
        
        Random random = new Random(42); // Seed fixa para consistência
        
        for (int i = 0; i < displayedImages; i++) {
            SatelliteImage image = images.get(i % images.size()); 
            
            // Usa as funções de projeção corrigidas
            int x = getMapX(image.getLongitude());
            int y = getMapY(image.getLatitude());
            
            int padding = 50;
            if (x >= padding && x < getWidth() - padding && 
                y >= padding && y < getHeight() - padding) {
                drawImagePoint(g2d, x, y, image);
            }
        }
    }
    
    private void drawImagePoint(Graphics2D g2d, int x, int y, SatelliteImage image) {
        Color pointColor = getQualityColor(image.getQuality());
        int size = getPointSize(image.getQuality());
        
        // Ponto principal com gradiente
        GradientPaint pointGradient = new GradientPaint(
            x - size, y - size, pointColor.brighter(),
            x + size, y + size, pointColor.darker()
        );
        g2d.setPaint(pointGradient);
        g2d.fillOval(x - size, y - size, size * 2, size * 2);
        
        // Borda sutil
        g2d.setColor(pointColor.darker());
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(x - size, y - size, size * 2, size * 2);
        
        // Brilho interno
        g2d.setColor(new Color(255, 255, 255, 150));
        g2d.fillOval(x - 1, y - 1, 2, 2);
        
        // Tooltip no hover
        if (mousePosition != null) {
            int proximity = size + 10; 
            if (Math.abs(mousePosition.x - x) < proximity && 
                Math.abs(mousePosition.y - y) < proximity) {
                drawTooltip(g2d, x, y, image);
            }
        }
    }
    
    private void drawTooltip(Graphics2D g2d, int x, int y, SatelliteImage image) {
        int tooltipWidth = 220;
        int tooltipHeight = 100;
        int tooltipX = x + 20;
        int tooltipY = y - tooltipHeight / 2;
        
        // Ajustar posição se necessário para não sair da tela
        if (tooltipX + tooltipWidth > getWidth()) {
            tooltipX = x - tooltipWidth - 20;
        }
        if (tooltipY + tooltipHeight > getHeight()) {
            tooltipY = getHeight() - tooltipHeight - 10;
        }
        if (tooltipY < 10) {
            tooltipY = 10;
        }
        
        // Fundo do tooltip
        g2d.setColor(new Color(50, 50, 60, 240));
        g2d.fillRoundRect(tooltipX, tooltipY, tooltipWidth, tooltipHeight, 12, 12);
        
        // Borda
        g2d.setColor(ACCENT);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(tooltipX, tooltipY, tooltipWidth, tooltipHeight, 12, 12);
        
        // Conteúdo
        g2d.setColor(TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 12));
        g2d.drawString("🛰️ " + image.getImageId(), tooltipX + 10, tooltipY + 22);
        
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        g2d.drawString("📍 " + image.getRegion(), tooltipX + 10, tooltipY + 38);
        g2d.drawString("⭐ Qualidade: " + image.getQuality(), tooltipX + 10, tooltipY + 54);
        g2d.drawString("📅 " + image.getFormattedDate(), tooltipX + 10, tooltipY + 70);
        g2d.drawString("💾 " + image.getSizeKB() + " KB", tooltipX + 10, tooltipY + 86);
    }
    
    private void drawMouseHighlight(Graphics2D g2d) {
        if (mousePosition == null) return;
        
        // Círculo de destaque sutil
        g2d.setColor(new Color(255, 255, 255, 30));
        g2d.fillOval(mousePosition.x - 20, mousePosition.y - 20, 40, 40);
        
        // Ponto central
        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(mousePosition.x - 2, mousePosition.y - 2, 4, 4);
    }
    
    private void drawLegend(Graphics2D g2d) {
        int legendX = getWidth() - 230;
        int legendY = 80;
        int legendWidth = 210;
        int legendHeight = 160;
        
        // Fundo da legenda
        g2d.setColor(new Color(40, 40, 50, 240));
        g2d.fillRoundRect(legendX, legendY, legendWidth, legendHeight, 15, 15);
        
        // Borda
        g2d.setColor(ACCENT);
        g2d.setStroke(new BasicStroke(2));
        g2d.drawRoundRect(legendX, legendY, legendWidth, legendHeight, 15, 15);
        
        // Título
        g2d.setColor(TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 13));
        g2d.drawString("🗺️  LEGENDA DO MAPA", legendX + 15, legendY + 22);
        
        // Linha separadora
        g2d.setColor(new Color(100, 100, 120));
        g2d.drawLine(legendX + 15, legendY + 30, legendX + legendWidth - 15, legendY + 30);
        
        // Itens da legenda - organizados em duas colunas
        int startY = legendY + 50;
        int column1X = legendX + 20;
        int column2X = legendX + 120;
        
        // Coluna 1
        drawLegendItem(g2d, column1X, startY, new Color(220, 20, 60), "Alta", "Imagens Detalhadas");
        drawLegendItem(g2d, column1X, startY + 30, new Color(255, 140, 0), "Média", "Qualidade Padrão");
        drawLegendItem(g2d, column1X, startY + 60, new Color(30, 144, 255), "Baixa", "Visão Geral");
        
        // Coluna 2
        drawLegendItem(g2d, column2X, startY, new Color(255, 255, 100), "Cidades", "Centros Urbanos");
        drawLegendItem(g2d, column2X, startY + 30, new Color(180, 180, 180), "Estados", "Amazônia Legal");
        
        // Informação adicional
        g2d.setColor(TEXT_SECONDARY);
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        g2d.drawString("💡 Pontos representam imagens de satélite", legendX + 15, legendY + legendHeight - 10);
    }
    
    private void drawLegendItem(Graphics2D g2d, int x, int y, Color color, String title, String desc) {
        // Ícone
        g2d.setColor(color);
        
        if ("Cidades".equals(title)) {
            g2d.fillOval(x, y - 5, 8, 8);
            g2d.setColor(color.darker());
            g2d.drawOval(x, y - 5, 8, 8);
        } else if ("Estados".equals(title)) {
            g2d.fillRect(x, y - 6, 10, 8);
            g2d.setColor(color.darker());
            g2d.drawRect(x, y - 6, 10, 8);
        } else {
            g2d.fillOval(x, y - 4, 6, 6);
            g2d.setColor(color.darker());
            g2d.drawOval(x, y - 4, 6, 6);
        }
        
        // Texto
        g2d.setColor(TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 10));
        g2d.drawString(title, x + 15, y);
        
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 9));
        g2d.setColor(TEXT_SECONDARY);
        g2d.drawString(desc, x + 15, y + 12);
    }
    
    private void drawSelectedImageHighlight(Graphics2D g2d) {
        if (selectedImage == null) return;
        
        // Usa as funções de projeção corrigidas
        int x = getMapX(selectedImage.getLongitude());
        int y = getMapY(selectedImage.getLatitude());
        
        int padding = 50;
        if (x < padding || x >= getWidth() - padding || y < padding || y >= getHeight() - padding) return;
        
        // Animações de seleção
        for (int i = 0; i < 3; i++) {
            float size = 15 + i * 10 + (float)Math.sin(pulseAnimation + i) * 5;
            float alpha = 100 - i * 25 - (float)Math.abs(Math.sin(pulseAnimation)) * 20;
            
            g2d.setColor(new Color(255, 255, 0, Math.max(0, (int)alpha)));
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawOval((int)(x - size/2), (int)(y - size/2), (int)size, (int)size);
        }
        
        // Ponto central destacado
        g2d.setColor(Color.YELLOW);
        g2d.fillOval(x - 4, y - 4, 8, 8);
        g2d.setColor(Color.ORANGE);
        g2d.drawOval(x - 4, y - 4, 8, 8);
    }
    
    private void drawTitle(Graphics2D g2d) {
        String title = "AMAZÔNIA LEGAL - MONITORAMENTO POR SATÉLITE";
        String subtitle = "Imagens de Satélite por Qualidade e Localização";
        
        g2d.setColor(TEXT_PRIMARY);
        g2d.setFont(new Font("Segoe UI", Font.BOLD, 18));
        FontMetrics fm = g2d.getFontMetrics();
        int titleWidth = fm.stringWidth(title);
        g2d.drawString(title, (getWidth() - titleWidth) / 2, 35);
        
        g2d.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        g2d.setColor(TEXT_SECONDARY);
        fm = g2d.getFontMetrics();
        int subtitleWidth = fm.stringWidth(subtitle);
        g2d.drawString(subtitle, (getWidth() - subtitleWidth) / 2, 52);
    }
    
    private Color getQualityColor(String quality) {
        switch (quality) {
            case "Alta": return new Color(220, 20, 60);    // Vermelho
            case "Média": return new Color(255, 140, 0);   // Laranja
            case "Baixa": return new Color(30, 144, 255);  // Azul
            default: return new Color(150, 150, 150);      // Cinza
        }
    }
    
    private int getPointSize(String quality) {
        switch (quality) {
            case "Alta": return 5;
            case "Média": return 4;
            case "Baixa": return 3;
            default: return 3;
        }
    }
    
    private void handleMapClick(int x, int y) {
        int padding = 50;
        int mapWidth = getWidth() - 2 * padding;
        int mapHeight = getHeight() - 2 * padding;
        
        if (mapWidth <= 0 || mapHeight <= 0) return;

        // Converte coordenadas de clique para coordenadas geográficas
        double clickedLon = minLon + ((x - padding) * (maxLon - minLon) / mapWidth);
        double clickedLat = maxLat - ((y - padding) * (maxLat - minLat) / mapHeight);
        
        SatelliteImage closest = findClosestImage(clickedLon, clickedLat);
        
        if (closest != null) {
            selectedImage = closest;
            showImageDetails(closest);
            repaint();
        } else {
            selectedImage = null;
            repaint();
        }
    }
    
    private SatelliteImage findClosestImage(double longitude, double latitude) {
        SatelliteImage closest = null;
        double minDistance = Double.MAX_VALUE;
        double searchRadius = 3.0; // Raio de busca em graus
        
        for (SatelliteImage image : images) {
            double distance = Math.sqrt(
                Math.pow(image.getLongitude() - longitude, 2) + 
                Math.pow(image.getLatitude() - latitude, 2)
            );
            
            if (distance < minDistance && distance < searchRadius) {
                minDistance = distance;
                closest = image;
            }
        }
        
        return closest;
    }
    
    private void showImageDetails(SatelliteImage image) {
        Color qualityColor = getQualityColor(image.getQuality());
        String htmlColor;

        if (qualityColor.equals(new Color(220, 20, 60))) {
            htmlColor = "#DC143C"; 
        } else if (qualityColor.equals(new Color(255, 140, 0))) {
            htmlColor = "#FF8C00";
        } else {
            htmlColor = "#1E90FF";
        }

        String message = String.format(
            "<html><div style='width: 420px; background: #1E1E2E; padding: 20px; border-radius: 10px; color: white; border: 2px solid #0096FF;'>" +
            "<h2 style='margin: 0 0 15px 0; text-align: center; color: #0096FF;'>🛰️ DETALHES DA IMAGEM</h2>" +
            "<div style='background: #2D2D3D; padding: 15px; border-radius: 8px;'>" +
            "<p><b>🆔 ID:</b> <span style='color: #00C8A0;'>%s</span></p>" +
            "<p><b>📍 Coordenadas:</b> %.4f° Lat, %.4f° Long</p>" +
            "<p><b>📅 Data/Hora:</b> %s</p>" +
            "<p><b>⭐ Qualidade:</b> <span style='color: %s; font-weight: bold;'>%s</span></p>" +
            "<p><b>🌎 Região:</b> %s</p>" +
            "<p><b>💾 Tamanho:</b> %d KB</p>" +
            "</div>" +
            "<p style='text-align: center; margin-top: 15px; font-style: italic; color: #888;'>" +
            "Sistema de Monitoramento Ambiental</p>" +
            "</div></html>",
            image.getImageId(), 
            image.getLatitude(), 
            image.getLongitude(),
            image.getFormattedDate(),
            htmlColor,
            image.getQuality(),
            image.getRegion(),
            image.getSizeKB()
        );
        
        JOptionPane.showMessageDialog(this, message, "Detalhes da Imagem", 
                                     JOptionPane.INFORMATION_MESSAGE);
    }
    
    @Override
    public void removeNotify() {
        super.removeNotify();
        if (animationTimer != null) {
            animationTimer.stop();
        }
    }
}
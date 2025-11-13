import java.awt.*;
import javax.swing.*;

public class MainFrame extends JFrame {
    private MainController controller;
    private JTabbedPane tabbedPane;
    
    public MainFrame(MainController controller) {
        this.controller = controller;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("🌍 Sistema de Análise de Performance - Algoritmos de Ordenação");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(1200, 800));
        
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        setupTabbedPane();
        setupMenuBar();
        
        pack();
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(240, 242, 245));
    }
    
    private void setupTabbedPane() {
        tabbedPane = new JTabbedPane();
        tabbedPane.setBackground(new Color(240, 242, 245));
        
        // Criar painel de informações dos algoritmos
        JPanel algorithmsInfoPanel = createAlgorithmsInfoPanel();
        DashboardPanel dashboardPanel = new DashboardPanel(controller);
        SortingPanel sortingPanel = new SortingPanel(controller);
        MapPanel mapPanel = new MapPanel(controller);
        ChartPanel chartPanel = new ChartPanel(controller);
        
        // Ordem das abas: Algoritmos, Dashboard, Testes, Mapa, Gráficos
        tabbedPane.addTab("🔧 Algoritmos", algorithmsInfoPanel);
        tabbedPane.addTab("📊 Dashboard", dashboardPanel);
        tabbedPane.addTab("⚡ Testes", sortingPanel);
        tabbedPane.addTab("🗺️ Mapa", mapPanel);
        tabbedPane.addTab("📈 Gráficos", chartPanel);
        
        add(tabbedPane);
    }
    
    private JPanel createAlgorithmsInfoPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(18, 18, 24));
        
        // Título principal
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(30, 30, 40));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        JLabel titleLabel = new JLabel("🔧 ALGORITMOS DE ORDENAÇÃO", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 150, 255));
        
        JLabel subtitleLabel = new JLabel("Comparação de Desempenho e Características", JLabel.LEFT);
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(180, 180, 190));
        subtitleLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
        
        titlePanel.add(titleLabel, BorderLayout.NORTH);
        titlePanel.add(subtitleLabel, BorderLayout.CENTER);
        
        // Painel de conteúdo com cards
        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setBackground(new Color(18, 18, 24));
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 30, 30));
        
        // Adicionar cards dos algoritmos
        contentPanel.add(createAlgorithmCard("QuickSort", "⚡", 
            "Divisão e Conquista com Pivô", new Color(74, 124, 178)));
        contentPanel.add(Box.createVerticalStrut(20));
        
        contentPanel.add(createAlgorithmCard("MergeSort", "🔄", 
            "Ordenação Estável por Fusão", new Color(86, 166, 86)));
        contentPanel.add(Box.createVerticalStrut(20));
        
        contentPanel.add(createAlgorithmCard("HeapSort", "📊", 
            "Baseado em Estrutura Heap", new Color(219, 139, 11)));
        contentPanel.add(Box.createVerticalStrut(20));
        
        contentPanel.add(createAlgorithmCard("TimSort", "🎯", 
            "Híbrido (Merge + Insertion)", new Color(147, 112, 219)));
        contentPanel.add(Box.createVerticalStrut(30));
        
        // Adicionar seção de comparação
        contentPanel.add(createComparisonSection());
        
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(new Color(18, 18, 24));
        
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        return mainPanel;
    }
    
    private JPanel createAlgorithmCard(String algorithmName, String emoji, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(new Color(30, 30, 40));
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        
        // Header do card
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(30, 30, 40));
        
        JLabel nameLabel = new JLabel(emoji + " " + algorithmName);
        nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        nameLabel.setForeground(color);
        
        JLabel descLabel = new JLabel(description);
        descLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        descLabel.setForeground(new Color(180, 180, 190));
        descLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));
        
        headerPanel.add(nameLabel, BorderLayout.NORTH);
        headerPanel.add(descLabel, BorderLayout.CENTER);
        
        // Detalhes do algoritmo
        JPanel detailsPanel = new JPanel(new GridLayout(2, 2, 15, 10));
        detailsPanel.setBackground(new Color(30, 30, 40));
        
        String[][] details = getAlgorithmDetails(algorithmName);
        
        for (String[] detail : details) {
            JPanel detailItem = new JPanel(new BorderLayout());
            detailItem.setBackground(new Color(40, 40, 50));
            detailItem.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            
            JLabel keyLabel = new JLabel(detail[0]);
            keyLabel.setFont(new Font("Segoe UI", Font.BOLD, 11));
            keyLabel.setForeground(new Color(200, 200, 210));
            
            JLabel valueLabel = new JLabel(detail[1]);
            valueLabel.setFont(new Font("Consolas", Font.PLAIN, 11));
            valueLabel.setForeground(color);
            
            detailItem.add(keyLabel, BorderLayout.NORTH);
            detailItem.add(valueLabel, BorderLayout.CENTER);
            
            detailsPanel.add(detailItem);
        }
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(detailsPanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private String[][] getAlgorithmDetails(String algorithmName) {
        switch (algorithmName) {
            case "QuickSort":
                return new String[][] {
                    {"Complexidade Temporal", "O(n log n) médio\nO(n²) pior caso"},
                    {"Complexidade Espacial", "O(log n)"},
                    {"Estabilidade", "Não Estável"},
                    {"In-Place", "Sim"}
                };
            case "MergeSort":
                return new String[][] {
                    {"Complexidade Temporal", "O(n log n)"},
                    {"Complexidade Espacial", "O(n)"},
                    {"Estabilidade", "Estável"},
                    {"In-Place", "Não"}
                };
            case "HeapSort":
                return new String[][] {
                    {"Complexidade Temporal", "O(n log n)"},
                    {"Complexidade Espacial", "O(1)"},
                    {"Estabilidade", "Não Estável"},
                    {"In-Place", "Sim"}
                };
            case "TimSort":
                return new String[][] {
                    {"Complexidade Temporal", "O(n log n)"},
                    {"Complexidade Espacial", "O(n)"},
                    {"Estabilidade", "Estável"},
                    {"In-Place", "Não"}
                };
            default:
                return new String[0][0];
        }
    }
    
    private JPanel createComparisonSection() {
        JPanel comparisonPanel = new JPanel(new BorderLayout());
        comparisonPanel.setBackground(new Color(30, 30, 40));
        comparisonPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 255), 2),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        
        JLabel titleLabel = new JLabel("🏆 COMPARAÇÃO GERAL", JLabel.LEFT);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(0, 150, 255));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JTextArea comparisonText = new JTextArea();
        comparisonText.setEditable(false);
        comparisonText.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comparisonText.setBackground(new Color(30, 30, 40));
        comparisonText.setForeground(new Color(220, 220, 220));
        comparisonText.setLineWrap(true);
        comparisonText.setWrapStyleWord(true);
        comparisonText.setText(getComparisonText());
        
        comparisonPanel.add(titleLabel, BorderLayout.NORTH);
        comparisonPanel.add(comparisonText, BorderLayout.CENTER);
        
        return comparisonPanel;
    }
    
    private String getComparisonText() {
        return 
            "• ⚡ MAIS RÁPIDO NA PRÁTICA: QuickSort\n" +
            "   - Geralmente o mais rápido para dados aleatórios\n" +
            "   - Excelente desempenho médio\n" +
            "   - Sensível à escolha do pivô\n\n" +
            
            "• 🔄 MAIS CONSISTENTE: MergeSort\n" +
            "   - Performance previsível O(n log n)\n" +
            "   - Estável (mantém ordem relativa)\n" +
            "   - Ideal quando estabilidade é importante\n\n" +
            
            "• 📊 MENOS MEMÓRIA: HeapSort\n" +
            "   - Ordenação in-place (O(1) espaço)\n" +
            "   - Desempenho consistente\n" +
            "   - Ideal para sistemas com memória limitada\n\n" +
            
            "• 🎯 MAIS ADAPTATIVO: TimSort\n" +
            "   - Híbrido otimizado para dados reais\n" +
            "   - Excelente com dados parcialmente ordenados\n" +
            "   - Usado no Arrays.sort() do Java\n\n" +
            
            "📈 ORDEM RECOMENDADA DE DESEMPENHO:\n" +
            "   QuickSort > TimSort > MergeSort > HeapSort\n\n" +
            
            "💡 DICA: Execute testes com seus dados específicos\n" +
            "para determinar o melhor algoritmo para seu caso!";
    }
    
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setBackground(new Color(52, 73, 94));
        
        JMenu fileMenu = new JMenu("Arquivo");
        fileMenu.setForeground(Color.WHITE);
        fileMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JMenuItem exportItem = new JMenuItem("📤 Exportar Resultados");
        exportItem.addActionListener(e -> controller.exportResultsToFile("resultados_analise.csv"));
        
        JMenuItem exitItem = new JMenuItem("🚪 Sair");
        exitItem.addActionListener(e -> System.exit(0));
        
        fileMenu.add(exportItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);
        
        JMenu helpMenu = new JMenu("Ajuda");
        helpMenu.setForeground(Color.WHITE);
        helpMenu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        JMenuItem aboutItem = new JMenuItem("ℹ️ Sobre");
        aboutItem.addActionListener(e -> showAboutDialog());
        
        helpMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        
        setJMenuBar(menuBar);
    }
    
    private void showAboutDialog() {
        String aboutText = 
            "<html><div style='width: 400px; text-align: center;'>" +
            "<h2 style='color: #0096FF; font-family: Segoe UI;'>🌍 Sistema de Análise de Performance</h2>" +
            "<p style='font-family: Segoe UI;'><b>Algoritmos de Ordenação</b></p>" +
            "<p style='font-family: Segoe UI;'>Desenvolvido para análise comparativa no contexto de<br>" +
            "processamento de imagens de satélite da Amazônia</p>" +
            "<hr>" +
            "<p style='font-family: Segoe UI;'><b>Funcionalidades:</b></p>" +
            "<p style='font-family: Segoe UI;'>• Análise de 4 algoritmos de ordenação<br>" +
            "• Dados externos e aleatórios<br>" +
            "• Visualização em mapa interativo<br>" +
            "• Gráficos de performance animados<br>" +
            "• Exportação de resultados</p>" +
            "<p style='font-family: Segoe UI;'><b>Versão 2.2 - Design Premium</b></p>" +
            "</div></html>";
        
        JOptionPane.showMessageDialog(this, aboutText, "Sobre o Sistema", 
                                    JOptionPane.INFORMATION_MESSAGE);
    }
    
    public void updateDashboard() {
        Component[] components = tabbedPane.getComponents();
        for (Component comp : components) {
            if (comp instanceof DashboardPanel) {
                ((DashboardPanel) comp).updateData();
            }
        }
    }
}
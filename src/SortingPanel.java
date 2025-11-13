import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;

public class SortingPanel extends JPanel {
    private MainController controller;
    private JComboBox<String> dataTypeComboBox;
    private JComboBox<String> sortCriteriaComboBox;
    private JSpinner dataSizeSpinner;
    private JTextArea resultsArea;
    private JProgressBar progressBar;
    private Map<String, JCheckBox> algorithmCheckboxes;
    private JLabel statusLabel;
    private JLabel performanceSummaryLabel;
    
    // Cores do tema dark
    private final Color BACKGROUND = new Color(18, 18, 24);
    private final Color CARD_BG = new Color(30, 30, 40);
    private final Color TEXT_PRIMARY = new Color(240, 240, 240);
    private final Color TEXT_SECONDARY = new Color(180, 180, 190);
    private final Color ACCENT = new Color(0, 150, 255);
    private final Color SUCCESS = new Color(0, 200, 150);
    private final Color WARNING = new Color(255, 180, 0);
    private final Color DANGER = new Color(255, 80, 80);
    private final Color PURPLE = new Color(160, 100, 255);
    
    public SortingPanel(MainController controller) {
        this.controller = controller;
        this.algorithmCheckboxes = new HashMap<>();
        initializePanel();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout(15, 15));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(BACKGROUND);
        
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createControlPanel(), BorderLayout.WEST);
        add(createResultsPanel(), BorderLayout.CENTER);
        add(createActionPanel(), BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(BACKGROUND);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel title = new JLabel("🧪 TESTES DE PERFORMANCE");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(ACCENT);
        
        JLabel subtitle = new JLabel("Análise Comparativa de Algoritmos de Ordenação");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(TEXT_SECONDARY);
        
        header.add(title, BorderLayout.WEST);
        header.add(subtitle, BorderLayout.EAST);
        
        return header;
    }
    
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new BoxLayout(controlPanel, BoxLayout.Y_AXIS));
        controlPanel.setBackground(CARD_BG);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 70), 1),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));
        controlPanel.setPreferredSize(new Dimension(350, 0));
        
        // Título da seção
        JLabel sectionTitle = new JLabel("⚙️ CONFIGURAÇÃO DOS TESTES");
        sectionTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        sectionTitle.setForeground(ACCENT);
        sectionTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        controlPanel.add(sectionTitle);
        
        controlPanel.add(Box.createVerticalStrut(20));
        
        // Algoritmos
        controlPanel.add(createAlgorithmSection());
        controlPanel.add(Box.createVerticalStrut(20));
        
        // Tipo de dados
        controlPanel.add(createDataTypeSection());
        controlPanel.add(Box.createVerticalStrut(20));
        
        // Critério
        controlPanel.add(createCriteriaSection());
        controlPanel.add(Box.createVerticalStrut(20));
        
        // Tamanho
        controlPanel.add(createSizeSection());
        
        return controlPanel;
    }
    
    private JPanel createAlgorithmSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel("🔧 Algoritmos:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createVerticalStrut(10));
        
        JPanel checkboxesPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        checkboxesPanel.setBackground(CARD_BG);
        checkboxesPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        // CORREÇÃO: Verificar se o controller não é nulo e tratar possíveis exceções
        try {
            if (controller != null) {
                for (String algoName : controller.getAlgorithmNames()) {
                    JCheckBox checkBox = new JCheckBox(algoName);
                    checkBox.setSelected(true);
                    checkBox.setBackground(CARD_BG);
                    checkBox.setForeground(TEXT_PRIMARY);
                    checkBox.setFocusPainted(false);
                    checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    algorithmCheckboxes.put(algoName, checkBox);
                    checkboxesPanel.add(checkBox);
                }
            } else {
                // Fallback caso o controller seja nulo
                String[] defaultAlgorithms = {"QuickSort", "MergeSort", "HeapSort", "BubbleSort"};
                for (String algoName : defaultAlgorithms) {
                    JCheckBox checkBox = new JCheckBox(algoName);
                    checkBox.setSelected(true);
                    checkBox.setBackground(CARD_BG);
                    checkBox.setForeground(TEXT_PRIMARY);
                    checkBox.setFocusPainted(false);
                    checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                    algorithmCheckboxes.put(algoName, checkBox);
                    checkboxesPanel.add(checkBox);
                }
            }
        } catch (Exception e) {
            // Fallback em caso de erro
            String[] defaultAlgorithms = {"QuickSort", "MergeSort", "HeapSort", "BubbleSort"};
            for (String algoName : defaultAlgorithms) {
                JCheckBox checkBox = new JCheckBox(algoName);
                checkBox.setSelected(true);
                checkBox.setBackground(CARD_BG);
                checkBox.setForeground(TEXT_PRIMARY);
                checkBox.setFocusPainted(false);
                checkBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                algorithmCheckboxes.put(algoName, checkBox);
                checkboxesPanel.add(checkBox);
            }
        }
        
        panel.add(checkboxesPanel);
        return panel;
    }
    
    private JPanel createDataTypeSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel("📊 Tipo de Dados:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createVerticalStrut(8));
        
        dataTypeComboBox = new JComboBox<>(new String[]{
            "Externo", "Aleatório", "Ordenado", "Reverso", "Quase Ordenado"
        });
        styleComboBox(dataTypeComboBox);
        panel.add(dataTypeComboBox);
        
        return panel;
    }
    
    private JPanel createCriteriaSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel("🎯 Critério:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createVerticalStrut(8));
        
        sortCriteriaComboBox = new JComboBox<>(new String[]{
            "id", "timestamp", "latitude", "longitude", "quality"
        });
        styleComboBox(sortCriteriaComboBox);
        panel.add(sortCriteriaComboBox);
        
        return panel;
    }
    
    private JPanel createSizeSection() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(CARD_BG);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel label = new JLabel("📏 Tamanho:");
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        label.setForeground(TEXT_PRIMARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.add(label);
        
        panel.add(Box.createVerticalStrut(8));
        
        JPanel sizePanel = new JPanel(new BorderLayout(10, 0));
        sizePanel.setBackground(CARD_BG);
        sizePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        dataSizeSpinner = new JSpinner(new SpinnerNumberModel(1000, 100, 100000, 100));
        styleSpinner(dataSizeSpinner);
        
        JLabel unitLabel = new JLabel("elementos");
        unitLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        unitLabel.setForeground(TEXT_SECONDARY);
        
        sizePanel.add(dataSizeSpinner, BorderLayout.CENTER);
        sizePanel.add(unitLabel, BorderLayout.EAST);
        
        panel.add(sizePanel);
        return panel;
    }
    
    private JPanel createResultsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BACKGROUND);
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BACKGROUND);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel title = new JLabel("📊 RESULTADOS");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(ACCENT);
        
        performanceSummaryLabel = new JLabel("Pronto para executar testes");
        performanceSummaryLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        performanceSummaryLabel.setForeground(TEXT_SECONDARY);
        
        headerPanel.add(title, BorderLayout.WEST);
        headerPanel.add(performanceSummaryLabel, BorderLayout.EAST);
        
        // Área de texto
        resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Consolas", Font.PLAIN, 11));
        resultsArea.setBackground(new Color(25, 25, 35));
        resultsArea.setForeground(TEXT_SECONDARY);
        resultsArea.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JScrollPane scrollPane = new JScrollPane(resultsArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(60, 60, 70), 1));
        scrollPane.getViewport().setBackground(new Color(25, 25, 35));
        
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createActionPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBackground(BACKGROUND);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Barra de progresso
        progressBar = new JProgressBar();
        progressBar.setVisible(false);
        progressBar.setStringPainted(true);
        progressBar.setForeground(ACCENT);
        progressBar.setBackground(new Color(50, 50, 60));
        
        // Status
        statusLabel = new JLabel("🟢 Pronto para executar testes");
        statusLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        statusLabel.setForeground(SUCCESS);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Botões
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BACKGROUND);
        
        buttonPanel.add(createButton("🧪 Individual", ACCENT, this::runSingleTest));
        buttonPanel.add(createButton("🔬 Comparativo", SUCCESS, this::runMultipleTests));
        buttonPanel.add(createButton("📈 Escalabilidade", PURPLE, this::runScalabilityTest));
        buttonPanel.add(createButton("🗑️ Limpar", DANGER, this::clearResults));
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBackground(BACKGROUND);
        progressPanel.add(progressBar, BorderLayout.NORTH);
        progressPanel.add(statusLabel, BorderLayout.SOUTH);
        
        panel.add(progressPanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton createButton(String text, Color color, Runnable action) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(color.darker(), 2),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addActionListener(e -> {
            try {
                action.run();
            } catch (Exception ex) {
                showStatus("🔴 Erro: " + ex.getMessage(), DANGER);
                appendError("Erro ao executar ação: " + ex.getMessage());
            }
        });
        
        return button;
    }
    
    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setBackground(new Color(40, 40, 50));
        comboBox.setForeground(TEXT_PRIMARY);
        comboBox.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        comboBox.setMaximumSize(new Dimension(300, 40));
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.setBackground(isSelected ? ACCENT : new Color(40, 40, 50));
                c.setForeground(isSelected ? Color.WHITE : TEXT_PRIMARY);
                return c;
            }
        });
    }
    
    private void styleSpinner(JSpinner spinner) {
        spinner.setPreferredSize(new Dimension(120, 35));
        JSpinner.DefaultEditor editor = (JSpinner.DefaultEditor) spinner.getEditor();
        editor.getTextField().setBackground(new Color(40, 40, 50));
        editor.getTextField().setForeground(TEXT_PRIMARY);
        editor.getTextField().setCaretColor(TEXT_PRIMARY);
        editor.getTextField().setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
        editor.getTextField().setFont(new Font("Segoe UI", Font.PLAIN, 11));
        
        // CORREÇÃO: Estilizar os botões do spinner
        Component editorComp = spinner.getEditor();
        if (editorComp instanceof JSpinner.DefaultEditor) {
            JTextField textField = ((JSpinner.DefaultEditor) editorComp).getTextField();
            textField.setBackground(new Color(40, 40, 50));
            textField.setForeground(TEXT_PRIMARY);
        }
    }
    
    private void runSingleTest() {
        List<String> selected = getSelectedAlgorithms();
        if (selected.isEmpty()) {
            showStatus("🔴 Selecione pelo menos um algoritmo!", DANGER);
            return;
        }
        
        String algorithm = selected.get(0);
        String dataType = (String) dataTypeComboBox.getSelectedItem();
        int dataSize = (Integer) dataSizeSpinner.getValue();
        String criteria = (String) sortCriteriaComboBox.getSelectedItem();
        
        executeTest(() -> {
            try {
                // CORREÇÃO: Verificar se o controller não é nulo
                if (controller == null) {
                    throw new IllegalStateException("Controller não inicializado");
                }
                
                PerformanceResult result = controller.runSortingTest(algorithm, dataType, dataSize, criteria);
                SwingUtilities.invokeLater(() -> {
                    showStatus(result.isSuccess() ? "🟢 Teste concluído!" : "🔴 Falha na ordenação!", 
                              result.isSuccess() ? SUCCESS : DANGER);
                    appendResult(result);
                    updatePerformanceSummary();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    showStatus("🔴 Erro: " + e.getMessage(), DANGER);
                    appendError("Erro no " + algorithm + ": " + e.getMessage());
                });
            }
        }, "Executando " + algorithm + "...");
    }
    
    private void runMultipleTests() {
        List<String> selected = getSelectedAlgorithms();
        if (selected.isEmpty()) {
            showStatus("🔴 Selecione pelo menos um algoritmo!", DANGER);
            return;
        }
        
        String dataType = (String) dataTypeComboBox.getSelectedItem();
        int dataSize = (Integer) dataSizeSpinner.getValue();
        
        // CORREÇÃO: Usar a assinatura correta do método (sem criteria)
        executeTest(() -> {
            try {
                // CORREÇÃO: Verificar se o controller não é nulo
                if (controller == null) {
                    throw new IllegalStateException("Controller não inicializado");
                }
                
                List<PerformanceResult> results = controller.performComparativeTest(dataSize, dataType);
                
                SwingUtilities.invokeLater(() -> {
                    showStatus("🟢 Testes comparativos concluídos!", SUCCESS);
                    resultsArea.setText("");
                    
                    appendHeader("=== TESTE COMPARATIVO ===");
                    appendHeader("Tipo de Dados: " + dataType);
                    appendHeader("Tamanho: " + dataSize + " elementos");
                    appendHeader("Data: " + new Date());
                    appendHeader("");
                    
                    appendHeader("🏆 RANKING:");
                    for (int i = 0; i < results.size(); i++) {
                        PerformanceResult result = results.get(i);
                        String medal = i == 0 ? "🥇" : i == 1 ? "🥈" : i == 2 ? "🥉" : "  ";
                        appendResultWithRanking(result, medal, i + 1);
                    }
                    
                    updatePerformanceSummary();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    showStatus("🔴 Erro nos testes comparativos", DANGER);
                    appendError("Erro: " + e.getMessage());
                });
            }
        }, "Executando testes comparativos...");
    }
    
    private void runScalabilityTest() {
        List<String> selected = getSelectedAlgorithms();
        if (selected.isEmpty()) {
            showStatus("🔴 Selecione pelo menos um algoritmo!", DANGER);
            return;
        }
        
        String algorithm = selected.get(0);
        int maxSize = (Integer) dataSizeSpinner.getValue();
        
        // CORREÇÃO: Usar a assinatura correta do método (apenas algorithm e maxSize)
        executeTest(() -> {
            try {
                // CORREÇÃO: Verificar se o controller não é nulo
                if (controller == null) {
                    throw new IllegalStateException("Controller não inicializado");
                }
                
                List<PerformanceResult> results = controller.performScalabilityTest(algorithm, maxSize);
                
                SwingUtilities.invokeLater(() -> {
                    showStatus("🟢 Teste de escalabilidade concluído!", SUCCESS);
                    resultsArea.setText("");
                    
                    appendHeader("=== ESCALABILIDADE ===");
                    appendHeader("Algoritmo: " + algorithm);
                    appendHeader("Tamanho Máximo: " + maxSize);
                    appendHeader("Data: " + new Date());
                    appendHeader("");
                    
                    appendHeader("📈 RESULTADOS:");
                    for (PerformanceResult result : results) {
                        appendScalabilityResult(result);
                    }
                    
                    updatePerformanceSummary();
                });
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    showStatus("🔴 Erro no teste de escalabilidade", DANGER);
                    appendError("Erro: " + e.getMessage());
                });
            }
        }, "Testando escalabilidade...");
    }
    
    private void executeTest(Runnable test, String message) {
        new Thread(() -> {
            SwingUtilities.invokeLater(() -> {
                progressBar.setVisible(true);
                progressBar.setIndeterminate(true);
                progressBar.setString(message);
                showStatus("🟡 " + message, WARNING);
            });
            
            try {
                test.run();
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> {
                    showStatus("🔴 Erro durante execução", DANGER);
                    appendError("Erro na execução: " + e.getMessage());
                });
            } finally {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(false);
                });
            }
        }).start();
    }
    
    private List<String> getSelectedAlgorithms() {
        List<String> selected = new ArrayList<>();
        for (Map.Entry<String, JCheckBox> entry : algorithmCheckboxes.entrySet()) {
            if (entry.getValue().isSelected()) {
                selected.add(entry.getKey());
            }
        }
        return selected;
    }
    
    private void showStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);
    }
    
    private void appendHeader(String text) {
        resultsArea.append(text + "\n");
    }
    
    private void appendResult(PerformanceResult result) {
        String line = String.format("🔹 %-12s | ⏱️ %6d ms | 💾 %6d KB | 📊 %7d | %s\n",
            result.getAlgorithmName(),
            result.getExecutionTime(),
            result.getMemoryUsed(),
            result.getDataSize(),
            result.isSuccess() ? "✅" : "❌"
        );
        resultsArea.append(line);
        resultsArea.setCaretPosition(resultsArea.getText().length());
    }
    
    private void appendResultWithRanking(PerformanceResult result, String medal, int position) {
        String line = String.format("%s %d. %-12s | ⏱️ %6d ms | 💾 %6d KB | %s\n",
            medal, position, result.getAlgorithmName(),
            result.getExecutionTime(),
            result.getMemoryUsed(),
            result.isSuccess() ? "✅" : "❌"
        );
        resultsArea.append(line);
        resultsArea.setCaretPosition(resultsArea.getText().length());
    }
    
    private void appendScalabilityResult(PerformanceResult result) {
        String line = String.format("📏 %7d | ⏱️ %6d ms | 💾 %6d KB | %s\n",
            result.getDataSize(),
            result.getExecutionTime(),
            result.getMemoryUsed(),
            result.isSuccess() ? "✅" : "❌"
        );
        resultsArea.append(line);
        resultsArea.setCaretPosition(resultsArea.getText().length());
    }
    
    private void appendError(String error) {
        resultsArea.append("❌ " + error + "\n");
        resultsArea.setCaretPosition(resultsArea.getText().length());
    }
    
    private void updatePerformanceSummary() {
        try {
            // CORREÇÃO: Verificar se o controller não é nulo
            if (controller != null) {
                List<PerformanceResult> history = controller.getTestHistory();
                if (!history.isEmpty()) {
                    performanceSummaryLabel.setText("Testes executados: " + history.size());
                }
            }
        } catch (Exception e) {
            performanceSummaryLabel.setText("Erro ao carregar histórico");
        }
    }
    
    private void clearResults() {
        resultsArea.setText("");
        showStatus("🗑️ Resultados limpos", TEXT_SECONDARY);
        
        // CORREÇÃO: Usar javax.swing.Timer explicitamente para evitar ambiguidade
        javax.swing.Timer timer = new javax.swing.Timer(2000, e -> {
            showStatus("🟢 Pronto para executar testes", SUCCESS);
            ((javax.swing.Timer)e.getSource()).stop();
        });
        timer.setRepeats(false);
        timer.start();
    }
}
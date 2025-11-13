import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                System.out.println("=== SISTEMA DE COMPARAÇÃO DE ALGORITMOS DE ORDENAÇÃO ===");
                System.out.println("Contexto: Ordenação de imagens de satélite da Amazônia");
                System.out.println("Requisito: Comparar 3+ técnicas de ordenação");
                System.out.println("Dados: Externos (arquivo) e Internos (aleatórios)");
                System.out.println("Medida: Apenas tempo de ordenação\n");
                
                MainController controller = new MainController();
                controller.initialize();
                
            } catch (Exception e) {
                System.err.println("Erro ao iniciar o sistema: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
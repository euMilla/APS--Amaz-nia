
public class DashboardContent {
    
    public static String getSystemAnalysisContent() {
        return "<html><div style='font-family: Segoe UI; color: #e0e0e0; line-height: 1.4;'>" +
            
            "<h2 style='color: #4e86e8; margin-bottom: 15px;'>🌐 VISÃO GERAL DO SISTEMA</h2>" +
            
            "<h3 style='color: #46cc71; margin: 10px 0;'>📊 CONTEXTO DA APLICAÇÃO</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Sistema de análise de algoritmos de ordenação</li>" +
            "<li>Foco em imagens de satélite da Amazônia</li>" +
            "<li>Processamento de grandes volumes de dados</li>" +
            "<li>Comparação de desempenho entre técnicas</li>" +
            "</ul>" +
            
            "<h3 style='color: #46cc71; margin: 10px 0;'>🎯 OBJETIVOS PRINCIPAIS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Avaliar eficiência de algoritmos de ordenação</li>" +
            "<li>Analisar consumo de memória e tempo</li>" +
            "<li>Testar escalabilidade com diferentes tamanhos</li>" +
            "<li>Comparar desempenho em cenários reais</li>" +
            "</ul>" +
            
            "<h3 style='color: #46cc71; margin: 10px 0;'>🛰️ DADOS PROCESSADOS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Imagens de satélite da região amazônica</li>" +
            "<li>Metadados: coordenadas, timestamp, qualidade</li>" +
            "<li>Dados externos (arquivos) e gerados</li>" +
            "<li>Até 100.000 registros simultâneos</li>" +
            "</ul>" +
            
            "<h3 style='color: #46cc71; margin: 10px 0;'>⚡ CARACTERÍSTICAS TÉCNICAS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Interface gráfica moderna (Swing)</li>" +
            "<li>Visualização em tempo real</li>" +
            "<li>Exportação de resultados (CSV)</li>" +
            "<li>Mapa interativo da região</li>" +
            "<li>Gráficos animados de performance</li>" +
            "</ul>" +
            
            "<h3 style='color: #46cc71; margin: 10px 0;'>🔍 MÉTRICAS ANALISADAS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Tempo de execução (milissegundos)</li>" +
            "<li>Uso de memória (KB)</li>" +
            "<li>Taxa de sucesso na ordenação</li>" +
            "<li>Escalabilidade com tamanho de dados</li>" +
            "<li>Comparação entre critérios de ordenação</li>" +
            "</ul>" +
            
            "</div></html>";
    }
    
    public static String getAlgorithmsContent() {
        return "<html><div style='font-family: Segoe UI; color: #e0e0e0; line-height: 1.4;'>" +
            
            "<h2 style='color: #4e86e8; margin-bottom: 15px;'>⚙️ ALGORITMOS DE ORDENAÇÃO IMPLEMENTADOS</h2>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px; border-left: 4px solid #4e86e8;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🔧 QUICKSORT</h3>" +
            "<ul style='margin: 5px 0; font-size: 0.9em;'>" +
            "<li><b>Estratégia:</b> Divisão e conquista</li>" +
            "<li><b>Complexidade:</b> O(n log n) médio | O(n²) pior caso</li>" +
            "<li><b>Memória:</b> O(log n)</li>" +
            "<li><b>Características:</b> In-place, recursivo, não estável</li>" +
            "<li><b>Melhor para:</b> Dados aleatórios de tamanho médio a grande</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px; border-left: 4px solid #46cc71;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🔧 MERGESORT</h3>" +
            "<ul style='margin: 5px 0; font-size: 0.9em;'>" +
            "<li><b>Estratégia:</b> Divisão e conquista</li>" +
            "<li><b>Complexidade:</b> O(n log n) garantido</li>" +
            "<li><b>Memória:</b> O(n)</li>" +
            "<li><b>Características:</b> Estável, não in-place</li>" +
            "<li><b>Melhor para:</b> Quando estabilidade é importante</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px; border-left: 4px solid #ff5e57;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🔧 HEAPSORT</h3>" +
            "<ul style='margin: 5px 0; font-size: 0.9em;'>" +
            "<li><b>Estratégia:</b> Seleção por heap</li>" +
            "<li><b>Complexidade:</b> O(n log n) garantido</li>" +
            "<li><b>Memória:</b> O(1)</li>" +
            "<li><b>Características:</b> In-place, não estável</li>" +
            "<li><b>Melhor para:</b> Sistemas com memória limitada</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px; border-left: 4px solid #9b59b6;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🔧 TIMSORT</h3>" +
            "<ul style='margin: 5px 0; font-size: 0.9em;'>" +
            "<li><b>Estratégia:</b> Híbrida (Merge + Insertion)</li>" +
            "<li><b>Complexidade:</b> O(n log n)</li>" +
            "<li><b>Memória:</b> O(n)</li>" +
            "<li><b>Características:</b> Adaptativo, estável</li>" +
            "<li><b>Melhor para:</b> Dados parcialmente ordenados</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #333; padding: 12px; margin: 15px 0; border-radius: 6px; border: 1px solid #555;'>" +
            "<h3 style='color: #f1c40f; margin: 5px 0; text-align: center;'>🏆 RESUMO DE DESEMPENHO</h3>" +
            "<ul style='margin: 5px 0; text-align: center; list-style: none;'>" +
            "<li>🚀 <b>Mais rápido:</b> QuickSort (na prática)</li>" +
            "<li>📊 <b>Mais consistente:</b> MergeSort</li>" +
            "<li>💾 <b>Menos memória:</b> HeapSort</li>" +
            "<li>🔄 <b>Mais adaptativo:</b> TimSort</li>" +
            "</ul>" +
            "</div>" +
            
            "</div></html>";
    }
    
    public static String getPerformanceTips() {
        return "<html><div style='font-family: Segoe UI; color: #e0e0e0; line-height: 1.4;'>" +
            
            "<h2 style='color: #4e86e8; margin-bottom: 15px;'>💡 DICAS DE OTIMIZAÇÃO E USO</h2>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🎯 CONFIGURAÇÃO DE TESTES</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Comece com tamanhos pequenos (1.000 elementos)</li>" +
            "<li>Aumente gradualmente para testar escalabilidade</li>" +
            "<li>Use dados externos para cenários realistas</li>" +
            "<li>Teste diferentes critérios de ordenação</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>📊 INTERPRETAÇÃO DE RESULTADOS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li><b>QuickSort:</b> Geralmente o mais rápido</li>" +
            "<li><b>MergeSort:</b> Performance consistente</li>" +
            "<li><b>HeapSort:</b> Excelente em memória limitada</li>" +
            "<li><b>TimSort:</b> Ideal para dados parcialmente ordenados</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>⚡ OTIMIZAÇÕES RECOMENDADAS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Para dados pequenos: Insertion Sort</li>" +
            "<li>Para dados quase ordenados: TimSort</li>" +
            "<li>Quando estabilidade importa: MergeSort</li>" +
            "<li>Com restrição de memória: HeapSort</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🔍 ANÁLISE DE DESEMPENHO</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Monitore tanto tempo quanto memória</li>" +
            "<li>Execute múltiplas iterações para média</li>" +
            "<li>Compare com diferentes conjuntos de dados</li>" +
            "<li>Verifique a correção da ordenação</li>" +
            "</ul>" +
            "</div>" +
            
            "</div></html>";
    }
    
    public static String getSystemRequirements() {
        return "<html><div style='font-family: Segoe UI; color: #e0e0e0; line-height: 1.4;'>" +
            
            "<h2 style='color: #4e86e8; margin-bottom: 15px;'>⚙️ REQUISITOS E CONFIGURAÇÃO</h2>" +
            
            "<div style='display: flex; gap: 15px; margin-bottom: 15px;'>" +
            "<div style='flex: 1; background: #2a2a2a; padding: 12px; border-radius: 6px; border-left: 4px solid #ff5e57;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>💻 MÍNIMOS</h3>" +
            "<ul style='margin: 5px 0; font-size: 0.9em;'>" +
            "<li>Windows 10, Linux ou macOS</li>" +
            "<li>Java 8 ou superior</li>" +
            "<li>2 GB de RAM</li>" +
            "<li>Dual-core 2.0 GHz</li>" +
            "<li>100 MB livres</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='flex: 1; background: #2a2a2a; padding: 12px; border-radius: 6px; border-left: 4px solid #46cc71;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🚀 RECOMENDADOS</h3>" +
            "<ul style='margin: 5px 0; font-size: 0.9em;'>" +
            "<li>4 GB+ de RAM</li>" +
            "<li>Quad-core 3.0 GHz+</li>" +
            "<li>SSD para performance</li>" +
            "<li>Java 11+</li>" +
            "<li>GPU básica</li>" +
            "</ul>" +
            "</div>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>📁 ESTRUTURA DE ARQUIVOS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li><b>satellite_images.txt:</b> Dados externos</li>" +
            "<li><b>resultados_analise.csv:</b> Exportação</li>" +
            "<li>Logs no console para depuração</li>" +
            "<li>Configurações salvas automaticamente</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🔧 DEPENDÊNCIAS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Java Swing: Interface gráfica</li>" +
            "<li>Collections Framework: Estruturas de dados</li>" +
            "<li>I/O: Leitura/escrita de arquivos</li>" +
            "<li>AWT: Componentes gráficos</li>" +
            "</ul>" +
            "</div>" +
            
            "</div></html>";
    }
    
    public static String getDatasetInfo() {
        return "<html><div style='font-family: Segoe UI; color: #e0e0e0; line-height: 1.4;'>" +
            
            "<h2 style='color: #4e86e8; margin-bottom: 15px;'>📁 CONJUNTO DE DADOS</h2>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🛰️ ORIGEM DOS DADOS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Imagens de satélite da região amazônica</li>" +
            "<li>Dados sintéticos gerados para testes</li>" +
            "<li>Metadados com coordenadas reais</li>" +
            "<li>Timestamps distribuídos temporalmente</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>📊 ESTRUTURA DOS DADOS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li><b>ID:</b> Identificador único (SAT_IMG_...)</li>" +
            "<li><b>Coordenadas:</b> Latitude e longitude</li>" +
            "<li><b>Timestamp:</b> Data/hora em milissegundos</li>" +
            "<li><b>Qualidade:</b> Alta, Média, Baixa</li>" +
            "<li><b>Região:</b> Estado da Amazônia</li>" +
            "<li><b>Tamanho:</b> Em KB</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🌍 COBERTURA GEOGRÁFICA</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li><b>Região:</b> Amazônia Brasileira</li>" +
            "<li><b>Estados:</b> AM, PA, MT, RO, AC, RR, AP, TO, MA</li>" +
            "<li><b>Latitude:</b> -12° a 6°</li>" +
            "<li><b>Longitude:</b> -74° a -44°</li>" +
            "<li><b>Foco:</b> Áreas de monitoramento ambiental</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🎯 CRITÉRIOS DE ORDENAÇÃO</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li><b>ID:</b> Ordem alfabética do identificador</li>" +
            "<li><b>Timestamp:</b> Ordem cronológica</li>" +
            "<li><b>Latitude:</b> Norte para sul</li>" +
            "<li><b>Longitude:</b> Oeste para leste</li>" +
            "<li><b>Qualidade:</b> Alta > Média > Baixa</li>" +
            "</ul>" +
            "</div>" +
            
            "</div></html>";
    }
    
    public static String getTestingScenarios() {
        return "<html><div style='font-family: Segoe UI; color: #e0e0e0; line-height: 1.4;'>" +
            
            "<h2 style='color: #4e86e8; margin-bottom: 15px;'>🧪 CENÁRIOS DE TESTE</h2>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>📊 TIPOS DE DADOS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li><b>Externo:</b> Carregado do arquivo</li>" +
            "<li><b>Aleatório:</b> Gerado aleatoriamente</li>" +
            "<li><b>Ordenado:</b> Dados em ordem (melhor caso)</li>" +
            "<li><b>Reverso:</b> Ordem inversa (pior caso)</li>" +
            "<li><b>Quase Ordenado:</b> 90% ordenado, 10% aleatório</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>📈 TESTES DISPONÍVEIS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li><b>Individual:</b> Um algoritmo por vez</li>" +
            "<li><b>Comparativo:</b> Todos simultaneamente</li>" +
            "<li><b>Escalabilidade:</b> Crescimento com tamanho</li>" +
            "<li><b>Memória:</b> Análise de consumo de RAM</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #2a2a2a; padding: 10px; margin: 8px 0; border-radius: 5px;'>" +
            "<h3 style='color: #46cc71; margin: 5px 0;'>🎯 PARÂMETROS CONFIGURÁVEIS</h3>" +
            "<ul style='margin: 5px 0;'>" +
            "<li>Tamanho do dataset: 100 a 100.000 elementos</li>" +
            "<li>Algoritmo(s) a testar</li>" +
            "<li>Critério de ordenação</li>" +
            "<li>Tipo de dados de entrada</li>" +
            "<li>Número de iterações</li>" +
            "</ul>" +
            "</div>" +
            
            "<div style='background: #333; padding: 12px; margin: 15px 0; border-radius: 6px; border: 1px solid #555;'>" +
            "<h3 style='color: #f1c40f; margin: 5px 0; text-align: center;'>📋 FLUXO DE TESTE</h3>" +
            "<ol style='margin: 5px 0; padding-left: 20px;'>" +
            "<li>Configurar parâmetros do teste</li>" +
            "<li>Executar algoritmo(s) selecionado(s)</li>" +
            "<li>Medir tempo e memória utilizados</li>" +
            "<li>Verificar correção da ordenação</li>" +
            "<li>Exibir resultados comparativos</li>" +
            "<li>Exportar dados se necessário</li>" +
            "</ol>" +
            "</div>" +
            
            "</div></html>";
    }
}
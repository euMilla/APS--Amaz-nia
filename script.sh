#!/bin/bash

# Sistema de Analise de Performance - Algoritmos de Ordenacao
echo "======================================================="
echo "   SISTEMA DE ANALISE DE PERFORMANCE"
echo "   Algoritmos de Ordenacao - Processamento de Imagens"
echo "======================================================="
echo ""

echo "Verificando estrutura de diretorios..."
if [ ! -d "src/resources" ]; then
    mkdir -p "src/resources"
fi

if [ ! -d "bin" ]; then
    mkdir "bin"
fi

echo "Compilando o sistema..."
javac -encoding UTF-8 -d bin -cp "src" src/*.java

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Compilacao concluida com sucesso!"
    echo ""
    echo "Executando o sistema..."
    echo ""
    java -cp "bin:src" Main
else
    echo ""
    echo "❌ Erro durante a compilacao!"
    echo "Verifique se todos os arquivos estao na pasta src/"
    echo ""
    echo "Listando arquivos na pasta src:"
    ls src/*.java
    read -p "Pressione Enter para continuar..."
fi
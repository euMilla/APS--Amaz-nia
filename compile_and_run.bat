@echo off
title Sistema de Analise de Performance - Algoritmos de Ordenacao
echo =======================================================
echo    SISTEMA DE ANALISE DE PERFORMANCE
echo    Algoritmos de Ordenacao - Processamento de Imagens
echo =======================================================
echo.

echo Verificando estrutura de diretorios...
if not exist "src\resources" mkdir "src\resources"
if not exist "bin" mkdir "bin"

echo Compilando o sistema...
javac -encoding UTF-8 -d bin -cp "src" src\*.java

if %errorlevel% equ 0 (
    echo.
    echo ✅ Compilacao concluida com sucesso!
    echo.
    echo Executando o sistema...
    echo.
    java -cp "bin;src" Main
) else (
    echo.
    echo ❌ Erro durante a compilacao!
    echo Verifique se todos os arquivos estao na pasta src\
    echo.
    echo Listando arquivos na pasta src:
    dir src\*.java /b
    pause
)
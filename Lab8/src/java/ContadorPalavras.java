import java.io.*;
import java.util.concurrent.*;

public class ContadorPalavras {
    public static void main(String[] args) {
	System.out.println("Lab8");
    if (args.length == 0) {
        System.out.println("Uso: java ContadorPalavras <arquivos>");
        return;
    }
    
    int totalPalavras = 0;
    for (String nomeArquivo : args) {
        try {
            int count = contarPalavras(nomeArquivo);
            System.out.println(nomeArquivo + ": " + count + " palavras");
            totalPalavras += count;
        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo " + nomeArquivo + ": " + e.getMessage());
        }
    }
    
    System.out.println("Total de palavras: " + totalPalavras);    
    }

    static int contarPalavras(String nomeArquivo) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(nomeArquivo));
        int count = 0;
        String linha;
        while ((linha = br.readLine()) != null) {
            count += linha.split("\\s+").length;
        }
        br.close();
        return count;
    }
}


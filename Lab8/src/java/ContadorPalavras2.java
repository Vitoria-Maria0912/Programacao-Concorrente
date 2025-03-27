import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ContadorPalavras2 {
    public static void main(String[] args) {






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

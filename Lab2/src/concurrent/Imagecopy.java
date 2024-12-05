import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class Imagecopy {

    public static void applyMeanFilter(String inputPath, String outputPath, int kernelSize, int threadCount) throws IOException {
        // Carregar a imagem de entrada
        BufferedImage originalImage = ImageIO.read(new File(inputPath));
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Criar a imagem de saída
        BufferedImage filteredImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Dividir o trabalho entre as threads
        int rowsPerThread = height / threadCount;
        Thread[] threads = new Thread[threadCount];

        for (int i = 0; i < threadCount; i++) {
            int startRow = i * rowsPerThread;
            int endRow = (i == threadCount - 1) ? height : (i + 1) * rowsPerThread;

            threads[i] = new Thread(new MeanFilterTask(originalImage, filteredImage, kernelSize, startRow, endRow));
            threads[i].start();
        }

        // Aguardar a conclusão de todas as threads
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Thread interrompida", e);
            }
        }

        // Salvar a imagem processada
        ImageIO.write(filteredImage, "jpg", new File(outputPath));
    }

    // Classe estática para a tarefa de filtragem de uma faixa da imagem
    private static class MeanFilterTask implements Runnable {
        private final BufferedImage originalImage;
        private final BufferedImage filteredImage;
        private final int kernelSize;
        private final int startRow;
        private final int endRow;

        public MeanFilterTask(BufferedImage originalImage, BufferedImage filteredImage, int kernelSize, int startRow, int endRow) {
            this.originalImage = originalImage;
            this.filteredImage = filteredImage;
            this.kernelSize = kernelSize;
            this.startRow = startRow;
            this.endRow = endRow;
        }

        @Override
        public void run() {
            int width = originalImage.getWidth();

            for (int y = startRow; y < endRow; y++) {
                for (int x = 0; x < width; x++) {
                    int[] avgColor = calculateNeighborhoodAverage(originalImage, x, y, kernelSize);
                    filteredImage.setRGB(x, y, (avgColor[0] << 16) | (avgColor[1] << 8) | avgColor[2]);
                }
            }
        }

        private int[] calculateNeighborhoodAverage(BufferedImage image, int centerX, int centerY, int kernelSize) {
            int width = image.getWidth();
            int height = image.getHeight();
            int pad = kernelSize / 2;

            long redSum = 0, greenSum = 0, blueSum = 0;
            int pixelCount = 0;

            for (int dy = -pad; dy <= pad; dy++) {
                for (int dx = -pad; dx <= pad; dx++) {
                    int x = centerX + dx;
                    int y = centerY + dy;

                    if (x >= 0 && x < width && y >= 0 && y < height) {
                        int rgb = image.getRGB(x, y);
                        redSum += (rgb >> 16) & 0xFF;
                        greenSum += (rgb >> 8) & 0xFF;
                        blueSum += rgb & 0xFF;
                        pixelCount++;
                    }
                }
            }

            return new int[]{
                (int) (redSum / pixelCount),
                (int) (greenSum / pixelCount),
                (int) (blueSum / pixelCount)
            };
        }
    }

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Uso: java ConcurrentImageMeanFilter <input_file> <threads> <kernel_size>");
            System.exit(1);
        }

        String inputFile = args[0];
        int threadCount = Integer.parseInt(args[1]);
        int kernelSize = Integer.parseInt(args[2]);

        try {
            applyMeanFilter(inputFile, "filtered_output.jpg", kernelSize, threadCount);
            System.out.println("Imagem processada com sucesso.");
        } catch (IOException e) {
            System.err.println("Erro ao processar a imagem: " + e.getMessage());
        }
    }
}


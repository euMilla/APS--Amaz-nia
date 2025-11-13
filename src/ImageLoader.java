import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class ImageLoader {
    private static final String[] REGIONS = {
        "Amazônia Legal", "Pará", "Amazonas", "Mato Grosso", "Rondônia", 
        "Acre", "Roraima", "Amapá", "Tocantins", "Maranhão"
    };
    
    private static final String[] QUALITIES = {"Alta", "Média", "Baixa"};
    
    public static List<SatelliteImage> loadFromFile(String filename) {
        List<SatelliteImage> images = new ArrayList<>();
        
        try {
            File file = new File(filename);
            if (!file.exists()) {
                System.out.println("Arquivo não encontrado: " + filename + ". Gerando dados de exemplo...");
                return generateSampleData(100000);
            }
            
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            int count = 0;
            
            while ((line = reader.readLine()) != null && count < 100000) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    try {
                        SatelliteImage image = new SatelliteImage(
                            parts[0].trim(),
                            Double.parseDouble(parts[1].trim()),
                            Double.parseDouble(parts[2].trim()),
                            Long.parseLong(parts[3].trim()),
                            parts[4].trim(),
                            parts.length > 5 ? parts[5].trim() : "Amazônia",
                            parts.length > 6 ? Integer.parseInt(parts[6].trim()) : 2048
                        );
                        images.add(image);
                        count++;
                    } catch (NumberFormatException e) {
                        System.err.println("Erro ao parsear linha: " + line);
                    }
                }
            }
            reader.close();
            System.out.println("Carregadas " + images.size() + " imagens do arquivo.");
            
        } catch (IOException e) {
            System.err.println("Erro ao carregar arquivo: " + e.getMessage());
            return generateSampleData(50000);
        }
        
        return images;
    }
    
    public static List<SatelliteImage> generateSampleData(int count) {
        List<SatelliteImage> images = new ArrayList<>();
        Random random = new Random();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        
        System.out.println("Gerando " + count + " imagens de satélite de exemplo...");
        
        for (int i = 0; i < count; i++) {
            String imageId = "SAT_IMG_" + sdf.format(new Date()) + "_" + String.format("%06d", i);
            double latitude = -3.0 + random.nextDouble() * 12.0 - 6.0;
            double longitude = -60.0 + random.nextDouble() * 20.0 - 10.0;
            long timestamp = System.currentTimeMillis() - random.nextInt(365 * 24 * 60 * 60 * 1000);
            String quality = QUALITIES[random.nextInt(QUALITIES.length)];
            String region = REGIONS[random.nextInt(REGIONS.length)];
            int sizeKB = 512 + random.nextInt(4096);
            
            SatelliteImage image = new SatelliteImage(
                imageId, latitude, longitude, timestamp, quality, region, sizeKB
            );
            images.add(image);
        }
        
        saveToFile(images, "src/resources/satellite_images.txt");
        return images;
    }
    
    private static void saveToFile(List<SatelliteImage> images, String filename) {
        try {
            // Criar diretório se não existir
            File directory = new File("src/resources/");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            
            PrintWriter writer = new PrintWriter(new FileWriter(filename));
            for (SatelliteImage image : images) {
                writer.printf("%s,%.6f,%.6f,%d,%s,%s,%d\n",
                    image.getImageId(),
                    image.getLatitude(),
                    image.getLongitude(),
                    image.getTimestamp(),
                    image.getQuality(),
                    image.getRegion(),
                    image.getSizeKB()
                );
            }
            writer.close();
            System.out.println("Dados salvos em: " + filename);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo: " + e.getMessage());
        }
    }
    
    public static List<SatelliteImage> generateRandomData(int count) {
        List<SatelliteImage> images = new ArrayList<>();
        Random random = new Random();
        
        for (int i = 0; i < count; i++) {
            String imageId = "RND_" + System.currentTimeMillis() + "_" + i;
            double latitude = random.nextDouble() * 180 - 90;
            double longitude = random.nextDouble() * 360 - 180;
            long timestamp = System.currentTimeMillis();
            String quality = QUALITIES[random.nextInt(QUALITIES.length)];
            String region = "Aleatório";
            int sizeKB = 256 + random.nextInt(1024);
            
            images.add(new SatelliteImage(imageId, latitude, longitude, timestamp, quality, region, sizeKB));
        }
        
        return images;
    }
}
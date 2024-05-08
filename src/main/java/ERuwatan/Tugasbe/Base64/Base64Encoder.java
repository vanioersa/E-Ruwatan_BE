package ERuwatan.Tugasbe.Base64;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class Base64Encoder {
    public static void main(String[] args) {
        String imagePath = "path/to/image.jpg";
        String base64String = encodeImageToBase64(imagePath);
        System.out.println("Base64 string: " + base64String);
    }

    public static String encodeImageToBase64(String imagePath) {
        try {
            Path path = Paths.get(imagePath);
            byte[] imageBytes = Files.readAllBytes(path);
            String base64String = Base64.getEncoder().encodeToString(imageBytes);
            return base64String;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
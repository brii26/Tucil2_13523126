import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.image.BufferedImage;

public class ProcessImage {

    // Image Processing
    public static void ProcessInputImage(String in_file){
        String file = in_file.replace("\"", "");
        try{
            File inputFile = new File(file);
            if(inputFile.exists() && inputFile.isFile() && isValidImageExtension(inputFile)) {
                Main.imageFile = ImageIO.read(inputFile);
            }
            else{
                System.out.println("File is Invalid!");
            }
        }
        catch (IOException e){
            e.printStackTrace();
        }

    }

    // Extension validation (.png, .jpg, .jpeg)
    public static boolean isValidImageExtension(File file){
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png")  || fileName.endsWith(".jpeg");
    }

    public static boolean saveImage(BufferedImage image, String outputPath) {
        try {
            File outputFile = new File(outputPath);
            String lowerPath = outputPath.toLowerCase();
            String format;
            if(lowerPath.endsWith(".png")){
                format = "png";
            } else if(lowerPath.endsWith(".jpg") || lowerPath.endsWith(".jpeg")){
                format = "jpg";
            } else {
                System.out.println("Save Failed!");
                return false;
            }
            return ImageIO.write(image, format, outputFile);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

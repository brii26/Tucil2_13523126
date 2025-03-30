import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;

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

}

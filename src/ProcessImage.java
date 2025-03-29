import java.io.File;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Color;


public class ProcessImage {

    // Image Manipulation Methods
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

    public static boolean isValidImageExtension(File file){
        String fileName = file.getName().toLowerCase();
        return fileName.endsWith(".jpg") || fileName.endsWith(".png")  || fileName.endsWith(".jpeg");
    }



    // Get Average
    public static int[] getAverage(int x_coordinate, int y_coordinate, int width, int height){
        int redValue = 0;
        int greenValue = 0;
        int blueValue = 0;
        int count = 0;
        
        for (int y = y_coordinate - height/2; y < height; y++){
            for ( int x = x_coordinate - width/2 ; x < width ; x++){
                int pixelColor = Main.imageFile.getRGB(x,y);
                Color color = new Color(pixelColor);

                redValue += color.getRed();
                greenValue += color.getGreen();
                blueValue += color.getBlue();
                count++;
            }
        }
        int avg_red = redValue/count;
        int avg_blue = blueValue/count;
        int avg_green = greenValue/count;

        return new int[]{avg_red,avg_blue,avg_green};
    }

}

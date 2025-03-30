import java.util.Scanner;
import java.io.File;
import java.awt.image.BufferedImage;

public class Main{
    public static BufferedImage imageFile;
    public static void main(String args[]){
    
        int inputMode = -1;
        double treshold = -1;
        int minimumBlockSize = -1;
        double compressionPercentageTarget = -1.0;
        boolean countCompression = true;
        String absoluteImageOutputAddress ;
        String absoluteGifOutputAddress ;


        Scanner input = new Scanner(System.in);

        // Input Absolute Image Address
        do{
            System.out.print("Absolute image path : ");
            String filePath = input.nextLine();
            ProcessImage.ProcessInputImage(filePath);
        }while(imageFile == null);

        // Input Error Measurement Method
        do{
            System.out.println("\nSelect the following Error Measurement Methods :");
            System.out.println("1. Variance");
            System.out.println("2. Mean Absolute Deviation (MAD)");
            System.out.println("3. Max Pixel Defference");
            System.out.println("4. Entropy");
            System.out.println("5. Structural Similarity Index (SSIM)");
            System.out.print("mode (1/2/3/4/5): ");
            if (input.hasNextInt()){
                inputMode = input.nextInt();
                if(inputMode <1 || inputMode > 5){
                    System.out.println("Invalid input, input is out of range!");
                }
                System.out.println("\n");
            }
            else{
                input.nextLine();
                System.out.println("Invalid input, input is not an integer!");
                System.out.println("\n");
            }
        }while (inputMode < 1 || inputMode > 5);
        
        // Input treshold
        do {
            System.out.print("Threshold : ");
            
            if (input.hasNextDouble()) {
                treshold = input.nextDouble();
                if (treshold >= 0) {
                    break;
                } else {
                    System.out.println("Invalid treshold value");
                }
            } else {
                System.out.println("Invalid input");
                input.nextLine();
            }
        } while (treshold < 0);  

        // Input minimum block size
        do {
            System.out.print("Minimum Block Size (integer): ");
            
            if (input.hasNextInt()) {
                minimumBlockSize = input.nextInt();
                input.nextLine(); 
                
                if (minimumBlockSize > 0) {
                    break; 
                } else {
                    System.out.println("Invalid input: block size <= 0");
                }
            } else {
                System.out.println("Invalid input");
                input.nextLine();
            }
        } while (minimumBlockSize <= 0);


        // Input percentage compression target
        do {
            System.out.print("Percentage Compression Target: ");
            
            if (input.hasNextDouble()) {
                compressionPercentageTarget = input.nextDouble();
                input.nextLine(); 
                
                if (compressionPercentageTarget > 0 && compressionPercentageTarget <= 1) {
                    break;
                } else if (compressionPercentageTarget == 0) {
                    countCompression = false;
                    break;
                } else {
                    System.out.println("Input is out of range! (0.0-1.0)");
                }
            } else {
                System.out.println("Invalid input");
                input.nextLine();
            }
        } while (compressionPercentageTarget < 0 || compressionPercentageTarget > 1);

        // Input absolute output image address
        do {
            System.out.print("Absolute Output Image Address (png, jpg, jpeg): ");
            absoluteImageOutputAddress = input.nextLine();
            if (absoluteImageOutputAddress.toLowerCase().matches(".*\\.(png|jpg|jpeg)$")) {
                File file = new File(absoluteImageOutputAddress);
                File parentDir = file.getAbsoluteFile().getParentFile();
                
                if (parentDir == null) {
                    System.out.println("Invalid input: use an absolute path.");
                }
                else if (parentDir.exists() && parentDir.isDirectory()) {
                    if (file.exists()) {
                        System.out.println("File already exists!");
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Invalid input: directory doesn't exist!");
                }
            } else {
                System.out.println("Invalid input: .jpg / .png / .jpeg");
            }
        } while (true);

        // Input absolute gif address
        do {
            System.out.print("Absolute gif Address : ");
            absoluteGifOutputAddress = input.nextLine();
            if (absoluteGifOutputAddress.toLowerCase().endsWith(".gif")) {
                File file = new File(absoluteGifOutputAddress);
                File parentDir = file.getAbsoluteFile().getParentFile();
                if (parentDir == null) {
                    System.out.println("Invalid input: use an absolute path.");
                }
                else if (parentDir.exists() && parentDir.isDirectory()) {
                    if (file.exists()) {
                        System.out.println("File already exists!");
                    } else {
                        break;
                    }
                } else {
                    System.out.println("Invalid input: directory doesn't exist!");
                }
            } else {
                System.out.println("Invalid extension : .gif");
            }
        } while (true);

        input.close();

    }
}
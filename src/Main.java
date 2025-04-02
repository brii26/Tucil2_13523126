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
        String absoluteImageInputAddress;
        String absoluteImageOutputAddress ;
        String absoluteGifOutputAddress ;


        Scanner input = new Scanner(System.in);

        // Input Absolute Image Address
        do{
            System.out.print("Absolute image path : ");
            absoluteImageInputAddress = input.nextLine();
            ProcessImage.ProcessInputImage(absoluteImageInputAddress);
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
            System.out.print("Threshold ");
            // ideal range 
            if(inputMode == 1){
                System.out.print("Variance (ideal range [0..16384)) : "); // 16384 -> 128^2
            }else if(inputMode == 2){
                System.out.print("Mean Absolute Deviation (ideal range [0..255]) : ");
            }else if(inputMode == 3){
                System.out.print("Max Pixel Difference (ideal range [0..255]) : ");
            }else if(inputMode == 4){
                System.out.print("Entropy (ideal range [0..8]) : ");
            }
            else if (inputMode == 5){
                System.out.print("Structural Similarity Index (ideal range [-1..1]) : ");
            }
            
            if (input.hasNextDouble()) {
                treshold = input.nextDouble();
                if (treshold >= 0 || (treshold >= -1 && treshold <= 1 && inputMode == 5)) {
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

        // Bonus compression target variable helper
        int max_iteration = 30; 
        long inputImageSize = ProcessImage.getImageSize(imageFile, absoluteImageInputAddress);
        long outputImageSize = -1;
        double delta_change;
        double deltaToleranceToTarget = 100;
        double expectedOutputSize = inputImageSize * (1 - compressionPercentageTarget);
        boolean surpassTarget = false;
        boolean firstIteration = true;

        // Delta change for threshold initial change
        if(inputMode == 1) delta_change = 8192;
        else if(inputMode == 2) delta_change = 127;
        else if(inputMode == 3) delta_change = 127;
        else if(inputMode == 4) delta_change = 4;
        else if(inputMode == 5) delta_change = .5;
        else delta_change = -1;

        if(countCompression) treshold = delta_change/2;

        // output image 
        BufferedImage output = new BufferedImage(imageFile.getWidth(), imageFile.getHeight(), imageFile.getType());

        // create object QuadTree
        QuadTree qt = new QuadTree(imageFile.getWidth(), imageFile.getHeight());

        // compress & reconstruct
        long startTime = System.currentTimeMillis();
        if (countCompression == true){
            int iteration = max_iteration;
            double outputSize = 0;
            int countSameOutputSize = 0;

            // loop for compression target treshold adjustment
            while(Math.abs(outputImageSize - expectedOutputSize) > deltaToleranceToTarget && iteration > 0 && countSameOutputSize < 4){
                qt.compressQuadTree(inputMode, treshold, minimumBlockSize);
                qt.reconstructQuadTree(output);
                outputImageSize = ProcessImage.getImageSize(output, absoluteImageOutputAddress);

                System.out.println("Iteration " + (max_iteration - iteration + 1) + " => threshold: " + treshold + " | output size: " + outputImageSize + " | expected output size: " + expectedOutputSize);

                if(outputImageSize > expectedOutputSize){
                    if(!firstIteration && !surpassTarget){
                        delta_change /= 2.2;
                    }
                    treshold += delta_change;
                    surpassTarget = true;
                }
                else if ( outputImageSize < expectedOutputSize){
                    if(!firstIteration && surpassTarget){
                        delta_change /= 2.2;
                    }
                    treshold -= delta_change;
                    surpassTarget = false;
                }
                firstIteration = false;
                iteration--;
                if(outputSize == outputImageSize){
                    countSameOutputSize++;
                }
                else{
                    outputSize = outputImageSize;
                    countSameOutputSize = 0;
                }
            } 
        }
        else{
            qt.compressQuadTree(inputMode, treshold, minimumBlockSize);
            qt.reconstructQuadTree(output);
        }
        ProcessImage.saveImage(output, absoluteImageOutputAddress);
        long endTime = System.currentTimeMillis();


        // Output
        System.out.println("\n\n\n\n\n\n\nExecution Time : " + (endTime - startTime) + "ms");
        System.out.println("Image before compression size : " + inputImageSize + " bytes");
        File output_file = new File(absoluteImageOutputAddress);
        System.out.println("Image after compression size : " + output_file.length() + " bytes");
        double compression_percentage = (1 - ((double)outputImageSize/inputImageSize)) * 100.0;
        System.out.println("Compression percentage : " + String.format("%.2f", compression_percentage) + "%");
        System.out.println("QuadTree depth : " + qt.getDepth());
        System.out.println("QuadTree nodes : " + qt.getNodes());
    }
}
import java.awt.Color;

class ErrorMeasurement{

    // Variance Method
    public static double variance(QuadTree qt){
        double[] variance_value = {0,0,0};
        int n = qt.getPixel();
        for ( int y = qt.startY(); y < qt.endY() ; y++){
            for ( int x = qt.startX(); x < qt.endX() ; x++){
                int pixelColor = Main.imageFile.getRGB(x,y);
                Color color = new Color(pixelColor);
                variance_value[0] += Math.pow((color.getRed() - qt.getRedIntensityValue()),2);
                variance_value[1] += Math.pow((color.getGreen() - qt.getGreenIntensityValue()),2);
                variance_value[2] += Math.pow((color.getBlue() - qt.getBlueIntensityValue()),2);
            }
        }
        return (variance_value[0] + variance_value[1] + variance_value[2]) / (3*n);
    }

    // Mean Absolute Deviation Method
    public static double meanAbsoluteDeviation(QuadTree qt){
        double[] mad_value = {0,0,0};
        int n = qt.getPixel();
        for ( int y = qt.startY(); y < qt.endY() ; y++){
            for (int x  = qt.startX() ; x < qt.endX() ; x++){
                int pixelColor = Main.imageFile.getRGB(x,y);
                Color color = new Color(pixelColor);
                mad_value[0] += Math.abs(color.getRed() - qt.getRedIntensityValue());
                mad_value[1] += Math.abs(color.getGreen() - qt.getGreenIntensityValue());
                mad_value[2] += Math.abs(color.getBlue() - qt.getBlueIntensityValue());
            }
        }
        return (mad_value[0] + mad_value[1] + mad_value[2])/(3*n) ;
    }

    // Max Pixel Difference Method
    public static double maxPixelDifference(QuadTree qt){
        int max_red = 0;
        int min_red = 255;
        int max_green = 0;
        int min_green = 255;
        int max_blue = 0;
        int min_blue = 255;

        for (int y = qt.startY() ; y < qt.endY() ; y++){
            for ( int x = qt.startX() ; x < qt.endX() ; x++){
                int pixelColor = Main.imageFile.getRGB(x,y);
                Color color = new Color (pixelColor);
                
                if (color.getRed() < min_red){
                    min_red = color.getRed();
                }
                else if(max_red < color.getRed()){
                    max_red = color.getRed();
                }

                if(color.getGreen() < min_green){
                    min_green = color.getGreen();
                }
                else if (max_green < color.getGreen()){
                    max_green = color.getGreen();
                }

                if(color.getBlue () < min_blue){
                    min_blue = color.getBlue();
                }
                else if (max_blue < color.getBlue()){
                    max_blue = color.getBlue();
                }
            }
        }
        return (((max_red - min_red) + (max_green - min_green) + (max_blue - min_blue)) / 3);
    }

    // Entropy Method
    public static double entropy(QuadTree qt){
        int n = qt.getPixel();
        int[] histogram = new int[256];
        for ( int y = qt.startY() ; y < qt.endY() ; y++){
            for (int x = qt.startX() ; x < qt.endX(); x++){
                int pixelColor = Main.imageFile.getRGB(x,y);
                Color color = new Color(pixelColor);
                int rgbValue = ((color.getRed() + color.getGreen() + color.getBlue()) / 3);
                histogram[rgbValue]++;
            }
        }
        double entropy = 0.0;
        for(int i = 0 ;i < 256 ; i++){
            if(histogram[i] > 0){
                double p = (double) histogram[i]/n;
                entropy -= p * Math.log(p) / Math.log(2);
            }
        }
        return entropy;
    }

    // Structural Similarity Index Method
    public static double structuralSimilarityIndex(){ 
        return 0;
    }

    public static double errorValue(QuadTree qt, int input){
        if(input == 1){
            return variance(qt);
        }
        else if (input == 2){
            return meanAbsoluteDeviation(qt);
        }
        else if (input == 3){
            return maxPixelDifference(qt);
        }
        else if (input == 4){
            return entropy(qt);
        }
        else if (input == 5){
            return 0.0;
        }
        return 0.0;
    }

}
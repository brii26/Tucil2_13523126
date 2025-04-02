import java.awt.Color;

class ErrorMeasurement{

    // Variance Method
    public static double[] specificVariance(QuadTree qt){
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
        return new double[] {variance_value[0]/n , variance_value[1]/n, variance_value[2]/n};
    }

    public static double variance(QuadTree qt){
        return (specificVariance(qt)[0] + specificVariance(qt)[1] + specificVariance(qt)[2]) / (3);
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

    // mean
    public static double meanFourValue(double a, double b, double c, double d){
        return (a + b + c + d) /4;
    }

    // covariance
    public static double[] covariance(QuadTree parent, QuadTree child1, QuadTree child2, QuadTree child3, QuadTree child4){
        double numerator[] = {0,0,0};
        int n = parent.getPixel();

        //child 1
        for ( int y = child1.startY() ; y < child1.endY(); y++){
            for ( int x = child1.startX() ; x < child1.endX(); x++){
                int pixelColorParent = Main.imageFile.getRGB(x,y);
                int pixelColorChild1 = Main.imageFile.getRGB(x,y);
                Color colorParent = new Color(pixelColorParent);
                Color colorChild1 = new Color(pixelColorChild1);

                numerator[0] += (colorParent.getRed() - parent.getRedIntensityValue()) * (colorChild1.getRed() - child1.getRedIntensityValue());
                numerator[1] += (colorParent.getGreen() - parent.getGreenIntensityValue()) * (colorChild1.getGreen() - child1.getGreenIntensityValue());
                numerator[2] += (colorParent.getBlue() - parent.getBlueIntensityValue()) * (colorChild1.getBlue() - child1.getBlueIntensityValue());
            }
        }

        //child 2
        for ( int y = child2.startY() ; y < child2.endY(); y++){
            for ( int x = child2.startX() ; x < child2.endX(); x++){
                int pixelColorParent = Main.imageFile.getRGB(x,y);
                int pixelColorChild2 = Main.imageFile.getRGB(x,y);
                Color colorParent = new Color(pixelColorParent);
                Color colorChild2 = new Color(pixelColorChild2);

                numerator[0] += (colorParent.getRed() - parent.getRedIntensityValue()) * (colorChild2.getRed() - child2.getRedIntensityValue());
                numerator[1] += (colorParent.getGreen() - parent.getGreenIntensityValue()) * (colorChild2.getGreen() - child2.getGreenIntensityValue());
                numerator[2] += (colorParent.getBlue() - parent.getBlueIntensityValue()) * (colorChild2.getBlue() - child2.getBlueIntensityValue());
            }
        }

        //child 3
        for ( int y = child3.startY() ; y < child3.endY(); y++){
            for ( int x = child3.startX() ; x < child3.endX(); x++){
                int pixelColorParent = Main.imageFile.getRGB(x,y);
                int pixelColorChild3 = Main.imageFile.getRGB(x,y);
                Color colorParent = new Color(pixelColorParent);
                Color colorChild3 = new Color(pixelColorChild3);

                numerator[0] += (colorParent.getRed() - parent.getRedIntensityValue()) * (colorChild3.getRed() - child3.getRedIntensityValue());
                numerator[1] += (colorParent.getGreen() - parent.getGreenIntensityValue()) * (colorChild3.getGreen() - child3.getGreenIntensityValue());
                numerator[2] += (colorParent.getBlue() - parent.getBlueIntensityValue()) * (colorChild3.getBlue() - child3.getBlueIntensityValue());
            }
        }

        //child 4
        for ( int y = child4.startY() ; y < child4.endY(); y++){
            for ( int x = child4.startX() ; x < child4.endX(); x++){
                int pixelColorParent = Main.imageFile.getRGB(x,y);
                int pixelColorChild4 = Main.imageFile.getRGB(x,y);
                Color colorParent = new Color(pixelColorParent);
                Color colorChild4 = new Color(pixelColorChild4);

                numerator[0] += (colorParent.getRed() - parent.getRedIntensityValue()) * (colorChild4.getRed() - child4.getRedIntensityValue());
                numerator[1] += (colorParent.getGreen() - parent.getGreenIntensityValue()) * (colorChild4.getGreen() - child4.getGreenIntensityValue());
                numerator[2] += (colorParent.getBlue() - parent.getBlueIntensityValue()) * (colorChild4.getBlue() - child4.getBlueIntensityValue());
            }
        }

        return new double[]{numerator[0]/n , numerator[1]/n , numerator[2]/n};
    }

    public static double structuralSimilarityIndex(QuadTree parent, QuadTree child1, QuadTree child2, QuadTree child3, QuadTree child4){
        double c1 = Math.pow(0.01 * 255, 2); // constant 1
        double c2 = Math.pow(0.03 * 255, 2); // constant 2
    
        // Means 
        double meanRedChild   = meanFourValue(child1.getRedIntensityValue(), child2.getRedIntensityValue(), child3.getRedIntensityValue(), child4.getRedIntensityValue());
        double meanGreenChild = meanFourValue(child1.getGreenIntensityValue(), child2.getGreenIntensityValue(), child3.getGreenIntensityValue(), child4.getGreenIntensityValue());
        double meanBlueChild  = meanFourValue(child1.getBlueIntensityValue(), child2.getBlueIntensityValue(), child3.getBlueIntensityValue(), child4.getBlueIntensityValue());
    
        double meanRedParent   = parent.getRedIntensityValue();
        double meanGreenParent = parent.getGreenIntensityValue();
        double meanBlueParent  = parent.getBlueIntensityValue();
    
        // covariances
        double[] cov = covariance(parent, child1, child2, child3, child4);
        double redCovariance   = cov[0];
        double greenCovariance = cov[1];
        double blueCovariance  = cov[2];
    
        // Children's variances
        double redChildVariance   = meanFourValue(specificVariance(child1)[0], specificVariance(child2)[0], specificVariance(child3)[0], specificVariance(child4)[0]);
        double greenChildVariance = meanFourValue(specificVariance(child1)[1], specificVariance(child2)[1], specificVariance(child3)[1], specificVariance(child4)[1]);
        double blueChildVariance  = meanFourValue(specificVariance(child1)[2], specificVariance(child2)[2], specificVariance(child3)[2], specificVariance(child4)[2]);
    
        // Parent's variances
        double redParentVariance   = specificVariance(parent)[0];
        double greenParentVariance = specificVariance(parent)[1];
        double blueParentVariance  = specificVariance(parent)[2];
    
        // Partial SSIM value
        double ssimRed = ((2 * meanRedChild * meanRedParent + c1) * (2 * redCovariance + c2)) / ((Math.pow(meanRedChild, 2) + Math.pow(meanRedParent, 2) + c1) * (redChildVariance + redParentVariance + c2));
        double ssimGreen = ((2 * meanGreenChild * meanGreenParent + c1) * (2 * greenCovariance + c2)) / ((Math.pow(meanGreenChild, 2) + Math.pow(meanGreenParent, 2) + c1) * (greenChildVariance + greenParentVariance + c2));
        double ssimBlue = ((2 * meanBlueChild * meanBlueParent + c1) * (2 * blueCovariance + c2)) / ((Math.pow(meanBlueChild, 2) + Math.pow(meanBlueParent, 2) + c1) * (blueChildVariance + blueParentVariance + c2));
    
        // SSIM value
        double ssimRGB = 0.2989 * ssimRed + 0.5787 * ssimGreen + 0.1140 * ssimBlue; // ITU-R BT.601 standard weight constants
    
        return ssimRGB;
    }

    // Universal method error measurement
    public static double errorValue(QuadTree qt, int input){
        if(input == 1) return variance(qt);
        else if (input == 2) return meanAbsoluteDeviation(qt);
        else if (input == 3) return maxPixelDifference(qt);
        else if (input == 4) return entropy(qt);
        else if (input == 5) return structuralSimilarityIndex(qt, qt.getNorthEast(), qt.getNorthWest(), qt.getSouthEast(), qt.getSouthWest());
        return -1;
    }

}
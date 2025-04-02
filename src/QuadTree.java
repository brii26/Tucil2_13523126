import java.awt.Color;
import java.awt.image.BufferedImage;

public class QuadTree {

    // Class Point
    public static class Point{
        private int x; 
        private int y;

        //Constructor for Point
        Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    // Class RGB
    public static class RGB{
        private int red;
        private int green;
        private int blue;

        //Constructor for RGB
        RGB(int r, int g, int b){
            this.red = r;
            this.green = g;
            this.blue = b;
        }
    }

    // Attributes
    private Point point;
    private int width;
    private int height;
    private RGB intensityValue; 
    private QuadTree northEast;
    private QuadTree northWest;
    private QuadTree southEast;
    private QuadTree southWest;

    // Constructor for QuadTree
    public QuadTree(int w , int h){
        this.width = w;
        this.height = h;
        setChild(null,null,null,null);
        this.setPoint((int) Math.floor(this.width/2.0), (int) Math.ceil(this.height/2.0));
        this.setIntensityValue();
    }

    // setter and getter
    public void setWidth(int width){
        this.width = width;
    }
    public int getWidth(){
        return this.width;
    }
    public void setHeight(int height){
        this.height = height;
    }
    public int getHeight(){
        return this.height;
    }
    public void setPoint(int x, int y){
        this.point = new Point(x,y);
    }
    public int getAxis(){
        return this.point.x;
    }
    public int getOrdinate(){
        return this.point.y;
    }
    public int getRedIntensityValue(){
        return this.intensityValue.red;
    }
    public int getGreenIntensityValue(){
        return this.intensityValue.green;
    }
    public int getBlueIntensityValue(){
        return this.intensityValue.blue;
    }
    public void setIntensityValue(){
        int redValue = 0;
        int greenValue = 0;
        int blueValue = 0;
        int n = this.getPixel();
        for (int y = this.startY(); y < this.endY(); y++){
            for ( int x = this.startX() ; x < this.endX() ; x++){
                int pixelColor = Main.imageFile.getRGB(x,y);
                Color color = new Color(pixelColor);
                redValue += color.getRed();
                greenValue += color.getGreen();
                blueValue += color.getBlue();
            }
        }
        int avg_red = redValue/n;
        int avg_blue = blueValue/n;
        int avg_green = greenValue/n;
        this.intensityValue = new RGB(avg_red, avg_green, avg_blue);
    }
    public QuadTree getNorthEast(){
        return this.northEast;
    }
    public QuadTree getNorthWest(){
        return this.northWest;
    }
    public QuadTree getSouthEast(){
        return this.southEast;
    }
    public QuadTree getSouthWest(){
        return this.southWest;
    }
    public void setChild(QuadTree ne, QuadTree nw, QuadTree se, QuadTree sw){
        this.northEast = ne;
        this.northWest = nw;
        this.southEast = se;
        this.southWest = sw;
    }

    // utility method
    public int getPixel(){
        return this.width * this.height;
    }
    public int getArea(){
        return this.getPixel();
    }
    public int startX(){
        return Math.max(this.getAxis() - (int) Math.floor(this.getWidth()/2.0),0);
    }
    public int startY(){
        return Math.max(this.getOrdinate() - (int) Math.ceil(this.getHeight()/2.0),0);
    }
    public int endX(){
        return Math.min(this.getAxis() + (int) Math.ceil(this.getWidth()/2.0), Main.imageFile.getWidth());
    }
    public int endY(){
        return Math.min(this.getOrdinate() + (int) Math.floor(this.getHeight()/2.0), Main.imageFile.getHeight());
    }
    public boolean isLeaf(){
        return this.northEast == null && this.northWest == null && this.southEast == null && this.southWest == null;
    }

    // Method to recreate new compressed QuadTree ( divide and conquer algo)
    public void compressQuadTree(int inputMode, double treshold, int blockSizeConstrain){ 

        // end recursion if pixel size can no longer be devided
        if(this.getWidth() < 2 || this.getHeight() < 2){
            return;
        }

        // subdivide
        int base_axis = this.getAxis();
        int base_ordinate = this.getOrdinate();

        int floor_width = (int) Math.floor(this.getWidth()/2.0);
        int ceil_width = (int) Math.ceil(this.getWidth()/2.0);
        int ceil_height = (int) Math.ceil(this.getHeight()/2.0);
        int floor_height = (int) Math.floor(this.getHeight()/2.0);

        int south_ordinate = base_ordinate + (int) Math.ceil(0.5 * floor_height);
        int north_ordinate = base_ordinate - (int) Math.floor(0.5 * ceil_height);
        int west_axis = base_axis - (int) Math.ceil(0.5 * floor_width);
        int east_axis = base_axis + (int) Math.floor(0.5 * ceil_width);

        QuadTree nw = new QuadTree(floor_width, ceil_height);
        nw.setPoint(west_axis, north_ordinate);
        nw.setIntensityValue();

        QuadTree ne = new QuadTree(ceil_width, ceil_height);
        ne.setPoint(east_axis, north_ordinate);
        ne.setIntensityValue();

        QuadTree se = new QuadTree(ceil_width, floor_height);
        se.setPoint(east_axis, south_ordinate);
        se.setIntensityValue();

        QuadTree sw = new QuadTree(floor_width, floor_height);
        sw.setPoint(west_axis, south_ordinate);
        sw.setIntensityValue();

        this.setChild(ne,nw,se,sw);
        
        // constrains
        if(inputMode != 5){
            if(nw.getArea() > blockSizeConstrain && ErrorMeasurement.errorValue(nw,inputMode) > treshold){
                nw.compressQuadTree(inputMode, treshold, blockSizeConstrain);
            }

            if(ne.getArea() > blockSizeConstrain && ErrorMeasurement.errorValue(ne,inputMode) > treshold){
                ne.compressQuadTree(inputMode, treshold, blockSizeConstrain);
            }

            if(se.getArea() > blockSizeConstrain && ErrorMeasurement.errorValue(se,inputMode) > treshold){
                se.compressQuadTree(inputMode, treshold, blockSizeConstrain);
            }

            if(sw.getArea() > blockSizeConstrain && ErrorMeasurement.errorValue(sw,inputMode) > treshold){
                sw.compressQuadTree(inputMode, treshold, blockSizeConstrain);
            }
        }
        else{ // SSIM method, slight different
            if(nw.getArea() > blockSizeConstrain && ErrorMeasurement.errorValue(this,inputMode) > treshold){
                nw.compressQuadTree(inputMode, treshold, blockSizeConstrain);
            }

            if(ne.getArea() > blockSizeConstrain && ErrorMeasurement.errorValue(this,inputMode) > treshold){
                ne.compressQuadTree(inputMode, treshold, blockSizeConstrain);
            }

            if(se.getArea() > blockSizeConstrain && ErrorMeasurement.errorValue(this,inputMode) > treshold){
                se.compressQuadTree(inputMode, treshold, blockSizeConstrain);
            }

            if(sw.getArea() > blockSizeConstrain && ErrorMeasurement.errorValue(this,inputMode) > treshold){
                sw.compressQuadTree(inputMode, treshold, blockSizeConstrain);
            }
        }
        return;
    }

    // Reconstruct QuadTree to image file after compression
    public void reconstructQuadTree(BufferedImage output) {
        if(this.isLeaf()){
            for(int y = this.startY(); y < this.endY(); y++){
                for(int x = this.startX(); x < this.endX(); x++){
                    Color newColor = new Color(this.getRedIntensityValue(), this.getGreenIntensityValue(), this.getBlueIntensityValue());
                    output.setRGB(x,y,newColor.getRGB());
                }
            }
            return;
        }
        else{
            if(this.northEast != null){
                this.northEast.reconstructQuadTree(output);
            }
            if(this.northWest != null){
                this.northWest.reconstructQuadTree(output);
            }
            if(this.southEast != null){
                this.southEast.reconstructQuadTree(output);
            }
            if(this.southWest != null){
                this.southWest.reconstructQuadTree(output);
            }
        }
    }

    // Maximum QuadTree depth
    public int getDepth(){
        if (this.isLeaf()){
            return 1;
        }
        else{
            int depthNE = (this.northEast == null) ? 0 : this.northEast.getDepth();
            int depthNW = (this.northWest == null) ? 0 : this.northWest.getDepth();
            int depthSE = (this.southEast == null) ? 0 : this.southEast.getDepth();
            int depthSW = (this.southWest == null) ? 0 : this.southWest.getDepth();
            return 1 + Math.max(depthNE, Math.max(depthNW, Math.max(depthSE, depthSW)));
        }
    }

    // Get total ammount of QuadTree nodes
    public int getNodes(){
        if(this.isLeaf()){
            return 1;
        }else{
            int count = 1;
            if(this.northEast != null) count+= this.northEast.getNodes();
            if(this.northWest != null) count+= this.northWest.getNodes();
            if(this.southEast != null) count+= this.southEast.getNodes();
            if(this.southWest != null) count+= this.southWest.getNodes();

            return count;
        }
    }
}
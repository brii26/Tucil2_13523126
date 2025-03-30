import java.awt.Color;

public class QuadTree {

    // Class Point
    public static class Point{
        public int x; 
        public int y;

        //Constructor for Point
        Point(int x, int y){
            this.x = x;
            this.y = y;
        }
    }

    // Class RGB
    public static class RGB{
        public int red;
        public int green;
        public int blue;

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
    QuadTree(int w, int h, QuadTree ne, QuadTree nw, QuadTree se, QuadTree sw){
        this.width = w;
        this.height = h;
        this.northEast = ne;
        this.northWest = nw;
        this.southEast = se;
        this.southWest = sw;
        setPoint(width/2, height/2);

        //set intensity value
        int axis = this.getAxis();
        int ordinate = this.getOrdinate();
        int redValue = 0;
        int greenValue = 0;
        int blueValue = 0;
        int n = this.width * this.height;
        for (int y = ordinate - height/2; y < ordinate + height/2; y++){
            for ( int x = axis - width/2 ; x < axis + width/2 ; x++){
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

        setIntensityValue (avg_red, avg_green, avg_blue);
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
        Point newPoint = new Point(x,y);
        this.point = newPoint;
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
    public void setIntensityValue(int red, int green , int blue){
        this.intensityValue.red = red;
        this.intensityValue.green = green;
        this.intensityValue.blue = blue;
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
    public int getPixel(){
        return this.width * this.height;
    }

    // // Method to recreate new compressed QuadTree
    // public static QuadTree compressedQuadTree(int input, int x_coordinate, int y_coordinate, int height, int width, double treshold, double minimumBlockSize){
    //     //construct main node
    //     Point initialPoint = new Point(width/2, height/2);
    //     QuadTree initialImage = new QuadTree(initialPoint, width, height, getAverage(x_coordinate,y_coordinate,width,height))
    // }

}

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
    private double width;
    private double height;
    private RGB intensityValue; 
    private QuadTree northEast;
    private QuadTree northWest;
    private QuadTree southEast;
    private QuadTree southWest;

    // Constructor for QuadTree
    QuadTree(Point p, double w, double h, RGB iv, QuadTree ne, QuadTree nw, QuadTree se, QuadTree sw){
        this.point = p;
        this.width = w;
        this.height = h;
        this.intensityValue = iv;
        this.northEast = ne;
        this.northWest = nw;
        this.southEast = se;
        this.southWest = sw;
    }


}

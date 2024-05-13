// 207404997
/*
This class contains methods for the 'Index2D' pixel object.
 */
public class Index2D implements Pixel2D{
    private int _x, _y;
    public Index2D() {this(0,0);}
    public Index2D(int x, int y) {_x=x;_y=y;}
    public Index2D(Pixel2D t) {this(t.getX(), t.getY());}
    @Override
    public int getX() {
        return _x;
    }
    @Override
    public int getY() {
        return _y;
    }

    /**
     * @param t
     * @return the Euclidean distance between the pixel and another pixel.
     */
    public double distance2D(Pixel2D t) {
        double ans;
        if(t==null){
            throw new RuntimeException();
        }
        // get the 'x', 'y' values of the pixels:
        int xLength = Math.abs(this._x-t.getX());
        int yLength = Math.abs(this._y-t.getY());
        // calculate the Euclidean distance between them:
        ans = Math.sqrt(xLength*xLength + yLength*yLength);
        return ans;
    }

    @Override
    public String toString() {
        return getX()+","+getY();
    }

    @Override
    /**
     * @param t
     * @return the equality to given parameter.
     */
    /*
    compares a pixel to an object; returns true if and only if the object is also a pixel,
    and the 2 pixels have the same x and y values:
     */
    public boolean equals(Object t) {
        boolean ans = false;
        // check if the object is of the type 'Index':
        if(t instanceof Index2D){
            // check if the 'x', 'y' values are equal:
            ans = (this._x==((Index2D) t).getX() &&
                    this._y==((Index2D) t).getY());
        }
        return ans;
    }
}

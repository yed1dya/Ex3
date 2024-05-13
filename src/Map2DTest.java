// 207404997
import org.junit.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class Map2DTest {

    public static final int[][] MYARR = {{-2,-2,-2,-2,-1},{-1,-1,-2,-1,-2},{-2,-2,-2,-1,-1},{-1,-2,-1,-1,-2}};
    public static final Map MYMAP = new Map(MYARR);

    @Test
    /**
     * tests that all the constructors return the same object:
     */
    public void testMapConstructors(){
        int[][] m = new int[10][10];
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                m[i][j] = 0;
            }
        }
        Map m1 = new Map(10);
        Map m2 = new Map(m);
        Map m3 = new Map(10, 10, 0);
        for(int i=0; i<10; i++){
            for(int j=0; j<10; j++){
                assertTrue(m1.getMap()[i][j]==m2.getMap()[i][j]
                        && m2.getMap()[i][j]==m3.getMap()[i][j]);
            }
        }
    }

    @Test
    /**
     * tests that getMap() returns the correct array:
     */
    public void testGetMap(){
        Map m1 = new Map(MYARR);
        int[][] arr1 = m1.getMap();
        for(int i=0; i<4; i++){
            for(int j=0; j<5; j++){
                assertEquals(arr1[i][j], MYARR[i][j]);
            }
        }
        arr1[2][2] = 9;
        assertNotEquals(arr1[2][2], MYARR[2][2]);
    }

    @Test
    /**
     * tests that getWidth() returns the correct width:
     */
    public void testGetWidth(){
        int w = MYMAP.getWidth();
        assertEquals(4, w);
    }

    @Test
    /**
     * tests that getHeight() returns the correct height:
     */
    public void testGetHeight(){
        int h = MYMAP.getHeight();
        assertEquals(5, h);
    }

    @Test
    /**
     * tests that getPixel(int x, int y) returns the correct value, on specific points and on the whole array
     */
    public void testGetPixelXY(){
        int v1 = MYMAP.getPixel(2, 3);
        assertEquals(-1, v1);
        int v2 = MYMAP.getPixel(1, 2);
        assertEquals(-2, v2);
        for(int i=0; i<MYARR.length; i++){
            for(int j=0; j<MYARR[0].length; j++){
                assertEquals(MYMAP.getPixel(i, j), MYARR[i][j]);
            }
        }
    }

    @Test
    /**
     * tests that getPixel(Pixel2D p) returns the correct value:
     */
    public void testGetPixelP(){
        Pixel2D p;
        int e, v;
        for(int i=0; i<MYARR.length; i++){
            for(int j=0; j<MYARR[0].length; j++){
                p = new Index2D(i, j);
                e = MYARR[i][j];
                v = MYMAP.getPixel(p);
                assertEquals(e, v);
            }
        }
    }

    @Test
    /**
     * tests that setPixel(int x, int y, int v) sets the pixel to the correct value:
     */
    public void testSetPixelXYV(){
        Map m1 = new Map(MYARR);
        assertNotEquals(0, m1.getPixel(3, 2));
        m1.setPixel(3, 2, 0);
        assertEquals(0, m1.getPixel(3, 2));
    }

    @Test
    /**
     * tests that setPixel(Pixel2D p, int v) sets the pixel to the correct value:
     */
    public void testSetPixelPV(){
        Map m1 = new Map(MYARR);
        Pixel2D p = new Index2D(3, 2);
        assertNotEquals(0, m1.getPixel(p));
        m1.setPixel(p, 0);
        assertEquals(0, m1.getPixel(p));
    }

    @Test
    /**
     * tests that isInside(Pixel2D p) returns the expected value:
     */
    public void testIsInside(){
        Pixel2D p1 = new Index2D(-1, 2);
        assertFalse(MYMAP.isInside(p1));
        Pixel2D p2 = new Index2D(2, -1);
        assertFalse(MYMAP.isInside(p2));
        Pixel2D p3 = new Index2D(0, 6);
        assertFalse(MYMAP.isInside(p3));
        Pixel2D p4 = new Index2D(5, 1);
        assertFalse(MYMAP.isInside(p4));
        Pixel2D p5 = new Index2D(4, 5);
        assertTrue(MYMAP.isInside(p5));
        Pixel2D p6 = new Index2D();
        assertTrue(MYMAP.isInside(p5));
    }

    @Test
    /**
     * tests the allDistance function, with cyclic and not cyclic maps:
     */
    public void testAllDistance(){
        // array to build maps with:
        int[][] arr1 = {{-1,-1,-1,-1,-2},{-1,-1,-2,-1,-1},{-1,-2,-1,-2,-1},{-1,-1,-2,-1,-1}};
        // expected arrays:
        int[][] exp2 = {{0, 1, 2, 3, -2},{1, 2, -2, 3, 2},{2, -2, -1, -2, 3},{1, 2, -2, 3, 2}};
        int[][] exp3 = {{2, 3, 3, 2, -2},{3, 4, -2, 3, 2},{2, -2, -1, -2, 1},{1, 2, -2, 1, 0}};
        int[][] exp4 = {{0, 1, 2, 3, -2},{1, 2, -2, 4, 5},{2, -2, -1, -2, 6},{3, 4, -2, 8, 7}};
        int[][] exp5 = {{7, 6, 5, 4, -2},{8, 7, -2, 3, 2},{9, -2, -1, -2, 1},{10, 11, -2, 1, 0}};
        // create points to use:
        Pixel2D p3 = new Index2D(0, 0), p4 = new Index2D(3, 4);
        // create map to use:
        Map2D map1 = new Map(arr1);
        // get allDistance maps (cyclic)
        Map2D map2 = map1.allDistance(p3, -2);
        Map2D map3 = map1.allDistance(p4, -2);
        // check that map is equal to exp:
        for(int i=0; i< arr1.length; i++){
            for(int j=0; j<arr1[0].length; j++){
                assertEquals(exp2[i][j], map2.getMap()[i][j]);
                assertEquals(exp3[i][j], map3.getMap()[i][j]);
            }
        }

        // set map to not cyclic:
        map1.setCyclic(false);
        // get allDistance maps (not cyclic)
        Map2D map4 = map1.allDistance(p3, -2);
        Map2D map5 = map1.allDistance(p4, -2);
        // check that map is equal to exp:
        for(int i=0; i< arr1.length; i++){
            for(int j=0; j<arr1[0].length; j++){
                assertEquals(exp4[i][j], map4.getMap()[i][j]);
                assertEquals(exp5[i][j], map5.getMap()[i][j]);
            }
        }
    }

    @Test
    /**
     * tests shortestPath, with cyclic and not cyclic maps:
     */
    public void testShortestPath(){
        // array to build maps with:
        int[][] arr1 = {{-1,-1,-1,-1,-2},
                        {-1,-1,-2,-1,-1},
                        {-1,-2,-1,-2,-1},
                        {-1,-1,-2,-1,-1}};
        // expected answers:
        int[] exp1 = {0,0, 0,1, 0,2, 0,3, 1,3, 1,4, 2,4, 3,4};
        int[] exp2 = {3,4, 2,4, 1,4, 1,3, 0,3, 0,2, 0,1, 0,0};
        int[] exp3 = {0,0, 3,0, 3,4};
        int[] exp4 = {3,4, 3,0, 0,0};
        // create points to use:
        Pixel2D p3 = new Index2D(0, 0), p4 = new Index2D(3, 4);
        // create map to use:
        Map2D map5 = new Map(arr1);
        // get shortestPath:
        Pixel2D[] ans3 = map5.shortestPath(p3, p4, -2);
        Pixel2D[] ans4 = map5.shortestPath(p4, p3, -2);
        // create copy arrays from ans, to compare with exp:
        int[] comp3 = new int[exp3.length];
        int[] comp4 = new int[exp3.length];
        for (int i=0; i<exp3.length; i+=2) {
            comp3[i] = ans3[i/2].getX();
            comp3[i+1] = ans3[i/2].getY();
            comp4[i] = ans4[i/2].getX();
            comp4[i+1] = ans4[i/2].getY();
        }
        assertArrayEquals(exp3, comp3);
        assertArrayEquals(exp4, comp4);
        // same tests as above, with map set to cyclic:
        map5.setCyclic(false);
        Pixel2D[] ans1 = map5.shortestPath(p3, p4, -2);
        Pixel2D[] ans2 = map5.shortestPath(p4, p3, -2);
        int[] comp1 = new int[exp1.length];
        int[] comp2 = new int[exp1.length];
        for (int i=0; i<exp1.length; i+=2) {
            comp1[i] = ans1[i/2].getX();
            comp1[i+1] = ans1[i/2].getY();
            comp2[i] = ans2[i/2].getX();
            comp2[i+1] = ans2[i/2].getY();
        }
        assertArrayEquals(exp1, comp1);
        assertArrayEquals(exp2, comp2);
    }

    @Test
    /**
     * tests shortestPath with a map that's already set with allDistance values:
     */
    public void testShortestPathPreset(){
        int[][] arr1 = {{ 5, 6, 7, 6, 5},
                        { 4,-2,-2,-2, 4},
                        { 3, 2, 1, 2, 3},
                        { 2, 1, 0, 1, 2},
                        {-2,-2, 1, 2,-2},
                        { 4, 3, 2, 3, 4}};
        Map2D map1 = new Map(arr1);
        map1.setCyclic(false);
        Pixel2D p1= new Index2D(0,0);
        Pixel2D p2= new Index2D(3,2);
        int[] exp1 = {3,2, 3,1, 3,0, 2,0, 1,0, 0,0};
        Pixel2D[] ans1 = map1.shortestPath(p2, p1, -2);
        int[] comp1 = new int[exp1.length];
        int[] comp2 = new int[exp1.length];
        for (int i=0; i<exp1.length; i+=2) {
            comp1[i] = ans1[i/2].getX();
            comp1[i+1] = ans1[i/2].getY();
            comp2[i] = ans1[i/2].getX();
            comp2[i+1] = ans1[i/2].getY();
        }
        assertArrayEquals(exp1, comp1);
        assertArrayEquals(exp1, comp2);
    }

    @Test
    /**
     * tests that nextPixel returns the expected results, with cyclic and non-cyclic maps:
     */
    public void testNextPixel(){
        // cyclic:
        int[][] arr1 = {{2,  3,  3,  2, -2},
                        {3,  4, -2,  3,  2},
                        {2, -2, -1, -2,  1},
                        {1,  2, -2,  1,  0}};
        Map2D map1 = new Map(arr1);
        Pixel2D p1 = new Index2D(0, 3);
        Pixel2D p2 = new Index2D(Map.nextPixel(map1, p1));
        Pixel2D p3 = new Index2D(3, 3);
        assertNotEquals(p1, p2);
        assertEquals(p2, p3);

        // non-cyclic:
        int[][] arr2 = {{7,  6,  5,  4, -2},
                        {8,  7, -2,  3,  2},
                        {9, -2, -1, -2,  1},
                        {10, 11,-2,  1,  0}};
        Map2D map2 = new Map(arr2);
        Pixel2D p20 = new Index2D(-2, -2);
        Pixel2D p21 = new Index2D(0, 3);
        Pixel2D p22 = new Index2D(Map.nextPixel(map2, p21));
        Pixel2D p23 = new Index2D(1, 3);
        Pixel2D p24 = new Index2D(2, 2);
        Pixel2D p25 = new Index2D(Map.nextPixel(map2, p24));
        assertNotEquals(p21, p22);
        assertEquals(p22, p23);
        assertEquals(p20, p25);

    }

    @Test
    /**
     * tests that shortestPath returns (-2, -2) if one of the points isn't on the map:
     */
    public void testShortestPathNotIn(){
        // array to build map with:
        int[][] arr1 = {{-1,-1,-1,-1,-2},{-1,-1,-2,-1,-1},{-1,-2,-1,-2,-1},{-1,-1,-2,-1,-1}};
        // create point to use:
        Pixel2D p1 = new Index2D(0, 0), p2 = new Index2D(-1, 0);
        // create map to use:
        Map2D map = new Map(arr1);
        // get shortestPath:
        Pixel2D[] ans1 = map.shortestPath(p1, p2, -2);
        Pixel2D[] ans2 = map.shortestPath(p2, p1, -2);
        assertNull(ans1);
        assertNull(ans2);
    }

    @Test
    /**
     * tests fill on an array which has an inaccessible spot;
     * tests that fills the array correctly and returns the expected value,
     * on 4non-cyclic and cyclic maps:
     */
    public void testFill(){
        int[][] arr = {{ -1,-1,-1,-1,-2},
                        {-1,-1,-2,-1,-1},
                        {-1,-2,-1,-2,-2},
                        {-1,-1,-2,-1,-1}};
        Pixel2D p = new Index2D(0, 0);

        // non-cyclic
        int[][] exp = {{ 6, 6, 6, 6, -2},
                        {6, 6, -2, 6, 6},
                        {6, -2, -1, -2, -2},
                        {6, 6, -2, -1, -1}};
        Map2D map = new Map(arr);
        map.setCyclic(false);
        int n = map.fill(p, 6);
        int[][] ans = map.getMap();
        for(int i=0; i<exp.length; i++){
            for(int j=0; j<exp[0].length; j++){
                assertEquals(exp[i][j], ans[i][j]);
            }
        }
        assertEquals(11, n);

        // cyclic:
        int[][] exp2 = {{ 6,  6,  6,  6, -2},
                         {6,  6, -2,  6,  6},
                         {6, -2, -1, -2, -2},
                         {6,  6, -2, 6, 6}};
        Map2D map2 = new Map(arr);
        map2.setCyclic(true);
        int n2 = map2.fill(p, 6);
        int[][] ans2 = map2.getMap();
        for(int i=0; i<exp2.length; i++){
            for(int j=0; j<exp2[0].length; j++){
                assertEquals(exp2[i][j], ans2[i][j]);
            }
        }
        assertEquals(13, n2);
    }

}

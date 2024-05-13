import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class Index2DTest {
    public static final double EPS = 0.001;

    @Test
    /**
     * tests that the function is symmetric:
     */
    public void testDistance2DSymmetry(){
        Index2D p1 = new Index2D(5, 8);
        Index2D p2 = new Index2D(2, 7);
        double d1 = p1.distance2D(p2);
        double d2 = p2.distance2D(p1);
        assertEquals(d1, d2, EPS);
    }

    @Test
    /**
     * tests that the function returns the same distance for
     * different points that are the same distance from p1:
     */
    public void testDistance2DSameDistance(){
        Index2D p1 = new Index2D(8, 5);
        Index2D p2 = new Index2D(0, 0);
        Index2D p3 = new Index2D(13, 13);
        double d1 = p1.distance2D(p2);
        double d2 = p1.distance2D(p3);
        assertEquals(d1, d2, EPS);
    }

    @Test
    /**
     * tests that the function can work with negative values:
     */
    public void testDistance2DNegative(){
        Index2D p1 = new Index2D(3, 2);
        Index2D p2 = new Index2D(8, 10);
        Index2D p3 = new Index2D(-5, -3);
        double d1 = p1.distance2D(p2);
        double d2 = p1.distance2D(p3);
        assertEquals(d1, d2, EPS);
    }

    @Test
    /**
     * tests that a pixel's distance from itself is zero:
     */
    public void testDistance2DSame2(){
        Index2D p1 = new Index2D(23, 19);
        Index2D p2 = new Index2D(23, 19);
        double d1 = p1.distance2D(p2);
        assertEquals(0, d1);
    }

    @Test
    /**
     * tests that if p2==null, function throws RuntimeException:
     */
    public void testDistance2DNull(){
        // NEEDS TO BE UPDATED!!!
        Index2D p1;
        p1 = null;
        Index2D p2 = new Index2D(23, 19);
        //double d1 = p1.distance2D(p2);
    }

    @Test
    /**
     * tests that function returns the correct distance:
     */
    public void testDistance2DActual(){
        Index2D p1 = new Index2D(0, 0);
        Index2D p2 = new Index2D(1, 1);
        double d1 = p1.distance2D(p2);
        assertEquals(Math.sqrt(2), d1, EPS);
        Index2D p3 = new Index2D(0, -1);
        Index2D p4 = new Index2D(1, 0);
        double d2 = p1.distance2D(p2);
        assertEquals(Math.sqrt(2), d1, EPS);
    }

    @Test
    /**
     * test that two pixels with the same values are considered equal,
     * with a few different examples:
     */
    public void testSameEqual(){
        Index2D p1 = new Index2D(8, 13);
        Index2D p2 = new Index2D(8, 13);
        assertTrue(p1.equals(p2));
        Index2D p3 = new Index2D(-13, 5);
        Index2D p4 = new Index2D(-13, 5);
        assertTrue(p3.equals(p4));
    }

    @Test
    /**
     * test that different pixels return false:
     */
    public void testDifferentEqual(){
        Index2D p1 = new Index2D(8, 13);
        Index2D p2 = new Index2D(-8, -13);
        assertFalse(p1.equals(p2));
        Index2D p3 = new Index2D(-13, 5);
        Index2D p4 = new Index2D(-13, -5);
        assertFalse(p3.equals(p4));
        Index2D p5 = new Index2D(8, 13);
        Index2D p6 = new Index2D(-8, 13);
        assertFalse(p5.equals(p6));
        Index2D p7 = new Index2D(13, -5);
        Index2D p8 = new Index2D(-13, 5);
        assertFalse(p7.equals(p8));
    }

    @Test
    /**
     * tests that different types return false:
     */
    public void testDifferentTypeEqual(){
        Index2D p1 = new Index2D(8, 13);
        int[] t1 = {8, 13};
        assertFalse(p1.equals(t1));
        Index2D p2 = new Index2D(0, 0);
        int[] t2 = {0, 0};
        assertFalse(p2.equals(t2));
    }

}

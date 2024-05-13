// 207404997
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import exe.ex3.game.Game;

import java.awt.*;
import java.util.Arrays;

public class Ex3AlgoTest {
    static Pixel2D p00 = new Index2D(0,0), p01 = new Index2D(0,1),
            p02 = new Index2D(0,2), p03 = new Index2D(0,3),
            p10 = new Index2D(1,0), p11 = new Index2D(1,1),
            p12 = new Index2D(1,2), p13 = new Index2D(1,3),
            p20 = new Index2D(2,0), p21 = new Index2D(2,1),
            p22 = new Index2D(2,2), p23 = new Index2D(2,3),
            p30 = new Index2D(3,0), p31 = new Index2D(3,1),
            p32 = new Index2D(3,2), p33 = new Index2D(3,3),
            p45 = new Index2D(4,5);

    int x = Game.getIntColor(Color.BLUE, 0), o = Game.getIntColor(Color.PINK, 0);
    int l = Game.getIntColor(Color.BLACK, 0), g = Game.getIntColor(Color.GREEN, 0);
    int blue = Game.getIntColor(Color.BLUE,0), green = Game.getIntColor(Color.GREEN,0),
    black = Game.getIntColor(Color.BLACK, 0), pink = Game.getIntColor(Color.PINK, 0);
    int[][] mapArr1 = {
            {x,x,x,x,o,x,o,x,x,x},
            {o,x,o,o,o,o,o,o,g,x},
            {g,x,o,x,x,x,x,x,o,x},
            {o,x,o,x,o,o,o,x,o,x},
            {o,o,o,x,x,o,x,x,o,o},
            {o,o,o,o,o,o,o,o,o,o},
            {o,x,o,o,x,o,o,o,x,o},
            {o,x,o,x,x,x,o,x,x,x},
            {o,x,o,o,g,x,o,x,g,o},
            {x,x,x,x,x,x,x,x,x,x}};

    int[][] mapArr2 = {
            {x,x,x,x,l,x,l,x,x,x},
            {l,x,l,l,l,l,l,l,l,x},
            {l,x,l,x,x,x,x,x,l,x},
            {l,x,l,x,l,l,l,x,l,x},
            {l,l,l,x,x,l,x,x,l,l},
            {o,o,o,o,o,o,o,o,o,o},
            {o,x,o,o,x,o,o,o,x,o},
            {o,x,o,x,x,x,o,x,x,x},
            {o,x,o,o,g,x,o,x,g,o},
            {x,x,x,x,x,x,x,x,x,x}};

    int[][] ghostsTwo = {
            {1, 10, 4, 1, 2},
            {1, 11, 2, 8, 2},
            {1, 12, 7, 6, 2},
            {0, 12, 4, 3, 2}};

    int[][] ghostsOne = {
            {1, 10, 4, 2, 1},
            {1, 11, 2, 8, 1},
            {1, 12, 7, 6, 1},
            {0, 12, 4, 3, 1}};

    int[][] ghostsZero = {
            {1, 10, 4, 2, 1},
            {1, 11, 2, 8, 1},
            {1, 12, 7, 6, 1},
            {0, 12, 4, 3, 1}};

    int[][] ghostsMinus = {
            {1, 10, 4, 2, -1},
            {1, 11, 2, 8, -1},
            {1, 12, 7, 6, -1},
            {0, 12, 4, 3, -1}};

    @Test
    /*
     * test the target function:
     * public Pixel2D target(int[][] mapArr, Pixel2D pacPos, int[][] ghosts, int obsColor, int food, int power, boolean cyclic){
     */
    public void testTarget(){
        Pixel2D p1452t = Ex3Algo.target(mapArr1, p45, ghostsTwo, blue, pink, green, true);
        Pixel2D e1452t = new Index2D(4,5); assertEquals(e1452t, p1452t);
        Pixel2D p1452f = Ex3Algo.target(mapArr1, p45, ghostsTwo, blue, pink, green, false);
        Pixel2D e1452f = new Index2D(8,8); assertEquals(e1452f, p1452f);
        Pixel2D p1451t = Ex3Algo.target(mapArr1, p45, ghostsOne, blue, pink, green, true);
        Pixel2D e1451t = new Index2D(4,5); assertEquals(e1451t, p1451t);
        Pixel2D p1451f = Ex3Algo.target(mapArr1, p45, ghostsOne, blue, pink, green, false);
        Pixel2D e1451f = new Index2D(8,8); assertEquals(e1451f, p1451f);
        Pixel2D p1450t = Ex3Algo.target(mapArr1, p45, ghostsZero, blue, pink, green, true);
        Pixel2D e1450t = new Index2D(4,5); assertEquals(e1450t, p1450t);
        Pixel2D p1450f = Ex3Algo.target(mapArr1, p45, ghostsZero, blue, pink, green, false);
        Pixel2D e1450f = new Index2D(8,8); assertEquals(e1450f, p1450f);
        Pixel2D p145mt = Ex3Algo.target(mapArr1, p45, ghostsMinus, blue, pink, green, true);
        Pixel2D e145mt = new Index2D(4,5); assertEquals(e145mt, p145mt);
        Pixel2D p145mf = Ex3Algo.target(mapArr1, p45, ghostsMinus, blue, pink, green, false);
        Pixel2D e145mf = new Index2D(8,8); assertEquals(e145mf, p145mf);
    }

    @Test
    /*
     * tests that playerPosition() returns the correct position
     */
    public void testPlayerPosition(){
        String s1="2,4", s2="a2,4", s3="23", s4="3,2,4";
        Pixel2D e1 = new Index2D(2,4);
        Pixel2D p1 = Ex3Algo.playerPosition(s1),
                p2 = Ex3Algo.playerPosition(s2),
                p3 = Ex3Algo.playerPosition(s3),
                p4 = Ex3Algo.playerPosition(s4);
        assertEquals(e1, p1);
        assertNull(p2);
        assertNull(p3);
        assertNull(p4);
    }

    @Test
    /*
     * test the isOnlyInts() function
     */
    public void testIsOnlyInts(){
        String[] s1 = { "1", "2", "a"};
        String[] s2 = { "1", "2", ""};
        String[] s3 = { "1", "2", " "};
        String[] s4 = { "1", "2", "."};
        String[] s5 = { "1", "2", "3"};
        assertFalse(Ex3Algo.isOnlyInts(s1));
        assertFalse(Ex3Algo.isOnlyInts(s2));
        assertFalse(Ex3Algo.isOnlyInts(s3));
        assertFalse(Ex3Algo.isOnlyInts(s4));
        assertTrue(Ex3Algo.isOnlyInts(s5));
    }

    @Test
    /*
     * test that the nextDir function returns the expected direction in cyclic mode
     */
    public void testNextDirCyclic(){
        int width = 4, height = 4;
        Pixel2D[] path1 = {p03, p33}; int dir1 = Ex3Algo.nextDir(path1, width, height); assertEquals(Game.LEFT, dir1);
        Pixel2D[] path2 = {p31, p01}; int dir2 = Ex3Algo.nextDir(path2, width, height); assertEquals(Game.RIGHT, dir2);
        Pixel2D[] path3 = {p20, p23}; int dir3 = Ex3Algo.nextDir(path3, width, height); assertEquals(Game.DOWN, dir3);
        Pixel2D[] path4 = {p13, p10}; int dir4 = Ex3Algo.nextDir(path4, width, height); assertEquals(Game.UP, dir4);
    }

    @Test
    /*
     * test that the nextDir function returns the expected direction in non-cyclic mode
     */
    public void testNextDirNonCyclic(){
        int width = 4, height = 4;
        Pixel2D[] path1 = {p13, p03}; int dir1 = Ex3Algo.nextDir(path1, width, height); assertEquals(Game.LEFT, dir1);
        Pixel2D[] path2 = {p11, p21}; int dir2 = Ex3Algo.nextDir(path2, width, height); assertEquals(Game.RIGHT, dir2);
        Pixel2D[] path3 = {p22, p21}; int dir3 = Ex3Algo.nextDir(path3, width, height); assertEquals(Game.DOWN, dir3);
        Pixel2D[] path4 = {p11, p12}; int dir4 = Ex3Algo.nextDir(path4, width, height); assertEquals(Game.UP, dir4);
    }

}

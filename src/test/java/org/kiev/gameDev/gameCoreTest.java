package org.kiev.gameDev;

import junit.framework.TestCase;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Created by blabu on 03.07.16.
 */
public class gameCoreTest {
    private static gameCoreInterface testGame;
    @BeforeClass
    public static void init(){
        System.out.println("Before test running");
        testGame = new gameCore(3,8);
    }

    @Test
    public void compareTest(){
        TestCase.assertNotNull(testGame.getMatr());
        testGame.toUp();
        testGame.toDown();
        System.out.println();
        int[][] sourceMatr = testGame.getMatr();
        for (int[] val:sourceMatr) {
            for(int n:val) {
                System.out.print(n);
            }
            System.out.println();
        }
    }

}

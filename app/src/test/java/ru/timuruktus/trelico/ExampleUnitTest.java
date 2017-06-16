package ru.timuruktus.trelico;

import org.junit.Test;

import ru.timuruktus.trelico.Utils.GeoUtils;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test_distance_between_points_method(){
        //56.8402 60.62045
        System.out.println(GeoUtils.distanceBetweenPoints(60.60570, 56.83892, 60.62045, 56.84020));
    }
}
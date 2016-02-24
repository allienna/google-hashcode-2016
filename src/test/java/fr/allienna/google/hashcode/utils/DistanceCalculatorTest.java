package fr.allienna.google.hashcode.utils;

import fr.allienna.google.hashcode.model.Position;
import fr.allienna.google.hashcode.model.PositionTest;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public class DistanceCalculatorTest {

    @Test
    public void given_two_location_when_attemps_to_calculate_distance_should_return_distance() {
        Position a = new Position(1,1);
        Position b = new Position(3,3);
        assertEquals(3, DistanceCalculator.euclideanDistance(a,b));
    }
}
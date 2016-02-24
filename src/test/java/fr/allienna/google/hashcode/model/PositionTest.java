package fr.allienna.google.hashcode.model;

import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Created by aurelienallienne on 20/02/2016.
 */
public class PositionTest {

    @Test
    public void given_coordinates_when_setting_data_then_getter_return_values() {
        Position position = new Position();
        position.setX(12);
        position.setY(42);
        assertEquals(12, position.getX());
        assertEquals(42, position.getY());
    }
}

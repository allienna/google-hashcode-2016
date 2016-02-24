package fr.allienna.google.hashcode.utils;

import fr.allienna.google.hashcode.model.Position;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public class DistanceCalculator {

    private DistanceCalculator() {

    }

    public static int euclideanDistance(Position a, Position b) {
        return (int) Math.round(Math.sqrt(Math.pow((double) a.getX() - (double)b.getX(), (double)2) + Math.pow((double)a.getY() - (double)b.getY(), (double)2)));
    }
}

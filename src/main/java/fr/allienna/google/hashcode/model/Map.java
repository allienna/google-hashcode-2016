package fr.allienna.google.hashcode.model;

/**
 * Created by aurelienallienne on 12/02/2016.
 */
public class Map {

    private int row;
    private int column;

    public Map(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public String toString() {
        return "Map{" +
                "row=" + row +
                ", column=" + column +
                '}';
    }
}

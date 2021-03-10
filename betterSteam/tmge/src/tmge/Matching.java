package tmge;

public interface Matching {
    public abstract boolean rowIsFilled(int [] row);
    public abstract boolean columnIsFilled(int[] col);
    public abstract int matchAbove(int[] col, int start);
    public abstract int matchBelow(int[] col, int start);
    public abstract int matchLeft(int[] row, int start);
    public abstract int matchRight(int[] row, int start);
}

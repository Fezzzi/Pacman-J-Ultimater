package pacman_ultimater.project_base.custom_utils;

public class IntPair {
    public int Item1, Item2;

    public IntPair(int Item1, int Item2) {
        this.Item1 = Item1;
        this.Item2 = Item2;
    }

    public boolean equals(Object o) {
        if (!(o instanceof IntPair)) {
            return false;
        }

        return (((IntPair) o).Item1 == this.Item1 && ((IntPair) o).Item2 == Item2);
    }
}

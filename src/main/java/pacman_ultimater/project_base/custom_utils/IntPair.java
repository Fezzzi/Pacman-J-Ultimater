package pacman_ultimater.project_base.custom_utils;

/**
 * Basic tuple consisting of two int items.
 */
public class IntPair
{
    public int item1, item2;

    public IntPair(int item1, int item2) {
        this.item1 = item1;
        this.item2 = item2;
    }

    public boolean equals(Object o) {
        if (!(o instanceof IntPair)) {
            return false;
        }

        return (((IntPair) o).item1 == item1 && ((IntPair) o).item2 == item2);
    }
}

package pacman_ultimater.project_base.custom_utils;

/**
 * Tuple consisting of two generic items.
 */
public class Pair<T1, T2>
{
    public final T1 item1;
    public final T2 item2;

    public Pair(T1 item1, T2 item2)
    {
        this.item1 = item1;
        this.item2 = item2;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof Pair)) {
            return false;
        }
        return (((Pair)o).item1 == item1 && ((Pair)o).item2 == item2);
    }
}

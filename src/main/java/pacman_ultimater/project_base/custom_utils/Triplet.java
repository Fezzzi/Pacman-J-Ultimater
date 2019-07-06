package pacman_ultimater.project_base.custom_utils;

/**
 * Tuple consisting of three generic items.
 */
public class Triplet<T1, T2, T3>
{
    public final T1 item1;
    public final T2 item2;
    public final T3 item3;

    public Triplet(T1 item1, T2 item2, T3 item3)
    {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
    }

    public boolean equals(Object o)
    {
        if (!(o instanceof Triplet)) {
            return false;
        }
        return (((Triplet)o).item1 == item1 && ((Triplet)o).item2 == item2 && ((Triplet)o).item3 == item3);
    }
}

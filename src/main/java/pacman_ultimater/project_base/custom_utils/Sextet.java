package pacman_ultimater.project_base.custom_utils;

/**
 * Tuple consisting of five generic items.
 */
public class Sextet<T1, T2, T3, T4, T5, T6>
{
    public T1 item1;
    public T2 item2;
    public T3 item3;
    public T4 item4;
    public T5 item5;
    public T6 item6;

    public Sextet(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5, T6 item6) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
        this.item6 = item6;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Sextet)) {
            return false;
        }

        return (((Sextet) o).item1 == item1 && ((Sextet) o).item2 == item2
             && ((Sextet) o).item3 == item3 && ((Sextet) o).item4 == item4
            && ((Sextet) o).item5 == item5 && ((Sextet)o).item6 == item6);
    }
}

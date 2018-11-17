package pacman_ultimater.project_base.custom_utils;

public class Quintet<T1, T2, T3, T4, T5> {
    public T1 item1;
    public T2 item2;
    public T3 item3;
    public T4 item4;
    public T5 item5;

    public Quintet(T1 item1, T2 item2, T3 item3, T4 item4, T5 item5) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
        this.item5 = item5;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Quintet)) {
            return false;
        }

        return (((Quintet) o).item1 == item1 && ((Quintet) o).item2 == item2
             && ((Quintet) o).item3 == item3 && ((Quintet) o).item4 == item4 && ((Quintet) o).item5 == item5);
    }
}

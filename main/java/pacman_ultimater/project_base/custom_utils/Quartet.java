package pacman_ultimater.project_base.custom_utils;

public class Quartet<T1, T2, T3, T4> {
        public T1 item1;
        public T2 item2;
        public T3 item3;
        public T4 item4;

    public Quartet(T1 item1, T2 item2, T3 item3, T4 item4) {
        this.item1 = item1;
        this.item2 = item2;
        this.item3 = item3;
        this.item4 = item4;
    }

    public boolean equals(Object o) {
        if (!(o instanceof Quartet)) {
            return false;
        }

        return (((Quartet)o).item1 == item1 && ((Quartet)o).item2 == item2
             && ((Quartet)o).item3 == item3 && ((Quartet)o).item4 == item4);
    }
}

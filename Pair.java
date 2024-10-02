// Pair class with generics to use for (H-)Minimax implementation
public class Pair<F, S> {
    public F first;
    public S second;

    //constructor
    public Pair(F f, S s) {
        this.first = f;
        this.second = s;
    }
}

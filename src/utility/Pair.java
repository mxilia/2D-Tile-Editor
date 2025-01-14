package utility;

public class Pair<Key, Value> {
    public Key first;
    public Value second;

    public Pair(Key key, Value value) {
        this.first = key;
        this.second = value;
    }

    public Key getFirst() {
        return first;
    }

    public Value getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}

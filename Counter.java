public class Counter {
    int tally = 0;

    public void tick() {
        tally++;
    }

    public boolean check(int delay) {
        if (tally % delay == 0) {
            return true;
        }
        return false;
    }
}

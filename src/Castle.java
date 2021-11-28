public class Castle {
    private int health;
    private int combo;

    public Castle(int health) {
        this.health = health;
    }

    public void addCombo() {
        this.combo++;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public void setCombo(int combo) {
        this.combo = combo;
    }

    public int getCombo() {
        return combo;
    }

    public int getHealth() {
        return health;
    }
}

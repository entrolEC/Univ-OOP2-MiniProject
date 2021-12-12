import java.io.Serializable;

public class User implements Serializable {
    private String name;
    private int score;

    public User(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public String toString() {
        return name+ " - " + score;
    }
}

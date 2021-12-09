import java.awt.*;

public class Combo {
    private int x;
    private int y;
    private String combo;
    private Color color;

    public Combo(int x, int y, String combo, Color color) {
        this.x = x;
        this.y = y;
        this.combo = combo;
        this.color = color;
    }

    public void draw(Graphics2D g2) {
        g2.setColor(color);
        g2.setFont(new Font("Calibri", Font.BOLD, 18));
        g2.drawString(combo, x, y);
    }
}

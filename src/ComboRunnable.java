import java.awt.*;

public class ComboRunnable implements Runnable{
    private GamePanel.GameGroundPanel panel;
    private Combo combo;

    public ComboRunnable(Combo combo, GamePanel.GameGroundPanel panel) {
        this.combo = combo;
        this.panel = panel;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        panel.removeCombo(combo);
    }
}

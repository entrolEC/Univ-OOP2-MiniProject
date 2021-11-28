import javax.swing.*;
import java.awt.*;

public class Enemy extends JLabel {
    private TextSource textSource = new TextSource(); // 단어 벡터 생성
    private int atk; // 공격력
    private int delay; // 속도 딜레이
    private boolean attacking = false; // 공격 상태

    public Enemy(int x, int y, int atk, int delay) {
        setLocation(x, y);
        setText(textSource.get());
        this.atk = atk;
        this.delay = delay;
        this.setSize(100,20);
        this.setForeground(Color.GREEN);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
    }

    public int getDelay() {
        return delay;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean getAttacking() {
        return attacking;
    }
}

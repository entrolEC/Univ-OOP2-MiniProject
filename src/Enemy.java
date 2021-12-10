import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import java.awt.*;

public class Enemy extends JLabel {
    private TextSource textSource = new TextSource(); // 단어 벡터 생성
    private int atk; // 공격력
    private int atkDelay; // 공격 속도 딜레이
    private int delay; // 이동 속도 딜레이
    private boolean attacking = false; // 공격 상태
    private boolean alive = true; // 살아 있는지

    public Enemy(int x, int y, int atk, int atkDelay, int delay) {
        setLocation(x, y);
        setText(textSource.get());
        this.atk = atk;
        this.atkDelay = atkDelay;
        this.delay = delay;
        this.setSize(100,20);
        this.setForeground(Color.GREEN);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.magenta));


    }

    public Enemy(Enemy enemy) {
        setLocation(enemy.getX(), enemy.getY());
        setText(enemy.getText());
        this.atk = enemy.getAtk();
        this.atkDelay = enemy.getAtkDelay();
        this.delay = enemy.getDelay();
        this.setSize(100,20);
        this.setForeground(Color.GREEN);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.magenta));
    }

    public void draw(Graphics g) {
        //super.paintComponent(g);
        g.setColor(attacking ? Color.RED : Color.CYAN);
        if(isVisible())
            g.fillOval(getX(), getY()+24, 10, 10);
    }

    public int getDelay() {
        return delay;
    }

    public int getAtkDelay() {
        return atkDelay;
    }

    public int getAtk() {
        return atk;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public boolean getAttacking() {
        return attacking;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public boolean getAlive() {
        return alive;
    }
}

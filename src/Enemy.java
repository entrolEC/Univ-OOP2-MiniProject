import javax.swing.*;
import java.awt.*;

public class Enemy extends JLabel {
    private TextSource textSource = new TextSource(); // �ܾ� ���� ����
    private int atk; // ���ݷ�
    private int atkDelay; // ���� �ӵ� ������
    private int delay; // �̵� �ӵ� ������
    private boolean attacking = false; // ���� ����
    private boolean alive = true; // ��� �ִ���

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
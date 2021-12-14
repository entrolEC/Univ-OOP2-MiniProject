import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Enemy extends JLabel {
    private int atk; // 공격력
    private int atkDelay; // 공격 속도 딜레이
    private int delay; // 이동 속도 딜레이
    private boolean attacking = false; // 공격 상태
    private boolean alive = true; // 살아 있는지
    private BufferedImage img;

    public Enemy(int x, int y, int atk, int atkDelay, int delay, TextSource textSource, String imgSource) {
        try {
            img = ImageIO.read(new File(imgSource));
        } catch (IOException e) {
            System.out.println("enemy not found");
        }
        setLocation(x, y);
        setText(textSource.get());
        this.atk = atk;
        this.atkDelay = atkDelay;
        this.delay = delay;
        this.setSize(120,25);
        this.setFont(new Font("Gothic", Font.BOLD, 14));
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
        this.setSize(120,25);
        this.setFont(new Font("Gothic", Font.BOLD, 14));
        this.setForeground(Color.GREEN);
        this.setBackground(Color.BLACK);
        this.setOpaque(true);
        setBorder(BorderFactory.createLineBorder(Color.magenta));
    }

    public void draw(Graphics g) {
        g.setColor(attacking ? Color.RED : Color.CYAN);
        if(isVisible())
            g.drawImage(img, getX()-10, getY()+24, 30, 30, this);
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

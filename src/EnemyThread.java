import javax.swing.*;

public class EnemyThread extends Thread{
    private GamePanel panel;
    private Enemy enemy;
    //private int delay;
    private int respawnDelay;
    private int spawnDelay;

    public EnemyThread(Enemy enemy, GamePanel panel, int respawnDelay, int spawnDelay) {
        this.enemy = enemy;
        this.panel = panel;
        this.respawnDelay = respawnDelay;
        this.spawnDelay = spawnDelay;
        this.enemy.setVisible(false);
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (spawnDelay * ((double) (Math.random() * 4.0) + 8)));
            enemy.setVisible(true);

            while (true) {
                if (enemy.getAttacking()) {
                    panel.setCastleHealth(panel.getCastleHealth() - enemy.getAtk());
                    System.out.println(panel.getCastleHealth());
                    panel.updateCastleHealthLabel();
                    Thread.sleep(enemy.getAtkDelay());
                } else if (!enemy.getAttacking() && enemy.getY() >= panel.getHeight()-40) {
                    enemy.setAttacking(true);
//                    enemy.setVisible(false);
//                    Thread.sleep((long) (respawnDelay * ((double) (Math.random() * 4.0) + 8)));
//                    enemy.setLocation((int) (Math.random() * panel.getWidth()), 0);
//                    enemy.setVisible(true);
                } else {
                    enemy.setLocation(enemy.getX(), enemy.getY() + 1);
                    Thread.sleep(enemy.getDelay());
                }

                if(enemy.isVisible())
                    panel.repaint();

            }
        } catch(InterruptedException e) {
            return;
        }
    }
}

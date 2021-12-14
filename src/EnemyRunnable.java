public class EnemyRunnable implements Runnable{
    private GamePanel.GameGroundPanel panel;
    private Enemy enemy;
    private int respawnDelay;
    private int spawnDelay;

    public EnemyRunnable(Enemy enemy, GamePanel.GameGroundPanel panel, int respawnDelay, int spawnDelay) {
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
                if (enemy.getAttacking()) { // ���ݻ����϶�
                    if(enemy.getAlive()) {
                        panel.setCastleHealth(panel.getCastleHealth() - enemy.getAtk());
                    }
                    Thread.sleep(enemy.getAtkDelay());
                } else if (!enemy.getAttacking() && enemy.getY() >= panel.getHeight()-70) { // ������ ���� ��
                    enemy.setAttacking(true);
                } else { // �̵���
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

public class BeamEffectRunnable implements Runnable {
    private GamePanel.GameGroundPanel panel;

    public BeamEffectRunnable(GamePanel.GameGroundPanel panel) {
        this.panel = panel;
    }

    @Override
    public void run() {
        for(int i = 0; i < 51; i++) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            panel.subBeamTransparency();
        }
    }
}

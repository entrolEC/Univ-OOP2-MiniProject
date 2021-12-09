public class FiringRunnable implements Runnable{
    private Aim aim;
    public FiringRunnable(Aim aim) {
        this.aim = aim;
    }

    @Override
    public void run() {
        aim.setFiring(true);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            return;
        }
        aim.setFiring(false);
    }
}

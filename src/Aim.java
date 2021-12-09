import java.awt.*;

public class Aim {
    Enemy target = null;
    private boolean firing = false;

    public void setTarget(Enemy target) {
        this.target = target;
    }

    public Enemy getTarget(){
        return target;
    }

    public void setFiring(boolean firing) {
        this.firing = firing;
    }

    public void paintComponent(Graphics2D g2) {
        if(target == null) return;

        int width = 50;
        int height = 50;
        int x = target.getX() - width/2 + 4;
        int y = target.getY() - height/2 + 24 + 4;
        int centerX = x+width/2;
        int centerY = y+height/2;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, width, height);
        if(firing) {
            g2.setStroke(new BasicStroke(4));
            g2.setColor(new Color(220,0,0));
        }
        g2.drawOval(x+15, y+15, width/5*2, height/5*2);
        g2.setColor(new Color(220,0,0));
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x-4, centerY, centerX-4, centerY);
        g2.drawLine(centerX, y-4, centerX, centerY-4);
        g2.drawLine(x+width+4, centerY, centerX+4, centerY);
        g2.drawLine(centerX, y+height+4, centerX, centerY+4);
    }
}

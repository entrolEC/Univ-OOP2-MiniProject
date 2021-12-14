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

    public void drawBeam(Graphics g, double x1, double y1, double endX2, double endX3) {
        if(target == null) return;
        double x2 = x1-25;
        double y2 = y1;
        double x3 = x1+25;
        double y3 = y1;

        g.drawLine((int)endX2, (int)(0), (int)x2, (int)y2);
        g.drawLine((int)endX3, (int)(0), (int)x3, (int)y3);
    }

    public void paintComponent(Graphics2D g2) {
        if(target == null) return;

        int width = 50;
        int height = 50;
        int x = target.getX() - width/2 + 4;
        int y = target.getY() - height/2 + 34 + 4;
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

        g2.drawLine(x-4, centerY, centerX-4, centerY);
        g2.drawLine(centerX, y-4, centerX, centerY-4);
        g2.drawLine(x+width+4, centerY, centerX+4, centerY);
        g2.drawLine(centerX, y+height+4, centerX, centerY+4);
    }
}

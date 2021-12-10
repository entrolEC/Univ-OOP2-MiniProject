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

    public void drawBeam(Graphics g, double x1, double y1) {
        if(target == null) return;
        double x3 = x1+50;
        double y3 = y1;
        double x2 = target.getX();
        double y2 = target.getY()+29;
        double x4 = target.getX() + 10;
        double y4 = target.getY()+29;
        double centerX = target.getX()+5;
        double centerY = target.getY()+29;

        double endX1 = ((centerX-25-x1)/(centerY-y1))*(-y1)+x1;
        double endX2 = ((centerX+25-x3)/(centerY-y3))*(-y3)+x3;

        g.drawLine((int)endX1, 0, (int)x1, (int)y1);
        //g.drawLine(1000, 0, (int)x1, (int)y1+25);
        g.drawLine((int)endX2, 0, (int)x3, (int)y3);
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

        g2.drawLine(x-4, centerY, centerX-4, centerY);
        g2.drawLine(centerX, y-4, centerX, centerY-4);
        g2.drawLine(x+width+4, centerY, centerX+4, centerY);
        g2.drawLine(centerX, y+height+4, centerX, centerY+4);
    }
}

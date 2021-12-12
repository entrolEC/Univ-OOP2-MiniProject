import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class GameOverPanel extends JPanel {
    private JTextField textField = new JTextField();
    private RecordSource recordSource;
    private int score;

    public GameOverPanel(int score, RecordSource recordSource) {
        this.score = score;
        this.recordSource = recordSource;
        setLayout(null);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                JLabel scoreLabel = new JLabel("Score : " + score);
                scoreLabel.setFont(new Font("Gothic", Font.BOLD, 40));
                scoreLabel.setSize(250,60);
                scoreLabel.setLocation(getWidth()/2-100, getHeight()/2-180);
                add(scoreLabel);

                JLabel label = new JLabel("Please Type Your Name.");
                label.setFont(new Font("Gothic", Font.BOLD, 20));
                label.setSize(250,40);
                label.setLocation(getWidth()/2-100, getHeight()/2);
                add(label);

                textField.setSize(200, 30);
                textField.setLocation(getWidth()/2-100, getHeight()/2+50);
                textField.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        recordSource.addRecords(new User(textField.getText(), score));
                        ((GameFrame) GameOverPanel.this.getTopLevelAncestor()).reset();
                    }
                });
                add(textField);
            }
        });
    }
}

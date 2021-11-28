import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamePanel extends JPanel {
	private final int NUM = 10;
	private JTextField input = new JTextField(30);
	private ScorePanel scorePanel = null;
	private EditPanel editPanel = null;
	private Castle castle = new Castle(100);
	private JLabel castleHealthLabel = new JLabel(Integer.toString(castle.getHealth()));

	private final Enemy[] enemys = new Enemy[NUM];
	
	public GamePanel(ScorePanel scorePanel, EditPanel editPanel) {
		this.scorePanel = scorePanel;
		this.editPanel = editPanel;
		
		setLayout(new BorderLayout());
		add(new GameGroundPanel(), BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);

	}

	public void startGame() {


	}


	
	class GameGroundPanel extends JPanel {
		public GameGroundPanel() {
			setLayout(null);
			this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					makeEnemies();
					input.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JTextField t = (JTextField)(e.getSource());
							String inWord = t.getText();
							for(int i = 0; i < NUM; i++) {
								if(enemys[i].getText().equals(inWord)) {
									remove(enemys[i]);
									GameGroundPanel.this.revalidate();
									GameGroundPanel.this.repaint();
									scorePanel.increase();
									t.setText("");
									makeEnemy(i);
								}
							}
						}
					});
					GamePanel.this.removeComponentListener(this);
				}
			});

		}

		public void makeEnemy(int idx) {
			enemys[idx] = new Enemy((int)(Math.random()*(getWidth()-100)), 0, 1, 100, 50);
			add(enemys[idx]);
			EnemyThread th = new EnemyThread(enemys[idx], GamePanel.this, 3000, 500*idx);
			th.start();
		}


		public void makeEnemies() {
			for(int i = 0; i < NUM; i++) {
				makeEnemy(i);
			}
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for(int i = 0; i < NUM; i++) {
				//System.out.println(snows[i].getX() + " " + snows[i].getY());
				g.setColor(enemys[i].getAttacking() ? Color.RED : Color.CYAN);
				if(enemys[i].isVisible())
					g.fillOval(enemys[i].getX(), enemys[i].getY()+24, 10, 10);
			}

		}


	}
	
	class InputPanel extends JPanel {
		public InputPanel() {
			setLayout(new FlowLayout());
			this.setBackground(Color.CYAN);
			add(input);
			add(castleHealthLabel);
		}
	}

	public int getCastleHealth() {
		return castle.getHealth();
	}

	public void setCastleHealth(int health) {
		castle.setHealth(health);
	}

	public void updateCastleHealthLabel() {
		castleHealthLabel.setText(Integer.toString(getCastleHealth()));
	}

}

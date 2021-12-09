import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class GamePanel extends JPanel {
	private final int NUM = 10;
	private JTextField input = new JTextField(30);
	private ScorePanel scorePanel = null;
	private EditPanel editPanel = null;
	private GameGroundPanel gameGroundPanel = new GameGroundPanel();
	private Castle castle = new Castle(100);
	private JLabel castleHealthLabel = new JLabel(Integer.toString(castle.getHealth()));
	private Enemy target;

	private final Enemy[] enemys = new Enemy[NUM];
	
	public GamePanel(ScorePanel scorePanel, EditPanel editPanel) {
		this.scorePanel = scorePanel;
		this.editPanel = editPanel;
		
		setLayout(new BorderLayout());
		add(gameGroundPanel, BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);

		castleHealthLabel.setSize(100, 20);
		castleHealthLabel.setLocation(70, 100);
		scorePanel.add(castleHealthLabel);

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
							t.setText("");
							target = new Enemy(target);
							for(int i = 0; i < NUM; i++) {
								if(enemys[i].getText().equals(inWord)) {
									enemys[i].setAlive(false);
									remove(enemys[i]);
									GameGroundPanel.this.revalidate();
									GameGroundPanel.this.repaint();
									scorePanel.increase();
									makeEnemy(i);
								}
							}
						}
					});
					// ready
					GameGroundPanel.this.removeComponentListener(this);
				}
			});

		}



		public void makeEnemy(int idx) {
			enemys[idx] = new Enemy((int)(Math.random()*(getWidth()-100)), 0, 1, 300, 50);
			add(enemys[idx]);
			EnemyThread th = new EnemyThread(enemys[idx], this, 3000, 500*idx);
			th.start();
		}


		public void makeEnemies() {
			for(int i = 0; i < NUM; i++) {
				makeEnemy(i);
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

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for(int i = 0; i < NUM; i++) {
				if(enemys[i] == null) continue;

				g.setColor(enemys[i].getAttacking() ? Color.RED : Color.CYAN);
				if(enemys[i].isVisible())
					g.fillOval(enemys[i].getX(), enemys[i].getY()+24, 10, 10);
			}

			if(target!=null) {
				drawAim((Graphics2D) g);
			}
		}

		private void drawAim(Graphics2D g2) {

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
			g2.drawOval(x+15, y+15, width/5*2, height/5*2);
			g2.setColor(new Color(220,0,0));
			g2.setStroke(new BasicStroke(2));
			g2.drawLine(x-4, centerY, centerX-4, centerY);
			g2.drawLine(centerX, y-4, centerX, centerY-4);
			g2.drawLine(x+width+4, centerY, centerX+4, centerY);
			g2.drawLine(centerX, y+height+4, centerX, centerY+4);
		}
	}
	
	class InputPanel extends JPanel {
		public InputPanel() {
			setLayout(new FlowLayout());
			this.setBackground(Color.DARK_GRAY);

			input.getDocument().addDocumentListener(new DocumentListener() {
				@Override
				public void insertUpdate(DocumentEvent e) {
					Document document = e.getDocument();
					try {
						String inputText = document.getText(0, document.getLength()); // 우선 aim객체 없이 메인스레드에서 검색연선 수행
						searchTarget(inputText);
						System.out.println(document.getText(0, document.getLength()));
					} catch (BadLocationException ex) {
						ex.printStackTrace();
					}
				}

				@Override
				public void removeUpdate(DocumentEvent e) {

				}

				@Override
				public void changedUpdate(DocumentEvent e) {


				}
			});

			add(input);
		}

		public void searchTarget(String inputText) {
			for(Enemy enemy : enemys) {
				if(enemy == null) continue;
				if(enemy.getAlive() && enemy.isVisible() && enemy.getText().substring(0, Math.min(enemy.getText().length(), inputText.length())).equals(inputText)) {
					target = enemy;
					break;
				}
			}
		}

	}

}

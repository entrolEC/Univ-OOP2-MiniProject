import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;

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
	private JLabel scoreLabel = new JLabel("0");
	private Aim aim = new Aim();
	private int combo = 0;
	private int score = 0;

	private final Enemy[] enemys = new Enemy[NUM];
	private ArrayList<Combo> combos = new ArrayList<>();
	
	public GamePanel(ScorePanel scorePanel, EditPanel editPanel) {
		this.scorePanel = scorePanel;
		this.editPanel = editPanel;
		
		setLayout(new BorderLayout());
		add(gameGroundPanel, BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);

	}
	
	class GameGroundPanel extends JPanel {
		public GameGroundPanel() {
			setLayout(null);
			this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					System.out.println(getWidth()/2-25);
					System.out.println(getHeight());
					makeEnemies();
					input.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							JTextField t = (JTextField)(e.getSource());
							String inWord = t.getText();
							t.setText("");
							new Thread(new FiringRunnable(aim)).start();
							aim.setTarget(new Enemy(aim.getTarget()));

							boolean flag = false;
							for(int i = 0; i < NUM; i++) {
								if(enemys[i].getText().equals(inWord) && enemys[i].isVisible()) {
									flag = true;
									Combo c = new Combo(aim.getTarget().getX()+((int)(Math.random()*60))-30, aim.getTarget().getY()-20, Integer.toString(++combo)+" Combo!", Color.BLUE);
									combos.add(c);
									new Thread(new ComboRunnable(c, GameGroundPanel.this)).start();

									if(combo%1==0) {
										for(int j = 0; j < NUM; j++) {
											if(enemys[j].isVisible() && beamJudge(enemys[j].getX(), enemys[j].getY())) {
												enemys[j].setAlive(false);
												remove(enemys[j]);

												addScore(10);
												makeEnemy(j);
											}
										}
									} else {
										enemys[i].setAlive(false);
										remove(enemys[i]);
										addScore(10);
										makeEnemy(i);
									}
									GameGroundPanel.this.revalidate();
									GameGroundPanel.this.repaint();
									break;
								}
							}

							if(!flag) {
								combo = 0;
								Combo c = new Combo(aim.getTarget().getX()+((int)(Math.random()*60))-30, aim.getTarget().getY()-20, "Miss!", Color.RED);
								combos.add(c);
								new Thread(new ComboRunnable(c, GameGroundPanel.this)).start();
							}
						}
					});
					// ready
					GameGroundPanel.this.removeComponentListener(this);
				}
			});

		}

		public void addScore(int n) {
			score += n;
			scoreLabel.setText(Integer.toString(score));
		}

		public boolean beamJudge(int ex, int ey) {
			double x1 = getWidth()/2;
			double y1 = getHeight();
			double x2 = x1-25;
			double y2 = y1;
			double x3 = x1+25;
			double y3 = y1;
			double centerX = aim.getTarget().getX()+5;
			double centerY = aim.getTarget().getY()+29;

			double dx = centerX-x1;
			double dy = y1-centerY;

			double endX1 = ((centerX-x1)/(centerY-y1))*(-y1)+x1;
			double alpha = ((dy) / (Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2)))) * 25;
			double beta = ((dx) / (Math.sqrt(Math.pow(dx,2)+Math.pow(dy,2)))) * 25;

			double targetX2 = centerX - alpha;
			double targetY2 = centerY - beta;
			double targetX3 = centerX + alpha;
			double targetY3 = centerY + beta;
			if(targetY2 > y1) targetY2 = y1;
			if(targetY3 > y1) targetY3 = y1;

			double endX2 = ((targetX2-x2)/(targetY2-y2))*(ey-y2)+x2;
			double endX3 = ((targetX3-x3)/(targetY3-y3))*(ey-y3)+x3;

			if(centerY - beta > y1) return ex-100 <= endX3;
			if(centerY + beta > y1) return endX2 <= ex+100;
			return endX2 <= ex && ex <= endX3;

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
			castleHealthLabel.setText(Integer.toString(getCastleHealth()));
		}

		public void removeCombo(Combo combo) { combos.remove(combo); }

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			if((combo+1)%1==0) {
				aim.drawBeam(g, getWidth()/2, getHeight());
			}

			for(int i = 0; i < NUM; i++) {
				if(enemys[i] == null) continue;

				enemys[i].draw(g);
			}

			for(Combo c : combos) {
				c.draw((Graphics2D) g);
			}

			aim.paintComponent((Graphics2D) g);

			g.setColor(Color.ORANGE);
			g.fillOval(getWidth()/2-25, getHeight()-25, 50, 50);
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

			castleHealthLabel.setForeground(Color.lightGray);
			add(castleHealthLabel);
			add(input);
			JLabel l = new JLabel("점수 ");
			l.setForeground(Color.lightGray);
			add(l);
			scoreLabel.setForeground(Color.lightGray);
			add(scoreLabel);


		}

		public void searchTarget(String inputText) {
			for(Enemy enemy : enemys) {
				if(enemy == null) continue;
				if(enemy.getAlive() && enemy.isVisible() && enemy.getText().substring(0, Math.min(enemy.getText().length(), inputText.length())).equals(inputText)) {
					aim.setTarget(enemy);
					break;
				}
			}
		}

	}

}

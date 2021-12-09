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
	private Aim aim = new Aim();
	private int combo = 0;

	private final Enemy[] enemys = new Enemy[NUM];
	private ArrayList<Combo> combos = new ArrayList<>();
	
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
							new Thread(new FiringRunnable(aim)).start();
							aim.setTarget(new Enemy(aim.getTarget()));

							boolean flag = false;
							for(int i = 0; i < NUM; i++) {
								if(enemys[i].getText().equals(inWord)) {
									flag = true;
									Combo c = new Combo(aim.getTarget().getX()+((int)(Math.random()*60))-30, aim.getTarget().getY()-20, Integer.toString(++combo)+" Combo!", Color.BLUE);
									combos.add(c);
									new Thread(new ComboRunnable(c, GameGroundPanel.this)).start();
									enemys[i].setAlive(false);
									remove(enemys[i]);
									GameGroundPanel.this.revalidate();
									GameGroundPanel.this.repaint();
									scorePanel.increase();
									makeEnemy(i);
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

		public void removeCombo(Combo combo) { combos.remove(combo); }

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			for(int i = 0; i < NUM; i++) {
				if(enemys[i] == null) continue;

				enemys[i].draw(g);
			}

			for(Combo c : combos) {
				c.draw((Graphics2D) g);
			}

			aim.paintComponent((Graphics2D) g);
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
					aim.setTarget(enemy);
					break;
				}
			}
		}

	}

}

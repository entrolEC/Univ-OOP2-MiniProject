import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

public class GamePanel extends JPanel {
	private int NUM;
	private TextSource textSource;
	private JTextField input = new JTextField(30);
	private ScorePanel scorePanel = null;
	private EditPanel editPanel = null;
	private GameGroundPanel gameGroundPanel = new GameGroundPanel();
	private Castle castle = new Castle(100);
	private JLabel castleHealthLabel = new JLabel(Integer.toString(castle.getHealth()));
	private JLabel scoreLabel = new JLabel("0");
	private Aim aim = new Aim();
	private ImageIcon background;
	private int difficulty;
	private int combo = 0;
	private int score = 0;

	private Enemy[] enemys;
	private ArrayList<Combo> combos = new ArrayList<>();
	private ArrayList<Thread> threads = new ArrayList<>();
	
	public GamePanel(ScorePanel scorePanel, EditPanel editPanel, TextSource textSource, ImageIcon background, int difficulty) {
		this.scorePanel = scorePanel;
		this.editPanel = editPanel;
		this.textSource = textSource;
		this.background = background;
		this.difficulty = difficulty;
		this.NUM = 8 + difficulty * 2;
		enemys = new Enemy[NUM];
		
		setLayout(new BorderLayout());
		add(gameGroundPanel, BorderLayout.CENTER);
		add(new InputPanel(), BorderLayout.SOUTH);

	}
	
	class GameGroundPanel extends JPanel {
		private BufferedImage cannonImg;
		private String[] normalEnemyImgSources = {"normal_enemy1.png", "normal_enemy2.png", "normal_enemy3.png"};
		private double endX2;
		private double endX3;
		private int beamTransparency = 0;

		public GameGroundPanel() {
			setLayout(null);
			try {
				cannonImg = ImageIO.read(new File("cannon.png"));;
			} catch (IOException e) {
				System.out.println("cannonImg not found");
			}
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

									if(combo%5==0) {
										beamTransparency = 255;
										new Thread(new BeamEffectRunnable(GameGroundPanel.this)).start();
										for(int j = 0; j < NUM; j++) {
											if(enemys[j].isVisible() && beamJudge(enemys[j].getX(), enemys[j].getY()+30)) {
												enemys[j].setAlive(false);
												remove(enemys[j]);

												addScore(10 + combo);
												makeEnemy(j);
											}
										}
									} else {
										enemys[i].setAlive(false);
										remove(enemys[i]);
										addScore(10 + combo);
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
					GameGroundPanel.this.removeComponentListener(this);
				}
			});

		}

		public void subBeamTransparency () {
			beamTransparency -= 5;
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

			endX2 = ((targetX2-x2)/(targetY2-y2))*(ey-y2)+x2;
			endX3 = ((targetX3-x3)/(targetY3-y3))*(ey-y3)+x3;

			if(centerY - beta > y1) return ex-100 <= endX3;
			if(centerY + beta > y1) return endX2 <= ex+100;
			return endX2-10 <= ex && ex <= endX3+10;

		}

		public void makeEnemy(int idx) {
			enemys[idx] = new Enemy((int)(Math.random()*(getWidth()-100)), 0, 1, 300, 50-difficulty*5, textSource, normalEnemyImgSources[(int)(Math.random()*3)]);
			add(enemys[idx]);
			Thread th = new Thread(new EnemyRunnable(enemys[idx], this, 3000-difficulty*500, (400-difficulty*50)*idx));
			threads.add(th);
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
			if(health <= 0) {
				System.out.println("gameover");
				for(Thread th : threads)
					th.interrupt();
				((GameFrame)getTopLevelAncestor()).gameOver(score);
			}
			castle.setHealth(health);
			castleHealthLabel.setText(Integer.toString(getCastleHealth()));
		}

		public void removeCombo(Combo combo) { combos.remove(combo); }

		public void paintComponent(Graphics g) {
			super.paintComponent(g);

			g.drawImage(background.getImage(), 0, 0, null);

			g.drawLine(0, getHeight()-40, getWidth(), getHeight()-40);

			g.setColor(Color.red);
			g.fillRect(0, getHeight()-10, (int)(getWidth() * (double)(castle.getHealth())/100), getHeight());


			if(aim.getTarget() != null) beamJudge(0, 0);
			if((combo+1)%5==0) {
				aim.drawBeam(g, (int)(getWidth()/2), getHeight(), endX2, endX3);
			}
			g.setColor(new Color(255,0,0, beamTransparency));
			g.fillPolygon(new int[] {getWidth()/2-25, getWidth()/2+26, (int)endX3+1, (int)endX2}, new int[] {getHeight(), getHeight(), 0, 0}, 4);
			g.setColor(new Color(255,0,0, 255));

			for(int i = 0; i < NUM; i++) {
				if(enemys[i] == null) continue;
				enemys[i].draw(g);
			}

			for(Combo c : combos) {
				c.draw((Graphics2D) g);
			}

			aim.paintComponent((Graphics2D) g);

			if(aim.getTarget() == null) {
				g.drawImage(cannonImg, getWidth()/2-40, getHeight()-60, 80,80,this);
			} else {
				int dx = aim.getTarget().getX() - getWidth()/2;
				int dy = getHeight() - aim.getTarget().getY();

				System.out.println();
				double rotationRequired = Math.toRadians (-Math.atan2(dy, dx) * (180 / Math.PI) + 90);
				double locationX = cannonImg.getWidth() / 2;
				double locationY = cannonImg.getHeight() / 2;
				AffineTransform tx = AffineTransform.getRotateInstance(rotationRequired, locationX, locationY);
				AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);

				g.drawImage(op.filter(cannonImg, null), getWidth()/2-40, getHeight()-60, 80,80,this);
			}

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
						String inputText = document.getText(0, document.getLength());
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
			JLabel l = new JLabel("Á¡¼ö");
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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Enumeration;

import javax.swing.*;

public class GameFrame extends JFrame {
	private TextSource textSource = new TextSource("words.txt");
	private RecordSource recordSource = new RecordSource("records.dat");
	private ScorePanel scorePanel = new ScorePanel(recordSource);
	private EditPanel editPanel = new EditPanel(textSource);
	private GamePanel gamePanel;
	private MenuPanel menuPanel = new MenuPanel();
	private ImageIcon background = new ImageIcon("background.jpg");
	
	private JMenuItem startItem = new JMenuItem("start");
	private JMenuItem stopItem = new JMenuItem("stop");

	JSplitPane hPane = new JSplitPane();
	JSplitPane pPane = new JSplitPane();
	
	public GameFrame() {
		setTitle("Shooting Invaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);

		splitPane2();

		setResizable(false);
		setVisible(true);
	}

	public void reset() {
		gamePanel = null;
		getContentPane().removeAll();
		splitPane2();
		scorePanel.update();
		revalidate();
		repaint();
	}

	public void gameOver(int score) {
		gamePanel = null;
		getContentPane().removeAll();
		getContentPane().add(new GameOverPanel(score, recordSource));
		revalidate();
		repaint();
	}

	private void splitPane2() {

		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(550);
		hPane.setEnabled(false);
		hPane.setLeftComponent(menuPanel);


		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(300);
		pPane.setTopComponent(scorePanel);
		pPane.setBottomComponent(editPanel);
		hPane.setRightComponent(pPane);
	}

	private void makeMenu() {
		JMenuBar mBar = new JMenuBar();
		setJMenuBar(mBar);
		JMenu fileMenu = new JMenu("Game");
		fileMenu.add(startItem);
		fileMenu.add(stopItem);
		fileMenu.addSeparator();
		fileMenu.add(new JMenuItem("exit"));
		mBar.add(fileMenu);
	}

	class MenuPanel extends JPanel {
		private ImageIcon normalIcon;
		private ButtonGroup difficultyGroup = new ButtonGroup();

		public MenuPanel() {
			setLayout(null);

			ImageIcon originIcon = new ImageIcon("start.png");
			Image originImg = originIcon.getImage();
			Image changedImg= originImg.getScaledInstance(120, 55, Image.SCALE_SMOOTH );
			normalIcon = new ImageIcon(changedImg);

			this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					JLabel titleLabel = new JLabel("<html>SHOOTING<br/>INVADERS</html>");
					titleLabel.setFont(new Font("Gothic", Font.BOLD, 50)); titleLabel.setForeground(new Color(150, 208, 255));
					titleLabel.setSize(400, 200); titleLabel.setLocation(getWidth()/2-120, 0);
					MenuPanel.this.add(titleLabel);

					JButton startBtn = new JButton(normalIcon);
					startBtn.setOpaque(false);
					startBtn.setBorderPainted(false);
					startBtn.setSize(100, 50);
					startBtn.setLocation(getWidth()/2 - 50, getHeight()/2-50);
					startBtn.addActionListener(new StartAction());
					MenuPanel.this.add(startBtn);

					JLabel difficultyLabel = new JLabel("Select Game Difficulty");
					difficultyLabel.setFont(new Font("Gothic", Font.BOLD, 15));
					difficultyLabel.setSize(250, 30); difficultyLabel.setLocation(getWidth()/2-80, getHeight()/2+70);
					MenuPanel.this.add(difficultyLabel);

					JRadioButton easy = new JRadioButton("Easy");
					JRadioButton normal = new JRadioButton("Normal");
					JRadioButton hard = new JRadioButton("Hard");
					JRadioButton impossible = new JRadioButton("Impossible");

					difficultyGroup.add(easy);
					difficultyGroup.add(normal);
					difficultyGroup.add(hard);
					difficultyGroup.add(impossible);

					easy.setLocation(getWidth()/2 - 50, getHeight()/2+100); easy.setSize(100,30);
					normal.setLocation(getWidth()/2 - 50, getHeight()/2+130); normal.setSize(100,30);
					hard.setLocation(getWidth()/2 - 50, getHeight()/2+160); hard.setSize(100,30);
					impossible.setLocation(getWidth()/2 - 50, getHeight()/2+190); impossible.setSize(100,30);

					MenuPanel.this.add(easy);
					MenuPanel.this.add(normal);
					MenuPanel.this.add(hard);
					MenuPanel.this.add(impossible);

					// ready
					MenuPanel.this.removeComponentListener(this);
				}
			});


		}

		public void paintComponent(Graphics g) {
			g.drawImage(background.getImage(), 0, 0, null);
		}

		private class StartAction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GameFrame.this.remove(MenuPanel.this);
				getContentPane().removeAll();
				System.out.println(getSelectedDifficulty(difficultyGroup));
				gamePanel = new GamePanel(scorePanel, editPanel, textSource, background, getSelectedDifficulty(difficultyGroup));
				getContentPane().add(gamePanel);
				GameFrame.this.revalidate();
				GameFrame.this.repaint();


			}

			public int getSelectedDifficulty(ButtonGroup buttonGroup) {
				for (Enumeration<AbstractButton> buttons = buttonGroup.getElements(); buttons.hasMoreElements();) {
					AbstractButton button = buttons.nextElement();
					if (button.isSelected()) {
						String s = button.getText();
						if(s.equals("Easy")) {
							return 0;
						} else if (s.equals("Normal")) {
							return 1;
						} else if (s.equals("Hard")) {
							return 2;
						} else if (s.equals("Impossible")) {
							return 3;
						}
					}
				}
				return 1;
			}
		}
	}
}

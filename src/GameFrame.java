import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.*;

public class GameFrame extends JFrame {
	// 버튼을 위해 이미지 로딩 하여 아이콘 만들기
	private ScorePanel scorePanel = new ScorePanel();
	private EditPanel editPanel = new EditPanel();
	private GamePanel gamePanel = new GamePanel(scorePanel, editPanel);

	private ImageIcon pressedIcon = new ImageIcon("pressed.gif");
	private ImageIcon overIcon = new ImageIcon("over.gif");
	
	private JMenuItem startItem = new JMenuItem("start");
	private JMenuItem stopItem = new JMenuItem("stop");

	
	public GameFrame() {
		setTitle("타이핑 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(800, 600);

		add(new MenuPanel());

		//splitPane(); // JSplitPane을 생성하여 컨텐트팬의 CENTER에 부착
		//makeMenu();

		//makeToolBar();
		setResizable(false);
		setVisible(true);
	}

	private void splitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(550);
		hPane.setEnabled(false);
		hPane.setLeftComponent(gamePanel);

		JSplitPane pPane = new JSplitPane();
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
		
		//startItem.addActionListener(new StartAction());
	}

	class MenuPanel extends JPanel {
		private ImageIcon normalIcon;

		public MenuPanel() {
			setLayout(null);

			ImageIcon originIcon = new ImageIcon("start.png");
			Image originImg = originIcon.getImage();
			Image changedImg= originImg.getScaledInstance(120, 55, Image.SCALE_SMOOTH );
			normalIcon = new ImageIcon(changedImg);

			this.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent e) {
					JButton startBtn = new JButton(normalIcon);
					startBtn.setOpaque(false);
					startBtn.setBorderPainted(false);
					startBtn.setSize(100, 50);
					startBtn.setLocation(getWidth()/2 - 50, getHeight()/2);

					startBtn.addActionListener(new StartAction());

					MenuPanel.this.add(startBtn);

					// ready
					MenuPanel.this.removeComponentListener(this);
				}
			});


		}

		private class StartAction implements ActionListener {
			public void actionPerformed(ActionEvent e) {
				GameFrame.this.remove(MenuPanel.this);
				GameFrame.this.revalidate();
				GameFrame.this.repaint();
				splitPane();
				GameFrame.this.revalidate();
				GameFrame.this.repaint();


			}
		}


	}
	
}

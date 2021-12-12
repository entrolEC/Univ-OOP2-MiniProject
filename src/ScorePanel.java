import java.awt.*;

import javax.swing.*;

public class ScorePanel extends JPanel {
	private RecordSource recordSource;
	private JScrollPane scrollpane;
	private JList list;
	
	public ScorePanel(RecordSource recordSource) {
		this.setBackground(Color.GRAY);
		setLayout(null);

		this.recordSource = recordSource;
		this.list = new JList(recordSource.getRecords());
		this.scrollpane = new JScrollPane(list);
		this.scrollpane.setSize(200, 500);
		this.scrollpane.setLocation(10,50);

		JLabel label = new JLabel("Records");
		label.setFont(new Font("Gothic", Font.BOLD, 20));
		label.setSize(100, 50);
		label.setLocation(70, 0);

		add(label);
		add(scrollpane);
	}

	public void update() {
		list.setListData(recordSource.getRecords());
		scrollpane.setViewportView(list);
	}
}




import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import javax.swing.*;

public class EditPanel extends JPanel {
	private TextSource textSource;
	private JTextField edit = new JTextField(20);
	private JButton addButton = new JButton("add");
	private JButton saveButton = new JButton("delete");
	private JScrollPane scrollpane;
	private JList list;
	
	public EditPanel(TextSource textSource) {
		this.textSource = textSource;
		this.setBackground(Color.DARK_GRAY);
		this.setLayout(new FlowLayout());
		list = new JList(textSource.getTexts());
		scrollpane = new JScrollPane(list);

		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				textSource.addTexts(edit.getText());
				list.setListData(textSource.getTexts());
				scrollpane.setViewportView(list);
			}
		});

		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println(list.getSelectedValue());
				textSource.removeTexts((String)list.getSelectedValue());
				list.setListData(textSource.getTexts());
				scrollpane.setViewportView(list);
			}
		});

		add(scrollpane);
		add(edit);
		add(addButton);
		add(saveButton);
	}
}

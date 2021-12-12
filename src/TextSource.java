import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class TextSource {
	private String source;
	private Vector<String> texts = new Vector<String>();
	
	public TextSource(String source) { // ���Ͽ��� �б�
		this.source = source;
		openFile();
	}

	public void openFile() {
		try {
			Scanner scanner = new Scanner(new File(source));
			while(scanner.hasNextLine()) {
				texts.add(scanner.nextLine());
			}
		} catch (IOException e) {
			System.out.println("������ �����ϴ�.");
		}
	}
	
	public String get() {
		int index = (int)(Math.random()*texts.size());
		return texts.get(index);
	}

	public Vector<String> getTexts() {
		return texts;
	}

	public void removeTexts(String s) {
		texts.remove(s);
		saveTexts();
	}

	public void addTexts(String s) {
		texts.add(s);
		saveTexts();
	}

	public void saveTexts() {
		FileWriter fw;
		try{
			fw = new FileWriter(source, false); // ������ ������� �����
			for(String word : texts)
				fw.write(word+"\r\n");
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

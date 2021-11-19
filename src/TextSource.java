import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class TextSource {
	private Vector<String> v = new Vector<String>();
	
	public TextSource() { // ���Ͽ��� �б�
		openFile();
	}

	public void openFile() {
		try {
			Scanner scanner = new Scanner(new File("words.txt"));
			while(scanner.hasNextLine()) {
				v.add(scanner.nextLine());
			}
		} catch (IOException e) {
			System.out.println("������ �����ϴ�.");
		}



	}
	
	public String get() {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}
}

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

public class RecordSource {
    private String source;
    private Vector<User> records = new Vector<User>();

    public RecordSource(String source) { // ���Ͽ��� �б�
        this.source = source;
        openFile();
    }

    @SuppressWarnings("unchecked")
    public void openFile() {
        try {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream(source)); // ��ü��Ʈ�� ����
            records = (Vector<User>)in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("������ �����ϴ�.a");
        }
    }

    public Vector<User> getRecords() {
        return records;
    }

    public void removeRecords(User user) {
        records.remove(user);
        saveRecords();
    }

    public void addRecords(User user) {
        records.add(user);
        saveRecords();
    }

    public void saveRecords() {
        try {
            ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream(source)); // ��ü��Ʈ�� ����

            out.writeObject(records);

            out.close();
        } catch (IOException e) {
            System.out.println("������ �����ϴ�.");
        }
    }
}

import java.io.*;
import java.util.*;

public class RecordSource {
    private String source;
    private Vector<User> records = new Vector<User>();

    public RecordSource(String source) { // 파일에서 읽기
        this.source = source;
        openFile();
    }

    @SuppressWarnings("unchecked")
    public void openFile() {
        try {
            ObjectInputStream in = new ObjectInputStream (new FileInputStream(source)); // 객체스트림 생성
            records = (Vector<User>)in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("파일이 없습니다.a");
        }
        Collections.sort(records, (u1, u2) -> {
            if(u1.getScore() > u2.getScore()) {
                return -1;
            } else {
                return 1;
            }
        });
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
        Collections.sort(records, (u1, u2) -> {
            if(u1.getScore() > u2.getScore()) {
                return -1;
            } else {
                return 1;
            }
        });
        saveRecords();
    }

    public void saveRecords() {
        try {
            ObjectOutputStream out = new ObjectOutputStream (new FileOutputStream(source)); // 객체스트림 생성

            out.writeObject(records);

            out.close();
        } catch (IOException e) {
            System.out.println("파일이 없습니다.");
        }
    }

}

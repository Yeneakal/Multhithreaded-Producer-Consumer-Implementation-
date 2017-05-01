package ProdConsPART4;

public class TnameQueue {
	String[] buffer;
	int rear, front, count;
	
	public TnameQueue() {
		buffer = new String[4];
		rear = 0;
                front = 0;
                count = 0;
	}
	
	public void insert(String name) {
		buffer[rear] = name;
		rear = (rear + 1) % 4;
		count++;
	}
	
	public String remove() {
		String Tname;
		Tname = buffer[front];
		front = (front + 1) % 4;
		count--;
		return Tname;
	}
	
	public boolean empty() {
		return (count == 0);
	}
	
	public boolean full() {
		return (count == 4);
	}
}

package ProdConsPART4;

public class MessageQueue {
	int[] buffer;
	int rear, front, count;
	
	public MessageQueue() {
		buffer = new int[4];
		rear = 0;
                front = 0;
                count = 0;
	}
	
	public void insert(int val) {
		buffer[rear] = val;
		rear = (rear + 1) % 4;
		count++;
	}
	
	public int remove() {
		int msg;
		msg = buffer[front];
		front = (front + 1) % 4;
		count--;
		return msg;
	}
	
	public boolean empty() {
		return (count == 0);
	}
	
	public boolean full() {
		return (count == 4);
	}
}

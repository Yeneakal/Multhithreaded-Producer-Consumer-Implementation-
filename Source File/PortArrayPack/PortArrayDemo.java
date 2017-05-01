package PortArrayPack;

import java.util.concurrent.ThreadLocalRandom;
import GenericSynchPort.Message;

class Client extends Thread {
	int port_index;
	
	public Client(String name, int num) {
		super(name);
		port_index = num;
	}
	public void run() {
		Message<Integer> msg = new Message<Integer>();
		/* To simulate message production */
		try{ sleep(ThreadLocalRandom.current().nextInt(100, 600)); 
		} catch (InterruptedException e) {
			System.err.println("Interrupted Exception during sleep of"
					+ "thread " + Thread.currentThread().getName());
		}
		msg.data = ThreadLocalRandom.current().nextInt(0, 100);
		Server.pa.send(msg, port_index);
	}
}

class Server extends Thread {
	public static PortArray<Integer> pa;
	int[] selectedp1, selectedp2;
	int ports_num;
	int pa_size;
	
	public Server(String name, int[] sp1, int[] sp2, int n, int size) {
		super(name);
		pa = new PortArray<Integer>(size, true);
		selectedp1 = sp1;
		selectedp2 = sp2;
		ports_num = n;
		pa_size = size;
	}
	public void run() {
		Message<Integer> msg = new Message<Integer>();
		for (int i = 0; i < 5; i++) {
			msg = pa.receive(selectedp1, ports_num);
		}
             
		for (int i = 0; i < 5; i++) {
			msg = pa.receive(selectedp2, ports_num);
		}
                
	}
}

public class PortArrayDemo {
	
	public static void main(String[] args) {
		/* set of selected ports to receive message */
		int ports_set1[] = {1, 3, 5, 7, 9};
		int ports_set2[] = {0, 2, 4, 6, 8};
		/* clients sending ports */
		int[] indexes = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};
		int ports_num = 5;
		int numberOfSenders = 10;
		int ports_size = 10;
		Server S = new Server("Server", ports_set1, ports_set2,
				ports_num, ports_size);
		Client[] sender = new Client[numberOfSenders];
		for (int i = 0; i < numberOfSenders; i++) {
			sender[i] = new Client("Sender-" + i, indexes[i]);
			sender[i].start();
		}
		S.start();
	}
}
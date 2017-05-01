package ProdConsPART4;

import java.util.concurrent.ThreadLocalRandom;
import GenericSynchPort.SynchPort;
import GenericSynchPort.Message;

class ProducerA extends Thread {
	
	
	public ProducerA(String name) {
		super(name);
	}
	public void run() {
		Message<Integer> msg = new Message<Integer>();
		try{ sleep(ThreadLocalRandom.current().nextInt(100, 600)); 
		} catch (InterruptedException e) {
			System.err.println("Interrupted Exception during sleep of"
					 + Thread.currentThread().getName());
		}
		for (int i = 0; i < 5; i++) {
			msg.setContent(0); /* insert request */
			MailboxA.request.send(msg);
			msg = new Message<Integer>();
			msg.setContent(ThreadLocalRandom.current().nextInt(0, 100)); 
			msg.setThreadName(Thread.currentThread().getName()); 
			MailboxA.insert_prod.send(msg);
		}
	}
}

class ConsumerA extends Thread {
	public static SynchPort<Integer> in = new SynchPort<Integer>();
	
	public ConsumerA(String name) {
		super(name);
	}
	public void run() {
		Message<Integer> msg;
                
		for (int i = 0; i < 50; i++) {
			msg = new Message<Integer>();
			msg.setContent(1);  /* remove request */
			msg.setReplyport(in);
			MailboxA.request.send(msg);
			msg = in.receive();
			System.out.println("Consumer received " + msg.getContent() +
					" from : " + msg.getThreadName());
		}
	}
}

public class Part4ADemo {
	
	public static void main(String[] args) {
		MailboxA m = new MailboxA();
		ConsumerA cons = new ConsumerA("Consumer");
		ProducerA[] prods = new ProducerA[10];
		for (int i = 0; i < 10; i++) {
			prods[i] = new ProducerA("Producer-" + i);
		}
		m.start();
		cons.start();
		for (int i = 0; i < 10; i++) {
			prods[i].start();
		}
	}
}
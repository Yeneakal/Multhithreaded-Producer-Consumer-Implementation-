package ProdConsPART4;


import java.util.concurrent.ThreadLocalRandom;
import GenericSynchPort.SynchPort;
import GenericSynchPort.Message;

/* Producer thread */

class ProducerB extends Thread {
	int priority;
	public SynchPort<Integer> in;
	
	public ProducerB(String name, int prio) {
		super(name);
		priority = prio;
		in = new SynchPort<Integer>();
	}
        @Override
	public void run() {
		Message<Integer> msg = new Message<Integer>();
		try{ sleep(ThreadLocalRandom.current().nextInt(100, 600)); 
		} catch (InterruptedException e) {}
		for (int i = 0; i < 5; i++) {
			msg.setContent(0); /* insert request */
			msg.setThreadName(Thread.currentThread().getName());
			msg.setPriority(priority); 
			msg.setReplyport(in);
			MailboxB.request.send(msg);	/* send the request */
			msg = in.receive();		/* receive a reply from the Mailbox */
			msg = new Message<Integer>();
			msg.setContent(ThreadLocalRandom.current().nextInt(0, 100));;
			msg.setThreadName(Thread.currentThread().getName());
			msg.setReplyport(in);
			MailboxB.insert_prod.send(msg);	/* send the value */
		}
	}
}

/* Consumer thread */

class ConsumerB extends Thread {
	public static SynchPort<Integer> in = new SynchPort<Integer>();
	
	public ConsumerB(String name) {
		super(name);
	}
        @Override
	public void run() {
		Message<Integer> msg;
		for (int i = 0; i < 50; i++) {
			msg = new Message<Integer>();
			msg.setContent(1);  /* remove request */
			msg.setReplyport(in);
			MailboxB.request.send(msg);	/* send the request */
			msg = in.receive();	/* receive the value */
			System.out.println("Consumer received " + msg.getContent() +
					" from thread: " + msg.getThreadName());
			/* every 10 receives, wait some time*/
			if (i % 10 == 0) {
				try{ sleep(ThreadLocalRandom.current().nextInt(100, 500)); 
				} catch (InterruptedException e) {
					System.err.println("Interrupted Exception during sleep of"
							+ Thread.currentThread().getName());
				}
			}
		}
	}
}

public class Part4BDemo {
	
	public static void main(String[] args) {
                String[] tname_list = new String[10];
		ProducerB[] prods = new ProducerB[10];
		ConsumerB cons = new ConsumerB("Consumer");
		for (int i = 0; i < 10; i++) {
			prods[i] = new ProducerB("P" + i, i + 1);
			tname_list[i] = prods[i].getName();
		}
		MailboxB m = new MailboxB(true, tname_list);
		m.start();
		cons.start();
		for (int i = 0; i < 10; i++) {
			prods[i].start();
		}
	}
}
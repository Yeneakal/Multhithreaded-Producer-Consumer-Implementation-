package ProdConsPART4;

import GenericSynchPort.SynchPort;
import GenericSynchPort.Message;

public class MailboxA extends Thread{
	/* port for receiving service requests */
	public static SynchPort<Integer> request = new SynchPort<Integer>();
	/* port for receiving producers values */
	public static SynchPort<Integer> insert_prod = new SynchPort<Integer>();
	int waiting_prod;		/* numbers of waiting producers */
	boolean waiting_cons;           /* true if the consumer is waiting */
	MessageQueue buffer;			/* Mailbox buffer */
	TnameQueue tname_queue;		/* producers Thread name queue */
	
	public MailboxA() {
		super("Mailbox");
		waiting_prod = 0;
		waiting_cons = false;
		buffer = new MessageQueue();
		tname_queue = new TnameQueue();
		
		setDaemon(true);
	}
	
	public void run() {
		System.out.println("MailboxA Server started");
		Message<Integer> msg_in, msg_out;
		msg_in = new Message<Integer>();
		int value;
		String prod_tname;
		while(true) {
			msg_in = request.receive();
			switch(msg_in.getContent()) {
			case 0:	/* insert request */
				if (buffer.full()) {
					waiting_prod++;
				} else {
					msg_in = insert_prod.receive();
					buffer.insert(msg_in.getContent());
					tname_queue.insert(msg_in.getThreadName());
					if (waiting_cons) {
						value = buffer.remove();
						prod_tname = tname_queue.remove();
						msg_out = new Message<Integer>();
						msg_out.setContent(value); 
						msg_out.setThreadName(prod_tname); 
						waiting_cons = false;
						ConsumerA.in.send(msg_out);
					}
				}
				break;
			case 1: /* remove request */
				if (buffer.empty()) {
					waiting_cons = true;
				} else {
					value = buffer.remove();
					prod_tname= tname_queue.remove();
					msg_out = new Message<Integer>();
					msg_out.setContent(value);
					msg_out.setThreadName(prod_tname); 
					ConsumerA.in.send(msg_out);
					if (waiting_prod > 0) {
						waiting_prod--;
						msg_in = insert_prod.receive();
						buffer.insert(msg_in.getContent());
						tname_queue.insert(msg_in.getThreadName());
					}
				}
				break;
			default:
				break;
			}
		}
	}
}

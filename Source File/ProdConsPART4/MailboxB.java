package ProdConsPART4;


import java.util.List;
import java.util.ArrayList;
import GenericSynchPort.SynchPort;
import GenericSynchPort.Message;

public class MailboxB extends Thread{
	/* port for receiving service requests */
	public static SynchPort<Integer> request = new SynchPort<Integer>();
	/* port for receiving producers values */
	public static SynchPort<Integer> insert_prod = new SynchPort<Integer>();
	List<SynchPort<Integer>> prod_reply;	/* list of producers reply ports */
	boolean[] blocked = new boolean[10];	/* producers state */
	int waiting_prod;		/* number of waiting producers */
	boolean waiting_cons;           /* true if the consumer is waiting */
	MessageQueue buffer;			/* Mailbox buffer */
        TnameQueue tname_queue;         /* producers thread name queue */
	boolean enableDemo;		/* enables testing */
	String[] prod_name_list;	/* Thread name list for testing */
	
	public MailboxB() {
		super("Mailbox");
		SynchPort<Integer> sp = new SynchPort<Integer>();
		prod_reply = new ArrayList<SynchPort<Integer>>();
		waiting_prod = 0;
		waiting_cons = false;
		for (int i = 0; i < 10; i++) {
			blocked[i] = false;
			/* we add empty ports to the list */
			prod_reply.add(sp);
		}
		buffer = new MessageQueue();
                tname_queue = new TnameQueue();
		enableDemo = false;
		prod_name_list = null;
		
		setDaemon(true);
	}
	
	public MailboxB(boolean demo, String[] tname) {
		super("Mailbox");
		SynchPort<Integer> sp = new SynchPort<Integer>();
		prod_reply = new ArrayList<SynchPort<Integer>>();
		waiting_prod = 0;
		waiting_cons = false;
		for (int i = 0; i < 10; i++) {
			blocked[i] = false;
			prod_reply.add(sp);
		}
		buffer = new MessageQueue();
                tname_queue = new TnameQueue();
		enableDemo = demo;
		prod_name_list = tname;
		
		setDaemon(true);
	}
	
        @Override
	public void run() {
		Message<Integer> msg_in, msg_out;
		msg_in = new Message<Integer>();
		int value, i;
                String prod_name;
		String BlockedProdList;
		System.out.println("Mailbox Server started");
		while(true) {
			msg_in = request.receive();
			switch(msg_in.getContent()) {
			case 0:	/* insert request */
				if (buffer.full()) {	/* buffer full */
					prod_reply.set(msg_in.priority - 1, msg_in.getReplyport());
					waiting_prod++;
					blocked[msg_in.priority - 1] = true;
				} else {				/* buffer not full */
					msg_out = new Message<Integer>();
					msg_in.reply.send(msg_out);
					msg_in = insert_prod.receive();
					buffer.insert(msg_in.getContent());
					tname_queue.insert(msg_in.getThreadName());
					if (waiting_cons) {	/* consumer is waiting */
						value = buffer.remove();
                                                prod_name = tname_queue.remove();
						msg_out = new Message<Integer>();
						msg_out.setContent(value); 
                                                msg_out.setThreadName(prod_name);
						waiting_cons = false;
						ConsumerB.in.send(msg_out);
					}
				}
				break;
			case 1: /* remove request */
				if (buffer.empty()) {	/* buffer empty */
					waiting_cons = true;
					System.out.println("Consumer is waiting");
				} else {
					value = buffer.remove();
                                        prod_name = tname_queue.remove();
					msg_out = new Message<Integer>();
					msg_out.setContent(value);
                                        msg_out.setThreadName(prod_name);
					msg_in.reply.send(msg_out);
					if (waiting_prod > 0) {	/* producers are waiting */
						i = 0;
						if (enableDemo) {
							BlockedProdList = "Producers waiting: ";
							for (int j = 0; j < 10; j++) {
								if (blocked[j]) {
									BlockedProdList += prod_name_list[j] + " , ";
								}
							}
							System.out.println(BlockedProdList);
						}
						/* searching the highest priority producer */
						while(!blocked[i]) i++;
						waiting_prod--;
						blocked[i] = false;
						msg_out = new Message<Integer>();
						prod_reply.get(i).send(msg_out);
						msg_in = insert_prod.receive();
						buffer.insert(msg_in.getContent());
						tname_queue.insert(msg_in.getThreadName());
						if (enableDemo) System.out.println("Producer " + 
								msg_in.getThreadName() + " inserted value: " + msg_in.getContent());
					}
				}
				break;
			default:
				break;
			}
		}
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenericSynchPort;

import static java.lang.Thread.sleep;
import java.util.concurrent.ThreadLocalRandom;

public class PortSenderDemo extends Thread {
    public SynchPort<Integer> reply=new SynchPort<Integer> (true);
    Message<Integer > p = new Message<Integer >();
    Message<Integer > result;
    int value;
    PortReceiverDemo s;
    int numberOfSend_OP_PerSender;
    
    public PortSenderDemo(String n,PortReceiverDemo x, int y,int _numberOfSend_OP_PerSender ) 
    {   super(n);
        s=x; 
        value=y;
        numberOfSend_OP_PerSender =_numberOfSend_OP_PerSender;
    }
    
    @Override
    public void run() {
        int i=0;
        while(i < numberOfSend_OP_PerSender ){
            value = value+(i+1);
            p.setContent(value);
            p.setReplyport(reply);
            s.in.send(p);
            try{ sleep(ThreadLocalRandom.current().nextInt(100, 300)); 
			} catch (InterruptedException e) {
				System.err.println("Interrupted Exception during sleep of"
						+ "thread " + Thread.currentThread().getName());
			}
            i++;
        }
        System.out.println("Thread "+getName()+ ", Finished sending its Data"); 
        
        }
}

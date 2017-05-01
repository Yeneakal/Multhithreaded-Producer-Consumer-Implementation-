/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenericSynchPort;

import java.util.concurrent.ThreadLocalRandom;


public class PortReceiverDemo extends Thread {
    public SynchPort<Integer> in;
    SynchPort<Integer> reply;
    int value; 
    int result;
    int numberOfSend_OP_PerSender;
    int numberOfSenders;
    
    public PortReceiverDemo(String s,int _numberOfSend_OP_PerSender, int _numberOfSenders) {
        super(s);
       numberOfSend_OP_PerSender = _numberOfSend_OP_PerSender;
       numberOfSenders = _numberOfSenders; 
       in=new SynchPort<Integer>(true);
    }
    
      
    @Override
    public void run() {
        Message<Integer> msg;
        int  numberOfReceiveOP = numberOfSenders*numberOfSend_OP_PerSender;
        System.out.println("Thread " + getName() + ", receiver starts\n");
  
          int i = 0;
          while(i < numberOfReceiveOP){ 
            msg=in.receive();
            try{ sleep(ThreadLocalRandom.current().nextInt(100, 500)); 
			} catch (InterruptedException e) {
				System.err.println("Interrupted Exception during sleep of"
						+ "thread " + Thread.currentThread().getName());
			}
    
            ++i;
          }
          System.out.println("Thread "+getName()+ ", receiver terminates");
            
            
   
    }
}
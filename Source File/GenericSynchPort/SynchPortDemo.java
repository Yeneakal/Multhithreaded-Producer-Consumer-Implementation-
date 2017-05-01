/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenericSynchPort;


/**
 *
 * @author Solina
 */
public class SynchPortDemo {
     public static void main (String[] args) {
         
        int numberOfSenders = 3;
        int numberOfSend_OP_PerSender = 3;
       
        PortReceiverDemo s = new PortReceiverDemo("Receiver",numberOfSend_OP_PerSender, numberOfSenders);
        PortSenderDemo c1=new PortSenderDemo("Sender1",s,1,numberOfSend_OP_PerSender);
        PortSenderDemo c2=new PortSenderDemo("Sender2",s,10,numberOfSend_OP_PerSender);
        PortSenderDemo c3=new PortSenderDemo("Sender3",s,20,numberOfSend_OP_PerSender);
       
        s.start();
        c1.start();
        c2.start();  
        c3.start();
        
        try{
                c1.join();
        } catch (InterruptedException e) {
                System.err.println("Error joining on Sender 1");
        }
        
        try{
                c2.join();
        } catch (InterruptedException e) {
                System.err.println("Error joining on Sender 2");
        }
        
        try{
                c3.join();
        } catch (InterruptedException e) {
                System.err.println("Error joining on Sender 3");
        }
		
        try {
                s.join();
        } catch (InterruptedException e) {
                System.err.println("Error joining on Receiver");
        }
	
         System.out.println("Demo Completed Successfully!");
    }
}

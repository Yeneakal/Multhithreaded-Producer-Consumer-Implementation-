/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GenericSynchPort;
import FIFOSem.FairSem;

public class SynchPort <T> {
	Message<T> buffer;				/* port buffer */
	FairSem semSend, semRecv, semEvent;
	boolean enableDemo;				/* enables demonstration */
	
	public SynchPort() {
		semSend = new FairSem(1);	/* semaphore for sending threads */
		semRecv = new FairSem(0);	/* semaphore for receiving thread */
		semEvent = new FairSem(0);	/* semaphore for rendezvous synchronization between sender and receiver */
	}
	
	public SynchPort(boolean demo) {
		semSend = new FairSem(1);	/* semaphore for sending threads */
		semRecv = new FairSem(0);	/* semaphore for receiving thread */
		semEvent = new FairSem(0);	/* semaphore for rendezvous synchronization between sender and receiver */				
		enableDemo = demo;
	}
	
	public void send(Message<T> mes) {
		semSend.P();
		buffer = mes;
		if (enableDemo) {
			System.out.println("Send OP: " + Thread.currentThread().getName() +
					" sent message " + mes.getContent());
		}
		semRecv.V();
		semEvent.P();
                semSend.V();
	}
	
	public Message<T> receive() {
		Message<T> m = new Message<T>();	
		semRecv.P();
		m = buffer;
		if (enableDemo) {
			System.out.println("Receive OP: " + Thread.currentThread().getName() + 
					" received message " + m.getContent());
		}			
		semEvent.V();	
		return m;
	}
}

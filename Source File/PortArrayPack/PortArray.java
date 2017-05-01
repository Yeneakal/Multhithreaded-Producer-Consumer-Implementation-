package PortArrayPack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import FIFOSem.FairSem;
import GenericSynchPort.Message;
import GenericSynchPort.SynchPort;


public class PortArray<T> {
	int port_num;				/* total number of ports */
	boolean[] occupied;                     /* state of each port in the port array*/
	List<SynchPort<T>> ports;               /* list containing the ports used in the port array*/
	FairSem mutex;                          /* semaphore for mutual exclusion*/
        FairSem semAvailable;                   /* message availability checker semaphore for the server event*/
	boolean enableDemo;			/* enables demo */
	
        
        public PortArray(int n, boolean demo) {
		SynchPort<T> p;
		port_num = n;
		ports = new ArrayList<SynchPort<T>>();
		mutex = new FairSem(1);		
		semAvailable = new FairSem(0);	
		occupied = new boolean[port_num];
		for (int i = 0; i < port_num; i++) {
			occupied[i] = false;
			p = new SynchPort<T>();
			ports.add(p);
		}
		enableDemo = demo;
	}
	
	public void send(Message<T> m, int n) {
		
                if (n >= port_num || n < 0) {
                  
                    try {
                        throw new PortNotAvailable();
                    } catch (PortNotAvailable ex) { }
                }
		mutex.P();
		occupied[n]= true;
		semAvailable.V();
		mutex.V();
		if (enableDemo) System.out.println(Thread.currentThread().getName() +
				" sent message " + m.getContent() + " using port " + n);
		ports.get(n).send(m);
	}
	
	public Message<T> receive(int[] v, int n) {
		Message<T> mes = new Message<T>();
		int randIndex, j;
		boolean available = false;
		j = 0;
		if (n > port_num || n <= 0) {
                    try {
                        throw new PortNotAvailable();
                    } catch (PortNotAvailable ex) {
                      
                    }
		}
                try {
                mutex.P();
		while (!available) {
			/* generate a random initial index */
			randIndex = ThreadLocalRandom.current().nextInt(0, port_num);
			j = randIndex;
			do {
				for (int i = 0; i < n && !available; i++) {
					if (j == v[i] && occupied[j]== true) available = true;
				}
				if (!available) j = (j + 1) % port_num;
			} while (j != randIndex && !available);
			if (!available) {
				/* if not avilable, wait the next message */
				mutex.V();
				if (enableDemo) System.out.println(Thread.currentThread().getName() +
						" waiting for a message");
				semAvailable.P();
				mutex.P();
			}
		}
		/* If message avilable on port */
		occupied[j] = false;
		mutex.V();
		mes = ports.get(j).receive();
		mes.setIndex(j); 
		if (enableDemo) System.out.println(Thread.currentThread().getName() +
				" received message " + mes.getContent() + " from port " + mes.getIndex());
                
            } catch (Exception e) {
            }
		
		return mes;	
	}
}

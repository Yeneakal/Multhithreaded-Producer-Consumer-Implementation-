/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FIFOSem;


import java.util.LinkedList;
import java.util.ArrayList;
import java.util.List;

/*
 * Note: We don't know how many threads will use this
 * queue, and we don't even know how long the queue will be.
 * For those reasons to avoid any deadlock possibility we
 * using notifyAll() instead of notify().
 */

public class ThreadList {
	List<Long> queue;
	
	public ThreadList() {
		queue = new LinkedList<>();
	}
	
	public synchronized void addToQueue(long Tid) {
		while (queue.size() == Integer.MAX_VALUE) {
			try { wait();
			} catch (InterruptedException e) {}
		}
		queue.add(Tid);
		notifyAll();
	}
	
	public synchronized long removeFromQueue() {
		long Threadid;
		while (queue.isEmpty()) {
			try{ wait();
			} catch (InterruptedException e) {}
		}
		Threadid = queue.remove(0);
		notifyAll();
		return Threadid;
	}
	
	public synchronized long getFirst() {
		if (!queue.isEmpty()) {
			return queue.get(0);
		} else {
			return 0; 
		}
	}
	
	public synchronized boolean empty() {
		return (queue.isEmpty());
	}
}

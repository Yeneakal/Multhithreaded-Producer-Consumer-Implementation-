/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FIFOSem;

public class FairSem {
    
    ThreadList waitList;
    ThreadList awakenedList;
    private int value;
    boolean enableDemo;
	
	public FairSem(int value_)
	{
		this.value=value_;
		waitList=new ThreadList();
                awakenedList= new ThreadList();
	}
	
        public FairSem(int value_, boolean demo)
	{
		this.value=value_;
                enableDemo = demo;
		waitList=new ThreadList();
                awakenedList= new ThreadList();
	}
        
	public synchronized void P()
	{       
                if (enableDemo) {
                    System.out.println(Thread.currentThread().getName() + " begins P Operation\n");	
		}
                
                if (value == 0) {	
				
                            if (enableDemo){     
                                System.out.println("Thread blocked: "+ Thread.currentThread().getName() + "\n"); 
                            }
                            /* add the current thread to waiting queue if the semaphore is red */
                            waitList.addToQueue(Thread.currentThread().getId());
                            
                            /* To prevent spurious wakeup while loop check*/
                            while (awakenedList.getFirst()!= Thread.currentThread().getId()){ 
                           
                                try {                                     
                                    wait();
                                } catch (InterruptedException e) {

                                }
                            }
				awakenedList.removeFromQueue();
                                /* wake up threads blocked on wait if some other thread perform V operation */
				if (!(awakenedList.empty())) {	
					notifyAll();
				}
		} else value--;
                    		
                if (enableDemo){
			System.out.println(Thread.currentThread().getName() + " ends P Operation\n");	
		}
		
	}
	
	public synchronized void V(){
         
            if (!waitList.empty()) {
			/* removes the first thread from the waiting thread list
			 * and insert to awakened list */
			awakenedList.addToQueue(waitList.removeFromQueue());
			notifyAll(); 
		} else value++;
           
            if (enableDemo) System.out.println(Thread.currentThread().getName() + " executed V Operation\n");
            notifyAll();
		
	}
        
}       
	


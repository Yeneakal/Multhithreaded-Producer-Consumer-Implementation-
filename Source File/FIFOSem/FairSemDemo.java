/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package FIFOSem;

 class TestThreads extends Thread{
        
	FairSem sem;
	public TestThreads( FairSem f)
	{
	
		this.sem=f;
	}
	
    @Override
	public void run()
	{
		sem.P();
                
		System.out.println( Thread.currentThread().getName()+": is running \n");
                
		sem.V();
	}
    
}

public class FairSemDemo {
     public static FairSem sem = new FairSem(1, true);
     public static void main(String[] args) {
		TestThreads[] thread = new TestThreads[10];
		
		for (int i = 0; i < 3; i++) {
			thread[i] = new TestThreads(sem);
			thread[i].start();
		}
		
		for (int i = 0; i < 3; i++) {
			try{
				thread[i].join();
			} catch (InterruptedException e) {
				
			}	
		}
                System.out.println("Threads Completed Operation on Semaphore sem");
                
        
    }
}

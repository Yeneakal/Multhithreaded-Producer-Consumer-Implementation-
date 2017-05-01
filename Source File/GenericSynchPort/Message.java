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

public class Message<T> {
	public T data;
	public int index;		/* index field, used in part 3 */
        public String ThreadName;       /* Thread name used in part 4A
	//public long ThreadID;		/* TID field, used in part 4A */
	public int priority;            /* priority field used in part 4B */
	public SynchPort<T> reply;
	
	public Message() {
		data = null;
		index = -1;
                ThreadName = null ;
		//ThreadID = -1;
		priority = -1;
		reply = null;
	}
        //public Message(){}
        public Message(T _content){
        data = _content;
    }
    
    public Message(T _content, SynchPort<T>  SenderID){
        data = _content;
        reply =  SenderID;
    }
    public void setContent(T _content){
        data = _content;
    }
    public T getContent(){
        return data;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getThreadName() {
        return ThreadName;
    }

    public void setThreadName(String ThreadName) {
        this.ThreadName = ThreadName;
    }

   /* public long getThreadID() {
        return ThreadID;
    }

    public void setThreadID(long ThreadID) {
        this.ThreadID = ThreadID;
    }
*/
    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
    
    public void setReplyport(SynchPort<T>  SenderID){
        reply = SenderID;
    }
     
    public SynchPort<T> getReplyport(){
        return reply;
    }
}
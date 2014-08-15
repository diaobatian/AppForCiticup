/**
 * 
 */
package com.citi.mc.bean;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Administrator
 * @param <T>
 *
 */
public final class ConsultmsgList<T> extends CopyOnWriteArrayList<T> {
	private final int maxcapacity=30; 
	/**
	 * 
	 */
	public ConsultmsgList() {
		// TODO Auto-generated constructor stub
//		super<Object>();
//		super();
		
	}

	/**
	 * @param args
	 */
	@Override
	public boolean add(T e)
	{
		if(this.size()>maxcapacity)
		{
			
			for(Iterator iter =this.iterator(); iter.hasNext();)
			{
				
				T Consultmsg=(T) iter.next();
		
				
			}
			return true;
			
		}
		else
		{
			return super.add(e);
		}
		
	}

	

}

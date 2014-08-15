package com.citi.mc.db;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.citi.mc.bean.Expression;
import com.citi.mc.R;

public class GetChatExpression 
{
	private int[] imageIds = new int[35];
	List<Expression>experssions = new ArrayList<Expression>();
	
	public List<Expression> getExpressions() throws Exception
	{
		for(int i=0; i <35; i++)
		{
			if(i<10)
			{
				Field field = R.drawable.class.getDeclaredField("d0"+i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			}
			else if(i <35)
			{
				Field field = R.drawable.class.getDeclaredField("d"+i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			}
			Expression expression = new Expression();
			expression.setImage(imageIds[i]);
			experssions.add(expression);
			
		}
		
		return experssions;
		
	}
	
}

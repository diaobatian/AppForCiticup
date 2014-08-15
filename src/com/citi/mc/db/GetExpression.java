package com.citi.mc.db;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.citi.mc.bean.Expression;
import com.citi.mc.R;

public class GetExpression 
{
	private int[] imageIds = new int[107];
	List<Expression>experssions = new ArrayList<Expression>();
	
	public List<Expression> getExperssions() throws Exception
	{
		for(int i=0; i <105; i++)
		{
			if(i<10)
			{
				Field field = R.drawable.class.getDeclaredField("f00"+i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			}
			else if(i <100)
			{
				Field field = R.drawable.class.getDeclaredField("f0"+i);
				int resourceId = Integer.parseInt(field.get(null).toString());
				imageIds[i] = resourceId;
			}
			else
			{
				Field field = R.drawable.class.getDeclaredField("f"+i);
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

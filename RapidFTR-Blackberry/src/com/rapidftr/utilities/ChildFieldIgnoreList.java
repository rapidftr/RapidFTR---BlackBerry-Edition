package com.rapidftr.utilities;

public class ChildFieldIgnoreList {

	public static final String ignoreList[] = {"histories","_id"};
	public static boolean isInIgnoreList(String field)
	{
		for(int i = 0 ; i<ignoreList.length;i++)
		{
			if(field.equals(ignoreList[i]))
			{
				return true;
			}
		}
		return false;
	}
}

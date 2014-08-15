package com.citi.mc.utils;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneParser
{
	 public static boolean isPhoneNumberValid(String phoneNumber){  
         
	        boolean isValid = false;  
	        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";  
	        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";  
	        CharSequence inputStr = phoneNumber;  
	          
	        Pattern pattern = Pattern.compile(expression);  
	          
	        Matcher matcher = pattern.matcher(inputStr);  
	          
	        Pattern pattern2 = Pattern.compile(expression2);  
	          
	        Matcher matcher2 = pattern2.matcher(inputStr);  
	          
	        if(matcher.matches()||matcher2.matches()){  
	            isValid = true;  
	        }  
	          
	          
	        return isValid;  
	 }
	 
	 public static boolean EmailFormat(String email) {
		 Pattern pattern = Pattern
		 .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
		 Matcher mc = pattern.matcher(email);
		 return mc.matches();
		 }
}

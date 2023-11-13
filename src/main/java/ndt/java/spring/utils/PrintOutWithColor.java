package ndt.java.spring.utils;

import java.security.Key;

/*
 * parameter 1: message
 * parameter 2: color
 * 
 * */
public class PrintOutWithColor {
	
	public static void print(String message, ColorSysoutUtil color) {
		System.out.print(color + message + ColorSysoutUtil.RESET);
	}
	
	public static void println(String message, ColorSysoutUtil color) {
		System.out.println(color + message + ColorSysoutUtil.RESET);
	}
	
	public static void println(Byte[] message, ColorSysoutUtil color) {
		System.out.println(color + ""+ message + ColorSysoutUtil.RESET);
	}

	public static void println(Key key, ColorSysoutUtil color) {
		// TODO Auto-generated method stub
		System.out.println(color + ""+ key + ColorSysoutUtil.RESET);
	}
}

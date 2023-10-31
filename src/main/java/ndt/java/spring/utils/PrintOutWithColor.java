package ndt.java.spring.utils;

import java.security.Key;

public class PrintOutWithColor {
	
	public static void print(String message, ColorSysoutUtil color) {
		System.out.print(color + message + color.RESET);
	}
	
	public static void println(String message, ColorSysoutUtil color) {
		System.out.println(color + message + color.RESET);
	}
	
	public static void println(Byte[] message, ColorSysoutUtil color) {
		System.out.println(color + ""+ message + color.RESET);
	}

	public static void println(Key key, ColorSysoutUtil color) {
		// TODO Auto-generated method stub
		System.out.println(color + ""+ key + color.RESET);
	}
}

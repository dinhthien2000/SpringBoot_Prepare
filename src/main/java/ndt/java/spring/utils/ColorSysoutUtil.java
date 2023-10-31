package ndt.java.spring.utils;

public enum ColorSysoutUtil {
    //Color end string, color reset
	RESET("\033[0m"),
    // Bold
    BLACK_BOLD("\033[1;30m"),   // BLACK
    RED_BOLD("\033[1;31m"),     // RED
    GREEN_BOLD("\033[1;32m"),   // GREEN
    YELLOW_BOLD("\033[1;33m"),  // YELLOW
    BLUE_BOLD("\033[1;34m"),    // BLUE
    MAGENTA_BOLD("\033[1;35m"), // MAGENTA
    CYAN_BOLD("\033[1;36m"),    // CYAN
    WHITE_BOLD("\033[1;37m");

	private final String code;
	
	ColorSysoutUtil(String code) {
		// TODO Auto-generated constructor stub
		this.code = code;
	}   
	
	@Override
    public String toString() {
        return code;
    }
    
}

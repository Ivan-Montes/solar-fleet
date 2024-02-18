package dev.ime.solar_fleet.tool;


public class RegexPattern {

	private RegexPattern() {}
	
	public static final String NAME_BASIC = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\-\\.&,:]+";
	public static final String NAME_FULL = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\-\\.&,:]{1,50}";
	public static final String LASTNAME_BASIC = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\-\\.&,:]+";
	public static final String LASTNAME_FULL = "[a-zA-ZñÑáéíóúÁÉÍÓÚ\\s\\-\\.&,:]{1,50}";
	public static final String PAGE = "^[1-9]\\d*$";
	public static final String OBJECT_ID = "^[0-9a-fA-F]{24}$";
	
}

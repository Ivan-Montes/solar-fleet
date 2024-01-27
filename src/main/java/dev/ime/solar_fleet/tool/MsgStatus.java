package dev.ime.solar_fleet.tool;

public final class MsgStatus {

	private MsgStatus() {}
	
	public static final String INVALID_OBJECTID = "\"error\": \"Invalid ObjectId\"";
	public static final String ENTITY_DELETED = "\"info\": \"Entity deleted\"";
	public static final String ENTITY_NOT_DELETED = "\"error\": \"Entity NOT deleted\"";
	public static final String RESOURCE_NOT_FOUND = "\"error\": \"Resource not found\"";
	public static final String EMPTY_LIST = "\"info\": \"Empty List -_-\"";
	public static final String ERROR_WTF = "\"error\": \"ERROR WTF\""; 
	public static final String ERROR_DELETE_ASSOCIATED_ITEMS = "\"error\": \"Some items are still associated to this entity\""; 
}

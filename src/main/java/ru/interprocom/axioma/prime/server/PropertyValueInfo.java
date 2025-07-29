package ru.interprocom.axioma.prime.server;

import lombok.Getter;
//import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
//@NoArgsConstructor
public class PropertyValueInfo {
	private String propname;
	private String axitype;
	private boolean liverefresh;
	private boolean encrypted;
	private String domainid;
	private String securelevel;
	private long rowstamp;
	private String propvalue;
	private String servername;
	private String serverhost;
	private String encryptedvalue;
	private long axipropvalueRowstamp;
}

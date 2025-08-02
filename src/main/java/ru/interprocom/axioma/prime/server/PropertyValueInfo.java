package ru.interprocom.axioma.prime.server;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PropertyValueInfo {
	@EqualsAndHashCode.Include
	private String propname;
	private String axitype;
	private boolean liverefresh;
	private boolean encrypted;
	private String domainid;
	private String securelevel;
	@EqualsAndHashCode.Include
	private long rowstamp;
	private String propvalue;
	private String servername;
	private String serverhost;
	private String encryptedvalue;
	@EqualsAndHashCode.Include
	private long axipropvalueRowstamp;
}

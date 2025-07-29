package ru.interprocom.axioma.cache.dto.axiprop;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AxiPropCreateDTO {
	private long axipropid;
	private String propname;
	private String description;
	private String axitype;
	private boolean globalonly;
	private boolean instanceonly;
	private String axiomadefault;
	private boolean liverefresh;
	private boolean encrypted;
	private String domainid;
	private boolean nullsallowed;
	private String securelevel;
	private boolean userdefined;
	private boolean onlinechanges;
	private String changeby;
	private LocalDateTime changedate;
	private boolean masked;
	private int accesstype;
	private String valuerules;
	private long axipropvalueid;
	private String propvalue;
	private String servername;
	private String serverhost;
	private String encryptedvalue;
}

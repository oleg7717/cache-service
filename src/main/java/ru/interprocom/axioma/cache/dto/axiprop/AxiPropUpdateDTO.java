package ru.interprocom.axioma.cache.dto.axiprop;

import lombok.Getter;
import lombok.Setter;
import org.openapitools.jackson.nullable.JsonNullable;

import java.time.LocalDateTime;

@Setter
@Getter
public class AxiPropUpdateDTO {
	/*Поле ключа для сохранения в кэш должно быть без обертки JsonNullable*/
	private String propname;
	private JsonNullable<String> description;
	private JsonNullable<String> axitype;
	private JsonNullable<Boolean> globalonly;
	private JsonNullable<Boolean> instanceonly;
	private JsonNullable<String> axiomadefault;
	private JsonNullable<Boolean> liverefresh;
	private JsonNullable<Boolean> encrypted;
	private JsonNullable<String> domainid;
	private JsonNullable<Boolean> nullsallowed;
	private JsonNullable<String> securelevel;
	private JsonNullable<Boolean> userdefined;
	private JsonNullable<Boolean> onlinechanges;
	private JsonNullable<String> changeby;
	private JsonNullable<LocalDateTime> changedate;
	private JsonNullable<Boolean> masked;
	private JsonNullable<Integer> accesstype;
	private JsonNullable<String> valuerules;
	private JsonNullable<String> propvalue;
	private JsonNullable<String> servername;
	private JsonNullable<String> serverhost;
	private JsonNullable<String> encryptedvalue;
}

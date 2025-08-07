package ru.interprocom.axioma.cache.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "axipropvalue")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AxiPropValue implements BaseEntity, Serializable {
	@Id
	@Column(unique=true, name = "axipropvalueid")
	private long axipropvalueid;

	@NotNull
	@Column(unique=true)
	@EqualsAndHashCode.Include
	private String propname;

	@NotNull
	private String servername;

	private String serverhost;

	@EqualsAndHashCode.Include
	private String propvalue;

	private String encryptedvalue;

	@NotNull
	private String changeby;

	@NotNull
	private LocalDateTime changedate;

	private String accesstype;

	private long rowstamp;
}

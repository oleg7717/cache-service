package ru.interprocom.axioma.cache.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.type.NumericBooleanConverter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "axiprop")
//@SecondaryTable(name = "axipropvalue")
@Setter
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AxiProp implements BaseEntity, Serializable {
	@Id
	@Column(unique=true)
//	@GeneratedValue(strategy = SEQUENCE)
	@EqualsAndHashCode.Include
	private long axipropid;

	@NotNull
	@Column(unique=true/*, table = "axiprop", name = "propname"*/)
	@EqualsAndHashCode.Include
	private String propname;

	@NotNull
	private String description;

	@NotNull
	private String axitype;
	@Convert(converter = NumericBooleanConverter.class)

	@NotNull
	private boolean globalonly;

	@NotNull
	@Convert(converter = NumericBooleanConverter.class)
	private boolean instanceonly;

	private String axiomadefault;

	@NotNull
	@Convert(converter = NumericBooleanConverter.class)
	private boolean liverefresh;

	@Convert(converter = NumericBooleanConverter.class)
	@NotNull
	private boolean encrypted;

	private String domainid;

	@NotNull
	@Convert(converter = NumericBooleanConverter.class)
	private boolean nullsallowed;

	@NotNull
	private String securelevel;

	@NotNull
	@Convert(converter = NumericBooleanConverter.class)
	private boolean userdefined;

	@NotNull
	@Convert(converter = NumericBooleanConverter.class)
	private boolean onlinechanges;

	@NotNull
	private String changeby;

	@NotNull
	private LocalDateTime changedate;

	@NotNull
	@Convert(converter = NumericBooleanConverter.class)
	private boolean masked;

	private String accesstype;

	private String valuerules;

	@EqualsAndHashCode.Include
	private long rowstamp;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "propname", referencedColumnName = "propname", insertable = false, updatable = false)
	private AxiPropValue axipropvalue;
/*	@Column(table = "axipropvalue", name = "axipropvalueid")
	private long axipropvalueid;
	@Column(table = "axipropvalue", name = "propvalue")
	private String propvalue;
	@Column(table = "axipropvalue", name = "servername")
	private String servername;
	@Column(table = "axipropvalue", name = "serverhost")
	private String serverhost;
	@Column(table = "axipropvalue", name = "encryptedvalue")
	private String encryptedvalue;
	@Column(table = "axipropvalue", name = "rowstamp")
	private long valueRowstamp;*/
}

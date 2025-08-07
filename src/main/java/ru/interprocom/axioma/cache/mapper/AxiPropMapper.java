package ru.interprocom.axioma.cache.mapper;

import org.mapstruct.*;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropCreateDTO;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropUpdateDTO;
import ru.interprocom.axioma.prime.server.PropertyValueInfo;
import ru.interprocom.axioma.cache.model.AxiProp;

@Mapper(
		uses = {JsonNullableMapper.class, ReferenceMapper.class},
		nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
		componentModel = MappingConstants.ComponentModel.SPRING,
		unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public abstract class AxiPropMapper {
	@Mapping(source = "axipropvalue.propvalue", target = "propvalue")
	@Mapping(source = "axipropvalue.servername", target = "servername")
	@Mapping(source = "axipropvalue.serverhost", target = "serverhost")
	@Mapping(source = "axipropvalue.encryptedvalue", target = "encryptedvalue")
	@Mapping(source = "axipropvalue.rowstamp", target = "axipropvalueRowstamp")
	public abstract PropertyValueInfo map(AxiProp prop);

	@Mapping(source = "axipropvalue.axipropvalueid", target = "axipropvalue.axipropvalueid")
	@Mapping(source = "propname", target = "axipropvalue.propname")
	@Mapping(source = "axipropvalue.servername", target = "axipropvalue.servername")
	@Mapping(source = "axipropvalue.serverhost", target = "axipropvalue.serverhost")
	@Mapping(source = "axipropvalue.propvalue", target = "axipropvalue.propvalue")
	@Mapping(source = "axipropvalue.encryptedvalue", target = "axipropvalue.encryptedvalue")
	@Mapping(source = "changeby", target = "axipropvalue.changeby")
	@Mapping(source = "changedate", target = "axipropvalue.changedate")
	@Mapping(source = "accesstype", target = "axipropvalue.accesstype")
	public abstract AxiProp map(AxiPropCreateDTO propDTO);

	@Mapping(source = "propvalue", target = "axipropvalue.propvalue")
	@Mapping(source = "servername", target = "axipropvalue.servername")
	@Mapping(source = "serverhost", target = "axipropvalue.serverhost")
	@Mapping(source = "encryptedvalue", target = "axipropvalue.encryptedvalue")
	public abstract void update(@MappingTarget AxiProp axiProp, AxiPropUpdateDTO propDTO);
}

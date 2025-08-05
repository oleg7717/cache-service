package ru.interprocom.axioma.cache.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.interprocom.axioma.cache.annotation.DeletingCache;
import ru.interprocom.axioma.cache.annotation.KeyParam;
import ru.interprocom.axioma.cache.annotation.StoringCache;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropDeleteDTO;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropUpdateDTO;
import ru.interprocom.axioma.prime.server.PropertyValueInfo;
import ru.interprocom.axioma.cache.exception.ResourceNotFoundException;
import ru.interprocom.axioma.cache.mapper.AxiPropMapper;
import ru.interprocom.axioma.cache.model.AxiProp;
import ru.interprocom.axioma.cache.repository.AxiPropRepository;

import java.util.List;

@Service
public class AxiPropService {
	@Autowired
	AxiPropRepository axiPropRepository;

	@Autowired
	AxiPropMapper axiPropMapper;

	public ResponseEntity<List<AxiProp>> getProperties(int limit, int pageNumber) {
		Pageable sortedByPropname = PageRequest.of(pageNumber - 1, limit, Sort.by("propname"));
		Page<AxiProp> result = axiPropRepository.findAll(sortedByPropname);
		return ResponseEntity.ok()
				.header("X-Total-Count", String.valueOf(result.getTotalElements()))
				.header("X-Total-PageCount", String.valueOf(result.getTotalPages()))
				.body(result.stream().toList());
	}

	public AxiProp getByPropname(String propname) {
		return axiPropRepository.findByPropname(propname)
//				.map(axiPropMapper::map)
				.orElseThrow(() -> new ResourceNotFoundException("Property with propname " + propname + " not found"));
	}

	//ToDo реализовать создание свойств

	@StoringCache(cacheName = "axiprop", key = "propDto.propname")
	public PropertyValueInfo updateByPropname(@KeyParam AxiPropUpdateDTO propDto) {
		String propName = propDto.getPropname();
		AxiProp axiProp = axiPropRepository.findByPropname(propName)
				.orElseThrow(() -> new ResourceNotFoundException("Property with propname " + propName + " not found"));
		axiPropMapper.update(axiProp, propDto);
		axiPropRepository.save(axiProp);

		return axiPropMapper.map(axiProp);
	}

	@DeletingCache(cacheName = "axiprop", key = "propDto.propname")
	@Transactional
	public void deleteByPropname(@KeyParam AxiPropDeleteDTO propDTO) {
		axiPropRepository.deleteByPropname(propDTO.getPropname());
	}
}

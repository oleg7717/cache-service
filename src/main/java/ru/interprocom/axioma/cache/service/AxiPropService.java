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
import ru.interprocom.axioma.cache.core.AxiPropCache;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropCreateDTO;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropDeleteDTO;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropUpdateDTO;
import ru.interprocom.axioma.cache.exception.ResourceNotFoundException;
import ru.interprocom.axioma.cache.mapper.AxiPropMapper;
import ru.interprocom.axioma.cache.model.AxiProp;
import ru.interprocom.axioma.cache.repository.AxiPropRepository;
import ru.interprocom.axioma.prime.server.PropertyValueInfo;

import java.util.List;

@Service
public class AxiPropService {
	private final AxiPropCache axiPropCache;
	private final AxiPropRepository repository;
	private final AxiPropMapper mapper;

	@Autowired
	public AxiPropService(AxiPropCache axiPropCache, AxiPropRepository repository, AxiPropMapper mapper) {
		this.axiPropCache = axiPropCache;
		this.repository = repository;
		this.mapper = mapper;
	}

	public ResponseEntity<List<AxiProp>> getProperties(int limit, int pageNumber) {
		Pageable sortedByPropname = PageRequest.of(pageNumber - 1, limit, Sort.by("propname"));
		Page<AxiProp> result = repository.findAll(sortedByPropname);
		return ResponseEntity.ok()
				.header("X-Total-Count", String.valueOf(result.getTotalElements()))
				.header("X-Total-PageCount", String.valueOf(result.getTotalPages()))
				.body(result.stream().toList());
	}

	public AxiProp getByPropname(String propname) {
		return repository.findByPropname(propname)
				.orElseThrow(() -> new ResourceNotFoundException("Property with propname " + propname + " not found"));
	}

	@StoringCache(cacheName = "axiprop", key = "propDto.propname")
	public PropertyValueInfo createByPropname(@KeyParam AxiPropCreateDTO propDto) {
		AxiProp axiProp = repository.save(mapper.map(propDto));
		//Обновляем записи, так как при сохранении / изменении в БД срабатывает триггер заполняющий поле rowstamp
		repository.refresh(axiProp);
		return mapper.map(axiProp);
	}

	@StoringCache(cacheName = "axiprop", key = "propDto.propname")
	public PropertyValueInfo updateByPropname(@KeyParam AxiPropUpdateDTO propDto) {
		String propName = propDto.getPropname();
		AxiProp axiProp = repository.findByPropname(propName)
				.orElseThrow(() -> new ResourceNotFoundException("Property with propname " + propName + " not found"));
		mapper.update(axiProp, propDto);
		repository.save(axiProp);
		//Обновляем записи, так как при сохранении / изменении в БД срабатывает триггер заполняющий поле rowstamp
		repository.refresh(axiProp);

		return mapper.map(axiProp);
	}

	@Transactional
	@DeletingCache(cacheName = "axiprop", key = "propDto.propname")
	public void deleteByPropname(@KeyParam AxiPropDeleteDTO propDTO) {
		repository.deleteByPropname(propDTO.getPropname());
	}

	public void reloadAll() {
		axiPropCache.reloadAll();
	}

	public void reload(AxiProp propDTO) {
		axiPropCache.reload(propDTO.getPropname());
	}
}

package ru.interprocom.axioma.cache.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropCreateDTO;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropDeleteDTO;
import ru.interprocom.axioma.cache.dto.axiprop.AxiPropUpdateDTO;
import ru.interprocom.axioma.cache.model.AxiProp;
import ru.interprocom.axioma.cache.service.AxiPropService;
import ru.interprocom.axioma.prime.server.PropertyValueInfo;

import java.util.List;

@RestController
@RequestMapping("/axiprop")
public class AxiPropController {
	@Autowired
	AxiPropService axiPropService;

	@GetMapping(path = "")
	public ResponseEntity<List<AxiProp>> index(@RequestParam(defaultValue = "10") Integer limit,
	                                              @RequestParam(defaultValue = "1") Integer pageNumber) {
		return axiPropService.getProperties(limit, pageNumber);
	}

	@GetMapping(path = "/property")
	public AxiProp showProperty(@RequestBody AxiProp prop) {
		return axiPropService.getByPropname(prop.getPropname());
	}

	@PostMapping(path = "")
	public PropertyValueInfo createByPropname(@RequestBody AxiPropCreateDTO propDTO) {
		return axiPropService.createByPropname(propDTO);
	}

	@PutMapping(path = "")
	public PropertyValueInfo updateByPropname(@RequestBody AxiPropUpdateDTO propDTO) {
		return axiPropService.updateByPropname(propDTO);
	}

	@DeleteMapping(path = "")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void deleteByPropname(@RequestBody AxiPropDeleteDTO propDTO) {
		axiPropService.deleteByPropname(propDTO);
	}
}

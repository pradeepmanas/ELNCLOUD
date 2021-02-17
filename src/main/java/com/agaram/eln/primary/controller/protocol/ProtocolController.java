package com.agaram.eln.primary.controller.protocol;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.agaram.eln.primary.service.protocol.ProtocolService;

@RestController
@RequestMapping(value="/protocol", method=RequestMethod.POST)
public class ProtocolController {

	@Autowired
	ProtocolService ProtocolMasterService;
	
	@RequestMapping(value="/getProtocolMasterInit")
	protected Map<String, Object> getProtocolMasterInit(@RequestBody Map<String, Object> argObj){
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolMasterInit(argObj);
		return objMap;
	}
	
	@RequestMapping(value="/addProtocolMaster")
	protected Map<String, Object> addProtocolMaster(@RequestBody Map<String, Object> argObj){
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.addProtocolMaster(argObj);
		return objMap;
	}
	
	@RequestMapping(value="/deleteProtocolMaster")
	protected Map<String, Object> deleteProtocolMaster(@RequestBody Map<String, Object> argObj){
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.deleteProtocolMaster(argObj);
		return objMap;
	}
	
	
	
	@RequestMapping(value="/getProtocolMasterLst")
	protected Map<String, Object> getProtocolMasterLst(@RequestBody Map<String, Object> argObj){
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getLSProtocolMasterLst(argObj);
		return objMap;
	}
	
	@RequestMapping(value="/getProtocolStepLst")
	protected Map<String, Object> getProtocolStepLst(@RequestBody Map<String, Object> argObj){
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.getProtocolStepLst(argObj);
		return objMap;
	}
	
	@RequestMapping(value="/addProtocolStep")
	protected Map<String, Object> addProtocolStep(@RequestBody Map<String, Object> argObj){
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.addProtocolStep(argObj);
		return objMap;
	}
	
	@RequestMapping(value="/deleteProtocolStep")
	protected Map<String, Object> deleteProtocolStep(@RequestBody Map<String, Object> argObj){
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.deleteProtocolStep(argObj);
		return objMap;
	}
	
	@RequestMapping(value="/sharewithteam")
	protected Map<String, Object> sharewithteam(@RequestBody Map<String, Object> argObj){
		Map<String, Object> objMap = new HashMap<String, Object>();
		objMap = ProtocolMasterService.sharewithteam(argObj);
		return objMap;
	}
}

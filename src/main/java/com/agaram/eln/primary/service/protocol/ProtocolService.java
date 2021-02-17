package com.agaram.eln.primary.service.protocol;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.protocols.LSprotocolmaster;
import com.agaram.eln.primary.model.protocols.LSprotocolstep;
import com.agaram.eln.primary.model.protocols.LSprotocolstepInfo;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LoggedUser;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolMasterRepository;
import com.agaram.eln.primary.repository.protocol.LSProtocolStepRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserteammappingRepository;

@Service
@EnableJpaRepositories(basePackageClasses = LSProtocolMasterRepository.class)
public class ProtocolService {

	@Autowired
	LSProtocolMasterRepository LSProtocolMasterRepositoryObj;
	
	@Autowired
	LSProtocolStepRepository LSProtocolStepRepositoryObj;
	
	@Autowired
	LSuserteammappingRepository LSuserteammappingRepositoryObj;
	
	@Autowired
	LSuserMasterRepository LSuserMasterRepositoryObj;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
    private LSuserMasterRepository lsuserMasterRepository;
	
	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
	
	
	@SuppressWarnings("unused")
	public Map<String, Object> getProtocolMasterInit(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LScfttransactionobj.setTableName("LSprotocolmaster");
			lscfttransactionRepository.save(LScfttransactionobj);
		}
			@SuppressWarnings("unchecked")
			List<LSprotocolmaster> LSprotocolmasterLst = (List<LSprotocolmaster>) getLSProtocolMasterLst(argObj).get("LSProtocolMasterLst");
//			LSuserteammappingRepositoryObj
			if(LSprotocolmasterLst.size() > 0) {
				List<LSprotocolstep> LSprotocolstepLst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(LSprotocolmasterLst.get(0).getProtocolmastercode(), 1);
				for(LSprotocolstep LSprotocolstepObj: LSprotocolstepLst) {
					LSprotocolstepInfo LSprotocolstepInfoObj = mongoTemplate.findById(LSprotocolstepObj.getProtocolstepcode(), LSprotocolstepInfo.class);
					if(LSprotocolstepInfoObj != null) {
						LSprotocolstepObj.setLsprotocolstepInfo(LSprotocolstepInfoObj.getContent());
					}
				}
				mapObj.put("protocolmasterLst", LSprotocolmasterLst);
				mapObj.put("protocolstepLst", LSprotocolstepLst);
			}else {
				mapObj.put("protocolmasterLst", LSprotocolmasterLst);
				mapObj.put("protocolstepLst", new ArrayList<>());
			}
		
		return mapObj;
	}
	
	public Map<String, Object> getLSProtocolMasterLst(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj.findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1, LScfttransactionobj.getLssitemaster());
			List<Integer> teamCodeLst = LSuserteammappingRepositoryObj.getTeamcodeByLsuserMaster4postgressandsql(LScfttransactionobj.getLsuserMaster());
			
			if(teamCodeLst.size() > 0) {
				List<LSuserMaster> lsusermasterLst = LSuserteammappingRepositoryObj.getLsuserMasterByTeamcode(teamCodeLst);
			
				if(lsusermasterLst.size() > 0) {
				for(LSuserMaster lsusermasterObj : lsusermasterLst) {
					if(lsusermasterObj.getUsercode() != LScfttransactionobj.getLsuserMaster()) {
						List<LSprotocolmaster> LSprotocolmasterTempLst = LSProtocolMasterRepositoryObj.findByCreatedbyAndStatusAndLssitemasterAndSharewithteam(lsusermasterObj.getUsercode(), 1, lsusermasterObj.getLssitemaster().getSitecode(), 1);
						if(LSprotocolmasterTempLst.size() > 0) {
							LSprotocolmasterLst.addAll(LSprotocolmasterTempLst);
						}
					}
				}
			}
			}
			mapObj.put("LSProtocolMasterLst", LSprotocolmasterLst);
		}
		
		return mapObj;
	}
	
	public Map<String, Object> getProtocolStepLst(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			LSprotocolmaster newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>(){});
			List<LSprotocolstep> LSprotocolsteplst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(newProtocolMasterObj.getProtocolmastercode() , 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
			for(LSprotocolstep LSprotocolstepObj1: LSprotocolsteplst) {
				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
				if(newLSprotocolstepInfo != null) {
					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
				}
				LSprotocolstepLst.add(LSprotocolstepObj1);
//				LSprotocolstepObj1.setLsprotocolstepInfo(mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class).getContent());
			}
			if(LSprotocolsteplst != null) {
				mapObj.put("protocolstepLst", LSprotocolstepLst);
			}else {
				mapObj.put("protocolstepLst", new ArrayList<>());
			}
		}
		return mapObj;
	}
	
	public Map<String, Object> addProtocolStep(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			
		}
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				LSuserMaster objuser= lsuserMasterRepository.findByusername(username);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//				cfttransaction.setLssitemaster(objuser.getLssitemaster());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
			if(argObj.containsKey("newProtocolstepObj")) {
				LSprotocolstep LSprotocolstepObj = new ObjectMapper().convertValue(argObj.get("newProtocolstepObj"), new TypeReference<LSprotocolstep>() {});
				LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj.findByusercode(LScfttransactionobj.getLsuserMaster());
				if(LSprotocolstepObj.getStatus() == null) {
					LSprotocolstepObj.setStatus(1);
					LSprotocolstepObj.setCreatedby(LScfttransactionobj.getLsuserMaster());
					LSprotocolstepObj.setCreatedbyusername(LsuserMasterObj.getUsername());
					LSprotocolstepObj.setCreateddate(new Date());
				}
				LSProtocolStepRepositoryObj.save(LSprotocolstepObj); 
				LSprotocolstepInfo LSprotocolstepInfoObj = new LSprotocolstepInfo();
				if(LSprotocolstepObj.getStatus() != null) {
//					mongoTemplate.remove(LSprotocolstepInfoObj);
					Query query = new Query(Criteria.where("id").is(LSprotocolstepObj.getProtocolstepcode()));
					Update update=new Update();
					update.set("content",LSprotocolstepObj.getLsprotocolstepInfo());
					
					mongoTemplate.upsert(query, update, LSprotocolstepInfo.class);
				}else {
					LSprotocolstepInfoObj.setId(LSprotocolstepObj.getProtocolstepcode());
					LSprotocolstepInfoObj.setContent(LSprotocolstepObj.getLsprotocolstepInfo());
					mongoTemplate.insert(LSprotocolstepInfoObj);
				}
				List<LSprotocolstep> tempLSprotocolstepLst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus(LSprotocolstepObj.getProtocolmastercode(), 1);
				List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
				for(LSprotocolstep LSprotocolstepObj1: tempLSprotocolstepLst) {
					LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
					if(newLSprotocolstepInfo != null) {
						LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
					}
					LSprotocolstepLst.add(LSprotocolstepObj1);
				}
				mapObj.put("protocolstepLst", LSprotocolstepLst);
			}
			
		return mapObj;
	}
	
	public Map<String, Object> deleteProtocolStep(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),new TypeReference<LScfttransaction>() {});
			
			List<LSprotocolstep> updateLSprotocolstepLst = new ObjectMapper().convertValue(argObj.get("protocolstepLst"), new TypeReference<List<LSprotocolstep>>() {});
			for(LSprotocolstep LSprotocolstepObj1: updateLSprotocolstepLst) {
				LSProtocolStepRepositoryObj.save(LSprotocolstepObj1);				
			}
			
			List<LSprotocolstep> tempLSprotocolstepLst = LSProtocolStepRepositoryObj.findByProtocolmastercodeAndStatus((Integer)argObj.get("protocolmastercode"), 1);
			List<LSprotocolstep> LSprotocolstepLst = new ArrayList<LSprotocolstep>();
			for(LSprotocolstep LSprotocolstepObj1: tempLSprotocolstepLst) {
				LSprotocolstepInfo newLSprotocolstepInfo = mongoTemplate.findById(LSprotocolstepObj1.getProtocolstepcode(), LSprotocolstepInfo.class);
				if(newLSprotocolstepInfo != null) {
					LSprotocolstepObj1.setLsprotocolstepInfo(newLSprotocolstepInfo.getContent());
				}
				LSprotocolstepLst.add(LSprotocolstepObj1);
			}
			mapObj.put("protocolstepLst", LSprotocolstepLst);
		
			
		}
		return mapObj;
	}
	
	public Map<String, Object> addProtocolMaster(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
		}
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		
//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				LSuserMaster objuser= lsuserMasterRepository.findByusername(username);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//				cfttransaction.setLssitemaster(objuser.getLssitemaster());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
			if(argObj.containsKey("newProtocolMasterObj")) {
				LSuserMaster LsuserMasterObj = LSuserMasterRepositoryObj.findByusercode(LScfttransactionobj.getLsuserMaster());
				LSprotocolmaster newProtocolMasterObj = new LSprotocolmaster();
				if(argObj.containsKey("edit")) {
					LSprotocolmaster newProtocolMasterObj1 = new ObjectMapper().convertValue(argObj.get("newProtocolMasterObj"), new TypeReference<LSprotocolmaster>() {});
					newProtocolMasterObj = LSProtocolMasterRepositoryObj.findFirstByProtocolmastercodeAndStatusAndLssitemaster(newProtocolMasterObj1.getProtocolmastercode(), 1, LScfttransactionobj.getLssitemaster());
					newProtocolMasterObj.setProtocolmastername(newProtocolMasterObj1.getProtocolmastername());
				}else {
					newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("newProtocolMasterObj"), new TypeReference<LSprotocolmaster>() {});
					newProtocolMasterObj.setCreatedate(new Date());
					newProtocolMasterObj.setLssitemaster(LScfttransactionobj.getLssitemaster());
					newProtocolMasterObj.setCreatedbyusername(LsuserMasterObj.getUsername());
				}
				LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);
				List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj.findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1,  LScfttransactionobj.getLssitemaster());
				List<LSprotocolmaster> AddedLSprotocolmasterObj =LSProtocolMasterRepositoryObj.findByStatusAndLssitemasterAndProtocolmastername(1, LScfttransactionobj.getLssitemaster(), newProtocolMasterObj.getProtocolmastername());
//				List<LSprotocolstep> LSprotocolsteplst = new ArrayList<LSprotocolstep>(); 
				if(argObj.containsKey("edit")) {
					Map<String, Object> argObj1 = new HashMap<String, Object>();
					argObj1.put("objsilentaudit", argObj.get("objsilentaudit"));
					argObj1.put("ProtocolMasterObj", argObj.get("newProtocolMasterObj"));
					Map<String, Object> ProtocolStepLstMap = getProtocolStepLst(argObj1);
					mapObj.put("protocolstepLst", ProtocolStepLstMap.get("protocolstepLst"));
				}else {
					mapObj.put("protocolstepLst", new ArrayList<Object>());
				}
				mapObj.put("protocolmasterLst", LSprotocolmasterLst);
				mapObj.put("AddedLSprotocolmasterObj", AddedLSprotocolmasterObj);
			}
		
		return mapObj;
	}
	
	public Map<String, Object> deleteProtocolMaster(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),
					new TypeReference<LScfttransaction>() {
					});
			}
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		
//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				LSuserMaster objuser= lsuserMasterRepository.findByusername(username);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//				cfttransaction.setLssitemaster(objuser.getLssitemaster());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
			if(argObj.containsKey("ProtocolMasterObj")) {
				LSprotocolmaster newProtocolMasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>() {});
				newProtocolMasterObj.setProtocolstatus(0);
				newProtocolMasterObj.setStatus(0);
				LSProtocolMasterRepositoryObj.save(newProtocolMasterObj);
				List<LSprotocolmaster> LSprotocolmasterLst = LSProtocolMasterRepositoryObj.findByCreatedbyAndStatusAndLssitemaster(LScfttransactionobj.getLsuserMaster(), 1,  LScfttransactionobj.getLssitemaster());
				mapObj.put("protocolmasterLst", LSprotocolmasterLst);
				
				Map<String, Object> argObj1 = new HashMap<String, Object>();
				argObj1.put("objsilentaudit", argObj.get("objsilentaudit"));
				if(LSprotocolmasterLst.size() > 0) {
					argObj1.put("ProtocolMasterObj", LSprotocolmasterLst.get(0));
					Map<String, Object> ProtocolStepLstMap = getProtocolStepLst(argObj1);
					mapObj.put("protocolstepLst", ProtocolStepLstMap.get("protocolstepLst"));
				}
			}
		
		return mapObj;
	}
	
	public Map<String, Object> sharewithteam(Map<String, Object> argObj){
		Map<String, Object> mapObj = new HashMap<String, Object>();
		@SuppressWarnings("unused")
		LScfttransaction LScfttransactionobj = new LScfttransaction();
		if(argObj.containsKey("objsilentaudit")) {
			LScfttransactionobj = new ObjectMapper().convertValue(argObj.get("objsilentaudit"),new TypeReference<LScfttransaction>() {});
		ObjectMapper objMapper= new ObjectMapper();
		LoggedUser objUser = new LoggedUser();
		
//		silent audit
		if(LScfttransactionobj!=null ) {
			LScfttransactionobj.setTableName("LSprotocolmaster");
			if(argObj.containsKey("username")) {
				String username= objMapper.convertValue(argObj.get("username"), String.class);
				LSuserMaster objuser= lsuserMasterRepository.findByusername(username);
				LScfttransactionobj.setLsuserMaster(objuser.getUsercode());
//				cfttransaction.setLssitemaster(objuser.getLssitemaster());
				LScfttransactionobj.setLssitemaster(objuser.getLssitemaster().getSitecode());
				LScfttransactionobj.setUsername(username);
			}
			lscfttransactionRepository.save(LScfttransactionobj);
		}
//		manual audit
		if(argObj.containsKey("objuser")) {
			objUser=objMapper.convertValue(argObj.get("objuser"), LoggedUser.class);
			if(argObj.containsKey("objmanualaudit")) {
				LScfttransaction objmanualaudit=new LScfttransaction();
				objmanualaudit = objMapper.convertValue(argObj.get("objmanualaudit"), LScfttransaction.class);
				
				objmanualaudit.setComments(objUser.getComments());
				lscfttransactionRepository.save(objmanualaudit);
			}
		}
			LSprotocolmaster LSprotocolmasterObj = new ObjectMapper().convertValue(argObj.get("ProtocolMasterObj"), new TypeReference<LSprotocolmaster>() { });
			LSprotocolmasterObj.setSharewithteam(1);
			LSProtocolMasterRepositoryObj.save(LSprotocolmasterObj);
			Map<String, Object> LSProtocolMasterLstMap = getLSProtocolMasterLst(argObj);
			mapObj.put("LSProtocolMasterLst", LSProtocolMasterLstMap.get("LSProtocolMasterLst"));
			mapObj.put("status", "success");
		}
		return mapObj;
	}
}

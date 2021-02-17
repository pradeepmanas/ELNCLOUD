package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolmaster;

public interface LSProtocolMasterRepository extends JpaRepository<LSprotocolmaster, Integer> {

	List<LSprotocolmaster> findByStatus(Integer status);
	
	List<LSprotocolmaster> findByStatusAndLssitemaster(Integer status, Integer site);
	
	LSprotocolmaster findFirstByProtocolmastercodeAndStatusAndLssitemaster(Integer protocolmastercode, Integer status, Integer site);
	
//	List<LSprotocolmaster> findByCreatedbyAndStatusAndLssitemaster(Integer createdby, Integer status, LSSiteMaster site);
	List<LSprotocolmaster> findByCreatedbyAndStatusAndLssitemaster(Integer createdby, Integer status, Integer site);
	
	List<LSprotocolmaster> findByCreatedbyAndStatusAndLssitemasterAndSharewithteam(Integer createdby, Integer status, Integer site, Integer Sharewithteam);
	
//	List<LSprotocolmaster> findByInCreatedbyAndStatusAndLssitemaster(Integer createdby, Integer status, LSSiteMaster site);
	
	List<LSprotocolmaster> findAll();
	
	@SuppressWarnings("unchecked")
	LSprotocolmaster save(LSprotocolmaster LSprotocolmasterObj);
	
//	List<LSprotocolmaster> findByStatusAndLssitemasterAndProtocolmastername(Integer status, LSSiteMaster site, String protocolmastername);
	List<LSprotocolmaster> findByStatusAndLssitemasterAndProtocolmastername(Integer status, Integer site, String protocolmastername);
	
//	void Save(LSprotocolmaster LSprotocolmasterObj);
	
}

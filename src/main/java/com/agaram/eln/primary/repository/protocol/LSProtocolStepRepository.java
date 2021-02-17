package com.agaram.eln.primary.repository.protocol;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agaram.eln.primary.model.protocols.LSprotocolstep;

public interface LSProtocolStepRepository extends JpaRepository<LSprotocolstep, Integer>{

	List<LSprotocolstep> findByProtocolmastercode(Integer protocolmastercode);
	
	List<LSprotocolstep> findByProtocolmastercodeAndStatus(Integer protocolmastercode, Integer status);
	
	@SuppressWarnings("unchecked")
	LSprotocolstep save(LSprotocolstep LSprotocolstepObj);
}

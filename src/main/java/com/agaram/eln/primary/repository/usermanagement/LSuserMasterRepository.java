package com.agaram.eln.primary.repository.usermanagement;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.agaram.eln.primary.model.usermanagement.LSSiteMaster;
import com.agaram.eln.primary.model.usermanagement.LSuserActions;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.model.usermanagement.LSusergroup;

public interface LSuserMasterRepository extends JpaRepository<LSuserMaster, Integer> {
	 public LSuserMaster findByusername(String username);
	 
	 public LSuserMaster findByusercode(Integer usercode);
	 
	 public List<LSuserMaster> findByUsernameAndLssitemaster(String username, LSSiteMaster lssitemaster);
	 
	 public List<LSuserMaster> findByLssitemasterAndLsusergroup(LSSiteMaster lssitemaster, LSusergroup lsusergroup);
	 
	 @Transactional
	 @Modifying
	 @Query("update LSuserMaster u set u.password = ?1 where u.usercode = ?2")
	 void setpasswordByusercode(String password, Integer usercode);
	 
	 public List<LSuserMaster> findByusernameNot(String username);
	 public LSuserMaster findByUsernameAndPassword(String username, String password);
	 public List<LSuserMaster> findByUsernameNotAndLssitemaster(String username, LSSiteMaster lssitemaster);

	 public LSuserMaster findByusernameIgnoreCase(String username);
	
	 @Transactional
	 @Modifying
	 @Query("update LSuserMaster u set u.lsuserActions = ?1 where u.usercode = ?2")
	 void setuseractionByusercode(LSuserActions lsuserActions, Integer usercode);
}

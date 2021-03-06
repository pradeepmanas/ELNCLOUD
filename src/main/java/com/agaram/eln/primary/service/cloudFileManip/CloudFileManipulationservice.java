package com.agaram.eln.primary.service.cloudFileManip;

import java.io.IOException;
import java.util.Date;

import org.bson.BsonBinarySubType;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.agaram.eln.primary.model.cfr.LScfttransaction;
import com.agaram.eln.primary.model.cloudFileManip.CloudOrderAttachment;
import com.agaram.eln.primary.model.cloudFileManip.CloudProfilePicture;
import com.agaram.eln.primary.model.instrumentDetails.LsOrderattachments;
import com.agaram.eln.primary.model.usermanagement.LSuserMaster;
import com.agaram.eln.primary.repository.cfr.LScfttransactionRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudOrderAttachmentRepository;
import com.agaram.eln.primary.repository.cloudFileManip.CloudProfilePictureRepository;
import com.agaram.eln.primary.repository.usermanagement.LSuserMasterRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

@Service
public class CloudFileManipulationservice {
	
	@Autowired
	private CloudOrderAttachmentRepository cloudOrderAttachmentRepository;
 	
	@Autowired
    private CloudProfilePictureRepository cloudProfilePictureRepository;

	@Autowired
	private LScfttransactionRepository lscfttransactionRepository;
 	
	@Autowired
    private LSuserMasterRepository lsuserMasterRepository;
	
	public CloudProfilePicture addPhoto(Integer usercode, MultipartFile file,Date currentdate) throws IOException { 
    	
    	LSuserMaster username=lsuserMasterRepository.findByusercode(usercode);
    	String name=username.getUsername();
       	LScfttransaction list= new LScfttransaction();
    	list.setModuleName("UserManagement");
    	list.setComments(name+" "+"Uploaded the profile picture successfully");
    	list.setActions("View / Load");
    	list.setSystemcoments("System Generated");
    	list.setTableName("profile");
    	list.setTransactiondate(currentdate);
    	list.setLsuserMaster(usercode);
		lscfttransactionRepository.save(list);
		deletePhoto(usercode,list);
		
		CloudProfilePicture profile = new CloudProfilePicture(); 
    	profile.setId(usercode);
    	profile.setName(file.getName());
    	profile.setImage(
          new Binary(BsonBinarySubType.BINARY, file.getBytes())); 
    	profile = cloudProfilePictureRepository.save(profile); 
 
    	return profile; 
    }
	
	public CloudProfilePicture getPhoto(Integer id) { 
		   
        return cloudProfilePictureRepository.findById(id); 
    }
    
    public Long deletePhoto(Integer id,LScfttransaction list) { 
    	list.setTableName("ProfilePicture");
    	lscfttransactionRepository.save(list);
        return cloudProfilePictureRepository.deleteById(id); 
    }
    
    public CloudOrderAttachment storeattachment(MultipartFile file) throws IOException
    {
    	CloudOrderAttachment objattachment = new CloudOrderAttachment();
    	objattachment.setFile(
          new Binary(BsonBinarySubType.BINARY, file.getBytes()));
    	
    	objattachment = cloudOrderAttachmentRepository.save(objattachment);
    	
    	return objattachment;
    }
    
    public String storeLargeattachment(String title, MultipartFile file) throws IOException { 
        DBObject metaData = new BasicDBObject(); 
        metaData.put("title", title); 
        
       // Object id = gridFsTemplate.store(file.getInputStream(), file.getName(), file.getContentType(), metaData).getId(); 
        return ""; //id.toString(); 
    }
    
    public CloudOrderAttachment retrieveFile(LsOrderattachments objattachment){
    
    	CloudOrderAttachment objfile = cloudOrderAttachmentRepository.findById(objattachment.getFileid());
	
        return objfile;
	}
    
    public GridFSDBFile retrieveLargeFile(String fileid) throws IllegalStateException, IOException{
    	GridFSDBFile file = null; // gridFsTemplate.findOne(new Query(Criteria.where("_id").is(fileid)));
    	return file;
    }
    
    public Long deleteattachments(String id) { 
        return cloudOrderAttachmentRepository.deleteById(id); 
    }
    
    public void deletelargeattachments(String id) { 
    	//gridFsTemplate.delete(Query.query(Criteria.where("_id").is(id)));
    }
}

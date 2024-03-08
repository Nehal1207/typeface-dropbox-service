package com.typeface.dropbox.service;

import com.mongodb.client.result.DeleteResult;
import com.typeface.dropbox.entity.DropboxEntity;
import com.typeface.dropbox.repository.DropboxRepository;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DropboxService {

    @Autowired
    DropboxRepository dropboxRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    public String uploadFile(String fileName, Map<String, String> metaData, byte[] file) {
        DropboxEntity dropboxEntity = new DropboxEntity();
        dropboxEntity.setFileName(fileName);
        dropboxEntity.setFileSize(metaData.get("fileSize"));
        dropboxEntity.setFileType(metaData.get("fileType"));
        dropboxEntity.setCreatedAt(new Date());
        dropboxEntity.setFile(new Binary(file));
        return dropboxRepository.save(dropboxEntity).getFileId();
    }

    public Binary retrieveFile(String fileId) throws Exception {
        DropboxEntity entity = dropboxRepository.findById(fileId).orElse(null);
        if(entity==null){
            throw new Exception("File does not exist with id : " + fileId);
        }
        return entity.getFile();
    }

    public void updateFile(byte[] file, String fileId, Map<String, String> fileData) throws Exception {
        DropboxEntity dropboxEntity = dropboxRepository.findById(fileId).orElse(null);
        if(dropboxEntity==null){
            throw new Exception("File does not exist with id : " + fileId);
        }
        if (fileData.get("fileName") != null) dropboxEntity.setFileName(fileData.get("fileName"));
        if (fileData.get("fileSize") != null) dropboxEntity.setFileSize(fileData.get("fileSize"));
        if (fileData.get("fileType") != null) dropboxEntity.setFileType(fileData.get("fileType"));
        if (file.length != 0) dropboxEntity.setFile(new Binary(file));
        dropboxRepository.save(dropboxEntity);
    }

    public void deleteFile(String fileId) throws Exception {
        DeleteResult deleteResult = mongoTemplate.remove(new Query(Criteria.where("_id").is(fileId)),DropboxEntity.class);
        if(deleteResult.getDeletedCount()==0){
            throw new Exception("File does not exist with id : " + fileId);
        }
    }

    public List<DropboxEntity> getFileListing(Integer pageNo, Integer pageSize) {
        pageNo = pageNo == null ? 0 : pageNo;
        pageSize = pageSize == null ? 5 : pageSize;
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        Query query = new Query();
        query.fields().exclude("file");
        query.with(pageable);
        return mongoTemplate.find(query,DropboxEntity.class);
    }
}

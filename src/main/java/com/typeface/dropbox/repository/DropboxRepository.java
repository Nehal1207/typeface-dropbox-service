package com.typeface.dropbox.repository;

import com.typeface.dropbox.entity.DropboxEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DropboxRepository extends MongoRepository<DropboxEntity,String> {
}

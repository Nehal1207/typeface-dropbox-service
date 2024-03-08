package com.typeface.dropbox.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.Binary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.Date;

@Document("Dropbox")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DropboxEntity implements Serializable {
    @Id
    private String fileId;
    private String fileName;
    private String fileSize;
    private String fileType;
    private Date createdAt;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Binary file;
}

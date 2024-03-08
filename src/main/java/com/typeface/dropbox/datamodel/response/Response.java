package com.typeface.dropbox.datamodel.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.typeface.dropbox.entity.DropboxEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String message;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String fileId;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<DropboxEntity> fileList;
}

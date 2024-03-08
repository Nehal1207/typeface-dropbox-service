package com.typeface.dropbox.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.typeface.dropbox.datamodel.response.Response;
import com.typeface.dropbox.entity.DropboxEntity;
import com.typeface.dropbox.service.DropboxService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.Binary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RestController
@Slf4j
public class DropboxController {

    @Autowired
    DropboxService dropboxService;

    @Autowired
    ObjectMapper objectMapper;

//---------------------------------------------------------------------------------------------
//        1. Upload File API: Allow users to upload files onto the platform.
//        Endpoint: POST /files/upload
//        Input: File binary data, file name, metadata (if any)
//        Output: A unique file identifier
//        Metadata to Save: File name, createdAt timestamp, size, file type.

    @PostMapping(value = "/files/upload")
    public ResponseEntity<Response> uploadFile(HttpServletRequest request,
                                               @RequestParam String fileName,
                                               @RequestParam Map<String, String> metaData) {

        HttpStatus responseStatus = HttpStatus.OK;
        Response response = new Response();
        try {
            byte[] bytes = request.getInputStream().readAllBytes();
            String fileId = dropboxService.uploadFile(fileName, metaData, bytes);
            response.setMessage("File saved successfully");
            response.setFileId(fileId);
            log.info("File saved with id : {}", fileId);
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setMessage("Error while saving file.");
            log.error("Error while saving file : {}", e.getMessage());
        }
        return ResponseEntity.status(responseStatus).body(response);
    }

//---------------------------------------------------------------------------------------------
//        2.Read File API:Retrieve a specific file based on a unique identifier.
//        Endpoint:GET/files/{fileId}
//        Input:Unique file identifier
//        Output:File binary data

    @GetMapping("/files/{fileId}")
    public void retrieveFile(@PathVariable String fileId, HttpServletResponse response) throws Exception {
        Binary file = dropboxService.retrieveFile(fileId);
        response.getOutputStream().write(file.getData());
    }

//---------------------------------------------------------------------------------------------
//      3.Update File API:Update an existing file or its metadata.
//      Endpoint:PUT/files/{fileId}
//      Input:New file binary data or new metadata
//      Output:Updated metadata or a success message

    @PutMapping("/files/{fileId}")
    public ResponseEntity<Response> updateFile(HttpServletRequest request, @PathVariable String fileId, @RequestParam Map<String, String> fileData) throws IOException {

        HttpStatus responseStatus = HttpStatus.OK;
        Response response = new Response();
        try {
            dropboxService.updateFile(request.getInputStream().readAllBytes(), fileId, fileData);
            response.setMessage("File updated successfully");
            response.setFileId(fileId);
            log.info("File updated with id : {}", fileId);
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setMessage("Error while updating file.");
            log.error("Error while updating file : {}", e.getMessage());
        }
        return ResponseEntity.status(responseStatus).body(response);
    }


//---------------------------------------------------------------------------------------------
//        4.Delete File API:Delete a specific file based on a unique identifier.
//        Endpoint:DELETE/files/{fileId}
//        Input:Unique file identifier
//        Output:A success or failure message

    @DeleteMapping("/files/{fileId}")
    public ResponseEntity<Response> deleteFile(@PathVariable String fileId) {
        HttpStatus responseStatus = HttpStatus.OK;
        Response response = new Response();
        try {
            dropboxService.deleteFile(fileId);
            response.setMessage("File deleted successfully");
            response.setFileId(fileId);
            log.info("File deleted with id : {}", fileId);
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setMessage("Error while deleting file.");
            log.error("Error while deleting file : {}", e.getMessage());
        }
        return ResponseEntity.status(responseStatus).body(response);
    }

//---------------------------------------------------------------------------------------------
//        5.List Files API:List all available files and their metadata.
//        Endpoint:GET/files
//        Input:None
//        Output:A list of file metadata objects,including file IDs,names,createdAt timestamps,etc.

    @GetMapping("/files")
    public ResponseEntity<Response> getFileListing(@RequestParam(required = false) Integer pageNo,
                                                              @RequestParam(required = false) Integer pageSize) {

        HttpStatus responseStatus = HttpStatus.OK;
        Response response  = new Response();
        try {
            List<DropboxEntity> fileList = dropboxService.getFileListing(pageNo, pageSize);
            response.setMessage("Files fetched successfully");
            response.setFileList(fileList);
            log.info("Fetched files from DB : {}", objectMapper.writeValueAsString(fileList));
        } catch (Exception e) {
            responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
            response.setMessage("Error while fetching files.");
            log.error("Error while fetching files : {}", e.getMessage());
        }
        return ResponseEntity.status(responseStatus).body(response);
    }

}






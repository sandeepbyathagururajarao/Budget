package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.config.ApplicationProperties;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * REST controller for uploading file
 */
@RestController
@RequestMapping("/api")
public class UploadResource {

    private final Logger log = LoggerFactory.getLogger(UploadResource.class);

    private static final String ENTITY_NAME = "upload";

    @Autowired
    private ApplicationProperties properties;

    private final static String SUCCESS = "File uploaded succesfully";
    private final static String ERROR = "Unable to upload file due to ";


    public UploadResource() {

    }

    /**
     *
     * @param imageFile
     * @return
     * @throws URISyntaxException
     */

    @PostMapping("/upload")
    @Timed
    public String uploadFile(@RequestParam("imageFile") MultipartFile imageFile) throws URISyntaxException {
        log.debug("REST request to upload file");
        String newName = null;
        try {
            byte[] bytes = imageFile.getBytes();
            String fileName = imageFile.getOriginalFilename();
            String extn = fileName.substring(fileName.lastIndexOf(".")+1,fileName.length());
            newName = String.format("%s.%s", RandomStringUtils.randomAlphanumeric(8), extn);
            Path path = Paths.get(properties.getFileUploadPath() + File.separator + newName);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
            return UploadResource.ERROR+e.getMessage();
        }
        return newName+"_"+UploadResource.SUCCESS;
    }
}

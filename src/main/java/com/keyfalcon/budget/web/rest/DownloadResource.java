package com.keyfalcon.budget.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.keyfalcon.budget.config.ApplicationProperties;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

/**
 * REST controller for uploading file
 */
@RestController
@RequestMapping("/api")
public class DownloadResource {

    private final Logger log = LoggerFactory.getLogger(DownloadResource.class);

    private static final String ENTITY_NAME = "upload";

    @Autowired
    private ApplicationProperties properties;

    private final static String SUCCESS = "File uploaded succesfully";
    private final static String ERROR = "Unable to upload file due to ";


    public DownloadResource() {

    }

    /**
     *
     * @param fileName
     * @param request
     * @return
     * @throws URISyntaxException
     */

    @GetMapping("/download/{fileName:.+}")
    @Timed
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) throws URISyntaxException {
        log.debug("REST request to download file");
        try {
            Resource resource = getFileResource(fileName);
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                log.info("Could not determine file type.");
            }
            if(contentType == null) {
                contentType = "application/octet-stream";
            }
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(null));
    }

    private Resource getFileResource(String fileName) throws MalformedURLException {
        Path path = Paths.get(properties.getFileUploadPath() + File.separator + fileName);
        Resource resource = new UrlResource(path.toUri());
        if(resource.exists()) {
            return resource;
        } else {
            throw new RuntimeException("File not found " + fileName);
        }
    }
}

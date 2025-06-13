package com.video_trimmer.resouce;

import com.video_trimmer.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.MultipartConfigElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/video")
public class VideoResource {
    @Autowired
    private MultipartConfigElement multipartConfigElement;

    @Autowired
    private VideoService videoService;


    @PostConstruct
    public void logMultipartLimit() {
        System.out.println("Max file size: " + multipartConfigElement.getMaxFileSize());
        System.out.println("Max request size: " + multipartConfigElement.getMaxRequestSize());
    }

    @Operation(summary = "Trim uploaded video")
    @PostMapping(value = "/upload-and-split", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadAndSplitVideo(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("partDurationSec") int partDurationSec) {
        try {
            System.out.println("Uploaded file size: " + file.getSize()); // should show size in bytes
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }

            // Save uploaded file to a temp location
            File tempFile = File.createTempFile("uploaded-", ".mp4");
            file.transferTo(tempFile);



            return ResponseEntity.ok(videoService.splitVideoIntoParts(tempFile, partDurationSec));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @Operation(summary = "Trim uploaded video")
    @PostMapping(value = "/upload-and-trim", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> trimVideoBasedOnTime(@RequestParam("file") MultipartFile file,
                                                      @RequestParam("partDurationSec") int startTimeInMinutes,
                                                      @RequestParam("partDurationSec") int endTimeInMinutes) {
        try {
            System.out.println("Uploaded file size: " + file.getSize()); // should show size in bytes
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("No file uploaded");
            }

            // Save uploaded file to a temp location
            File tempFile = File.createTempFile("uploaded-", ".mp4");
            file.transferTo(tempFile);



            return ResponseEntity.ok(videoService.trimVideoBasedOnDuration(tempFile, startTimeInMinutes, endTimeInMinutes));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }




}

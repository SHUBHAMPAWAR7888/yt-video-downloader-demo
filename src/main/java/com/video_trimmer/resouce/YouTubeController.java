package com.video_trimmer.resouce;

import com.video_trimmer.service.YouTubeService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/youtube")
public class YouTubeController {

    @Autowired
    private YouTubeService youTubeService;

    @Operation(summary = "download youTube  video")
    @PostMapping("/download")
    public ResponseEntity<String> download(
            @RequestParam String url,
            @RequestParam(defaultValue = "false") boolean audioOnly,
            @RequestParam(required = false) String format,
            @RequestParam(defaultValue = "false") boolean subtitles,
            @RequestParam(defaultValue = "false") boolean playlist) {
        String result = youTubeService.downloadVideo(url,audioOnly,format,subtitles,playlist);
        return ResponseEntity.ok(result);
    }
}


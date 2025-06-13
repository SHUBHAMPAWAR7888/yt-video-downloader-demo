package com.video_trimmer.serviceimpl;

import com.video_trimmer.config.FFmpegProperties;
import com.video_trimmer.service.YouTubeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeServiceImpl implements YouTubeService {

    private FFmpegProperties fFmpegProperties;

    @Autowired
    public YouTubeServiceImpl(FFmpegProperties ffmpegProperties) {
        this.fFmpegProperties = ffmpegProperties;
    }


    public String downloadVideo(String videoUrl, boolean audioOnly, String format, boolean subtitles, boolean playlist) {

        String YT_DLP_PATH = Paths.get(fFmpegProperties.getYtDlpPath()).toString();

        String userHome = System.getProperty("user.home");
        String downloadsDir = Paths.get(userHome, "Downloads").toString();

        List<String> command = new ArrayList<>();
        command.add(YT_DLP_PATH);
        command.add("-o");
        command.add(downloadsDir + File.separator + "%(title)s.%(ext)s");

        // Handle audio-only download
        if (audioOnly) {
            command.add("-x"); // extract audio
            if (format != null && !format.isEmpty()) {
                command.add("--audio-format");
                command.add(format); // e.g., mp3, m4a
            }
        } else if (format != null && !format.isEmpty()) {
            command.add("-f");
            command.add(format); // e.g., bestvideo+bestaudio/mp4
        }

        // Handle subtitles
        if (subtitles) {
            command.add("--write-sub");
            command.add("--sub-lang");
            command.add("en");
            command.add("--convert-subs");
            command.add("srt");
        }

        // Handle playlist download
        if (!playlist) {
            command.add("--no-playlist");
        }

        // Add the actual video URL
        command.add("--no-mtime");
        command.add(videoUrl);

        ProcessBuilder builder = new ProcessBuilder(command);
        builder.redirectErrorStream(true);

        try {
            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            int exitCode = process.waitFor();
            return exitCode == 0 ? "Video downloaded to Downloads folder." :
                    "Failed with exit code: " + exitCode;

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
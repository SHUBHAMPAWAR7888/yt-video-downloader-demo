package com.video_trimmer.serviceimpl;

import com.video_trimmer.config.FFmpegProperties;
import com.video_trimmer.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private FFmpegProperties fFmpegProperties;

    @Autowired
    public VideoServiceImpl(FFmpegProperties ffmpegProperties) {
        this.fFmpegProperties = ffmpegProperties;
    }

    @Override
    public String splitVideoIntoParts(File videoFile, int partDurationSec) throws IOException, InterruptedException {
        String ffmpegPath =Paths.get(fFmpegProperties.getPath()).toString();
        String ffprobePath = Paths.get(fFmpegProperties.getProbePath()).toString();

        // Prepare output directory: Downloads/stream-video
        String userHome = System.getProperty("user.home");
        String outputDir = Paths.get(userHome, "Desktop", "stream-video").toString();
        Files.createDirectories(Paths.get(outputDir));

        // Get video duration using ffprobe (inside this method)
        ProcessBuilder probeBuilder = new ProcessBuilder(
                ffprobePath,
                "-v", "error",
                "-show_entries", "format=duration",
                "-of", "default=noprint_wrappers=1:nokey=1",
                videoFile.getAbsolutePath()
        );
        probeBuilder.redirectErrorStream(true);
        Process probeProcess = probeBuilder.start();

        String durationLine;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(probeProcess.getInputStream()))) {
            durationLine = reader.readLine();
        }
        int exitCode = probeProcess.waitFor();
        if (exitCode != 0 || durationLine == null) {
            return "Failed to get video duration.";
        }
        int totalDurationSec = (int) Math.ceil(Double.parseDouble(durationLine));

        // Calculate number of parts
        int partCount = (int) Math.ceil((double) totalDurationSec / partDurationSec);

        for (int i = 0; i < partCount; i++) {
            int startSec = i * partDurationSec;
            String partName = String.format("part%02d.mp4", i + 1);
            String outputPath = Paths.get(outputDir, partName).toString();

            ProcessBuilder ffmpegBuilder = new ProcessBuilder(
                    ffmpegPath,
                    "-i", videoFile.getAbsolutePath(),
                    "-ss", String.valueOf(startSec),
                    "-t", String.valueOf(partDurationSec),
                    "-c", "copy",
                    outputPath
            );
            ffmpegBuilder.redirectErrorStream(true);
            Process ffmpegProcess = ffmpegBuilder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(ffmpegProcess.getInputStream()))) {
                while (reader.readLine() != null) {
                    // Consume output to avoid blocking
                }
            }

            int ffmpegExitCode = ffmpegProcess.waitFor();
            if (ffmpegExitCode != 0) {
                System.err.println("FFmpeg failed for part: " + partName + " (exit code: " + ffmpegExitCode + ")");
                return "Failed to split video.";
            }
            System.out.println("Successfully created: " + partName);
        }

        return "Video uploaded and split successfully into Downloads/stream-video/";
    }



    public String trimVideoBasedOnDuration(File videoFile, int startTimeInMinutes, int endTimeInMinutes) throws IOException, InterruptedException {
        String ffmpegPath = Paths.get(fFmpegProperties.getPath()).toString();

// Convert minutes to seconds
        int startTimeSec = startTimeInMinutes * 60;
        int endTimeSec = endTimeInMinutes * 60;
        int durationSec = endTimeSec - startTimeSec;

        if (durationSec <= 0) {
            return "Invalid time range: End time must be greater than start time.";
        }

        // Output directory
        String userHome = System.getProperty("user.home");
        String outputDir = Paths.get(userHome, "Desktop", "stream-video").toString();
        Files.createDirectories(Paths.get(outputDir));

        // Output file name
        String outputPath = Paths.get(outputDir, "trimmed-output.mp4").toString();



        if (durationSec <= 0) {
            return "Invalid time range: end time must be greater than start time.";
        }

        // Build FFmpeg process
        ProcessBuilder ffmpegBuilder = new ProcessBuilder(
                ffmpegPath,
                "-ss", String.valueOf(startTimeSec), // seek first
                "-i", videoFile.getAbsolutePath(),
                "-t", String.valueOf(durationSec),
                "-c", "copy",
                outputPath
        );
        ffmpegBuilder.redirectErrorStream(true);
        Process process = ffmpegBuilder.start();

        // Consume output
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            while (reader.readLine() != null) {
                // Consume to avoid blocking
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            return "Failed to trim video. FFmpeg exited with code: " + exitCode;
        }

        return "Trimmed video saved to: " + outputPath;
    }


}

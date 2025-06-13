package com.video_trimmer.config;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ffmpeg")
public class FFmpegProperties {

    private String path;
    private String inputDir;
    private String outputDir;
    private String probePath;
    private String ytDlpPath;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getInputDir() {
        return inputDir;
    }

    public void setInputDir(String inputDir) {
        this.inputDir = inputDir;
    }

    public String getOutputDir() {
        return outputDir;
    }

    public void setOutputDir(String outputDir) {
        this.outputDir = outputDir;
    }

    public String getProbePath() {
        return probePath;
    }

    public void setProbePath(String probePath) {
        this.probePath = probePath;
    }

    public String getYtDlpPath() {
        return ytDlpPath;
    }

    public void setYtDlpPath(String ytDlpPath) {
        this.ytDlpPath = ytDlpPath;
    }
}

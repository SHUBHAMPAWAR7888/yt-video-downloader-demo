package com.video_trimmer.service;

public interface YouTubeService {
    String downloadVideo(String url,boolean audioOnly,String format,boolean subtitles,boolean playlist);
}

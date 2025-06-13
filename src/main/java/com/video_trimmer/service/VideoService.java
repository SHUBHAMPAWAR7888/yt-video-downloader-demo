package com.video_trimmer.service;

import java.io.File;
import java.io.IOException;

public interface VideoService {

    String splitVideoIntoParts(File uploadedFile, int durationSec) throws IOException, InterruptedException;
    String trimVideoBasedOnDuration(File uploadedFile, int startTimeInMinutes, int endTimeInMinutes) throws IOException, InterruptedException;
}

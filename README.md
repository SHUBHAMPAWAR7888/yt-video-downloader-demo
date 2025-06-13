# 📦 Installation and Setup Guide for Video Splitting and YouTube Downloader Application

---

## 1. Install FFmpeg and FFprobe (.exe files)

- 🔗 Download FFmpeg from the official website: [https://ffmpeg.org/download.html](https://ffmpeg.org/download.html)
- Choose the build for your OS (Windows):
    - Download the **static build ZIP** that contains `ffmpeg.exe` and `ffprobe.exe`.
- Extract the ZIP to a folder, for example:  
  `C:\tools\ffmpeg\`
- Confirm that `ffmpeg.exe` and `ffprobe.exe` are inside the extracted folder, typically:  
  `C:\tools\ffmpeg\bin\`

**Note:**  
You can optionally add the folder `C:\tools\ffmpeg\bin` to your **System Environment Variables → PATH** for global command-line access.

---

## 2. Set Paths in `application.yml`

Open your configuration file located at `src/main/resources/application.yml` or `application.properties` and add the following:

```yaml
fFmpegProperties:
  path: "C:\\tools\\ffmpeg\\bin\\ffmpeg.exe"
  probePath: "C:\\tools\\ffmpeg\\bin\\ffprobe.exe"

ytDlpPath: "D:\\yt-dlp.exe"
```

**Notes:**

- Use **double backslashes (`\\`)** in Windows paths.
- Make sure the `.exe` files exist at the provided paths.

---

## 3. Build and Run the Spring Boot Application

- Use your IDE or run from terminal:
  ```bash
  ./gradlew bootRun
  ```
  or
  ```bash
  mvn spring-boot:run
  ```

---

## 4. Upload Video and Perform Split or Trim

- Upload your video file through the provided interface or endpoint.
- The output video chunks will be saved to your **Desktop** under the folder:  
  `stream-video`

---

## 5. Troubleshooting

### FFmpeg Not Found or Runtime Errors
- Ensure the paths in `application.yml` point exactly to the correct location of the `.exe` files.

### File Upload Size Limits
- Update your `application.yml` to allow larger uploads:
  ```yaml
  spring:
    servlet:
      multipart:
        max-file-size: 500MB
        max-request-size: 500MB
  ```

---

## 6. Optional: Add FFmpeg to System PATH

- Add `C:\tools\ffmpeg\bin` to your **System Environment Variable → PATH**
- This allows you to run `ffmpeg` or `ffprobe` from any terminal window.

---

## 7. YouTube Video Downloader (yt-dlp)

This project also supports downloading videos from YouTube using `yt-dlp`.

### Installation:
- Download `yt-dlp.exe` from: [https://github.com/yt-dlp/yt-dlp/releases](https://github.com/yt-dlp/yt-dlp/releases)
- Place it in a known location, e.g.:
  ```
  D:\yt-dlp.exe
  ```

### Configuration:
Add the path to your `application.yml` or `application.properties`:

```yaml
ytDlpPath: "D:\\yt-dlp.exe"
```

---

✅ You can save this guide as `docs/installation-guide.md` in your project.  
It is for **reference only** and **will not affect** your Spring Boot application.

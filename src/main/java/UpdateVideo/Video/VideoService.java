package UpdateVideo.Video;

import UpdateVideo.Video.dto.UploadThumbnailDto;
import UpdateVideo.Video.dto.UploadVideoToServerDto;
import lombok.RequiredArgsConstructor;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private static final String VIDEO_PATH = "F:\\UpdateVideo\\src\\main\\resources\\static\\upload\\video\\";
    private static final String THUMBNAIL_PATH = "F:\\UpdateVideo\\src\\main\\resources\\static\\upload\\thumbnail\\";

    public void uploadServer(UploadVideoToServerDto uploadVideoToServerDto, Map map) {
        String filename = uploadVideoToServerDto.getFile().getOriginalFilename();
        String saveFile = VIDEO_PATH + filename;

        try{
            uploadVideoToServerDto.getFile().transferTo(new File(saveFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.put("filename", filename);
        map.put("filepath", saveFile);
        map.put("success", true);
    }

    public void uploadThumbnail(UploadThumbnailDto uploadThumbnailDto, Map map) throws IOException {
        String filepath = uploadThumbnailDto.getFilepath();
        String filename = uploadThumbnailDto.getFilename();
        String ffmpegBasePath = "C:\\Users\\whd07\\ffmpeg\\bin\\";
        FFprobe ffprobe = new FFprobe(ffmpegBasePath+"ffprobe");

        FFmpegProbeResult probe = ffprobe.probe(filepath);
        FFmpegFormat format = probe.getFormat();
        double duration = format.duration;

        FFmpeg ffmpeg = new FFmpeg(ffmpegBasePath+"ffmpeg");

        FFmpegBuilder builder = new FFmpegBuilder()
                .overrideOutputFiles(true)					// output 파일을 덮어쓸 것인지 여부(false일 경우, output path에 해당 파일이 존재할 경우 예외 발생 - File 'C:/Users/Desktop/test.png' already exists. Exiting.)
                .setInput(filepath)     					// 썸네일 이미지 추출에 사용할 영상 파일의 절대 경로
                .addExtraArgs("-ss", "00:00:05") 			// 영상에서 추출하고자 하는 시간 - 00:00:01은 1초를 의미
                .addOutput(THUMBNAIL_PATH + filename +".jpg") 		// 저장 절대 경로(확장자 미 지정 시 예외 발생 - [NULL @ 000002cc1f9fa500] Unable to find a suitable output format for 'C:/Users/Desktop/test')
                .setFrames(1)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);		// FFmpeg 명령어 실행을 위한 FFmpegExecutor 객체 생성
        executor.createJob(builder).run();

        String filePath = "upload/thumbnail/" + filename +".jpg";
        String videoPath = "upload/video/" + filename;

        map.put("thumbnailPath", filePath);
        map.put("filepath", videoPath);
        map.put("duration", duration);
        map.put("success", true);


    }
}

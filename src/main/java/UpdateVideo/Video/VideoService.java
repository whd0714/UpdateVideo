package UpdateVideo.Video;

import UpdateVideo.Video.dto.UploadThumbnailDto;
import UpdateVideo.Video.dto.UploadVideoDto;
import UpdateVideo.Video.dto.UploadVideoToServerDto;
import UpdateVideo.channel.Channel;
import UpdateVideo.channel.ChannelRepository;
import UpdateVideo.member.Member;
import UpdateVideo.member.MemberRepository;
import UpdateVideo.member.dto.MemberRegisterDto;
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
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class VideoService {

    private final VideoRepository videoRepository;
    private static final String VIDEO_PATH = "F:\\UpdateVideo\\src\\main\\resources\\static\\upload\\video\\";
    private static final String THUMBNAIL_PATH = "F:\\UpdateVideo\\src\\main\\resources\\static\\upload\\thumbnail\\";
    private final MemberRepository memberRepository;
    private final ChannelRepository channelRepository;

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
                .overrideOutputFiles(true)					// output ????????? ????????? ????????? ??????(false??? ??????, output path??? ?????? ????????? ????????? ?????? ?????? ?????? - File 'C:/Users/Desktop/test.png' already exists. Exiting.)
                .setInput(filepath)     					// ????????? ????????? ????????? ????????? ?????? ????????? ?????? ??????
                .addExtraArgs("-ss", "00:00:05") 			// ???????????? ??????????????? ?????? ?????? - 00:00:01??? 1?????? ??????
                .addOutput(THUMBNAIL_PATH + filename +".jpg") 		// ?????? ?????? ??????(????????? ??? ?????? ??? ?????? ?????? - [NULL @ 000002cc1f9fa500] Unable to find a suitable output format for 'C:/Users/Desktop/test')
                .setFrames(1)
                .done();

        FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);		// FFmpeg ????????? ????????? ?????? FFmpegExecutor ?????? ??????
        executor.createJob(builder).run();

        String filePath = "upload/thumbnail/" + filename +".jpg";
        String videoPath = "upload/video/" + filename;

        map.put("filename", filename);
        map.put("thumbnailPath", filePath);
        map.put("filepath", videoPath);
        map.put("duration", duration);
        map.put("success", true);


    }

    public void uploadVideo(UploadVideoDto uploadVideoDto) {
        Optional<Member> memberById = memberRepository.findById(uploadVideoDto.getMemberId());

        Video video = new Video(uploadVideoDto.getFilename(), uploadVideoDto.getFilepath(), uploadVideoDto.getThumbnailPath(),
                uploadVideoDto.getTitle(), uploadVideoDto.getDescription(), uploadVideoDto.getCategory(),
                uploadVideoDto.getAccess(), uploadVideoDto.getDuration());
        videoRepository.save(video);

        memberById.ifPresent(m->{
            Channel channel = m.getChannel();
            if(channel != null) {
                video.changeChannel(channel);
            } else {
                Channel channel2 = new Channel();
                video.changeChannel(channel2);
                channel2.changeMember(m);
                channel2.firstName();
                channelRepository.save(channel2);
            }
        });
    }
}

package UpdateVideo.Video;

import UpdateVideo.Video.dto.UploadThumbnailDto;
import UpdateVideo.Video.dto.UploadVideoDto;
import UpdateVideo.Video.dto.UploadVideoToServerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final VideoRepository videoRepository;

    @PostMapping("/api/upload/server/video")
    public Map uploadVideoToServer(@ModelAttribute UploadVideoToServerDto uploadVideoToServerDto) {

        Map map = new HashMap<String, Object>();
        videoService.uploadServer(uploadVideoToServerDto, map);

        return map;
    }

    @PostMapping("/api/upload/server/thumbnail")
    public Map uploadVideoThumbnail(@RequestBody UploadThumbnailDto uploadThumbnailDto) throws IOException {
        Map map = new HashMap<String, Object>();

        videoService.uploadThumbnail(uploadThumbnailDto, map);

        return map;
    }

    @PostMapping("/api/video/upload")
    public Map uploadVideo(@RequestBody UploadVideoDto uploadVideoDto) {
        Map map = new HashMap<String, Object>();

        videoService.uploadVideo(uploadVideoDto);

        map.put("success", true);

        return map;
    }
}

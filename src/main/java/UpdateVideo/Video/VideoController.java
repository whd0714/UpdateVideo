package UpdateVideo.Video;

import UpdateVideo.Video.dto.UploadThumbnailDto;
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
    private Map uploadVideoToServer(@ModelAttribute UploadVideoToServerDto uploadVideoToServerDto) {

        Map map = new HashMap<String, Object>();
        videoService.uploadServer(uploadVideoToServerDto, map);

        return map;
    }

    @PostMapping("/api/upload/server/thumbnail")
    private Map uploadVideoThumbnail(@RequestBody UploadThumbnailDto uploadThumbnailDto) throws IOException {
        Map map = new HashMap<String, Object>();

        videoService.uploadThumbnail(uploadThumbnailDto, map);

        return map;
    }
}

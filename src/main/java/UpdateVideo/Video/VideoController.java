package UpdateVideo.Video;

import UpdateVideo.Video.dto.UploadVideoToServerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.transform.Result;
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

        System.out.println("map = " + map);

        return map;
    }
}

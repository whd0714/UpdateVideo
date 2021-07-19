package UpdateVideo.Video;

import UpdateVideo.Video.dto.UploadVideoToServerDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
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
}

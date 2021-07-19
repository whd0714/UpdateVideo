package UpdateVideo.Video.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadVideoToServerDto {

    private MultipartFile file;

}

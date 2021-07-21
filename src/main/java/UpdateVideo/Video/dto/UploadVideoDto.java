package UpdateVideo.Video.dto;

import lombok.Data;

@Data
public class UploadVideoDto {

    private String filename;
    private String filepath;
    private int duration;
    private String thumbnailPath;
    private Long memberId;
    private String title;
    private String description;
    private String access;
    private String category;
}

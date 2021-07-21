package UpdateVideo.Video;

import UpdateVideo.channel.Channel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Video {

    @Id @GeneratedValue
    @Column(name = "video_id")
    private Long id;

    private String filename;
    private String title;
    private String description;
    private String access;
    private String category;
    private String filePath;
    private String thumbnailPath;
    private int duration;

    @ManyToOne(fetch = FetchType.LAZY)
    private Channel channel;

    public Video(String filename, String filepath, String thumbnailPath, String title, String description, String category, String access, int duration) {
        this.filename = filename;
        this.filePath = filepath;
        this.thumbnailPath = thumbnailPath;
        this.title = title;
        this.description = description;
        this.category = category;
        this.access = access;
        this.duration = duration;
    }

    public void changeChannel(Channel channel) {
        this.channel = channel;
        channel.getVideos().add(this);
    }
}

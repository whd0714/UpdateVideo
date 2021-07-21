package UpdateVideo.channel;

import UpdateVideo.Video.Video;
import UpdateVideo.member.Member;
import UpdateVideo.subcribe.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Channel {

    @Id @GeneratedValue
    @Column(name = "channel_id")
    private Long id;

    private String channelName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "channel")
    private List<Subscribe> subscribes = new ArrayList<>();

    @OneToMany(mappedBy = "channel")
    private List<Video> videos = new ArrayList<>();

    public void changeMember(Member member) {
        this.member = member;
        member.changeChannel(this);
    }

    public void settingChannelName(String name) {
        this.channelName = name;
    }

    public void firstName() {
        settingChannelName(this.member.getName());
    }
}

package UpdateVideo.member;

import UpdateVideo.channel.Channel;
import UpdateVideo.subcribe.Subscribe;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;
    private String email;
    private String password;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "member")
    private Channel channel;

    @OneToMany(mappedBy = "member")
    private List<Subscribe> subscribes = new ArrayList<>();

    public Member(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void changeChannel(Channel channel) {
        this.channel = channel;
    }
}

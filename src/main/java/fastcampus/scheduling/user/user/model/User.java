package fastcampus.scheduling.user.user.model;


import fastcampus.scheduling.user.common.Position;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "user_tb")
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String userEmail;

    @Column(nullable = false, length = 100)
    private String userPassword;

    @Column(nullable = false, length = 50)
    private String userName;

    @Column(length = 255)
    private String profileThumbUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(unique = true, nullable = false, length = 20)
    private String phoneNumber;

    //@Column(nullable = false)
    private Integer remainingVacation;

    @CreationTimestamp
    private Timestamp createdAt;

    @UpdateTimestamp
    private Timestamp updatedAt;

}

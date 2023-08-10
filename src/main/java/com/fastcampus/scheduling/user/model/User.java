package com.fastcampus.scheduling.user.model;


import com.fastcampus.scheduling.user.common.Position;
import java.time.LocalDateTime;
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

    @Column(unique = true, nullable = false, length = 100,name = "user_email")
    private String userEmail;

    @Column(nullable = false, length = 100,name = "user_password")
    private String userPassword;

    @Column(nullable = false, length = 50,name="user_name")
    private String userName;

    @Column(length = 255,name = "profile_thumb_url")
    private String profileThumbUrl;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Position position;

    @Column(unique = true, nullable = false, length = 20, name = "phone_number")
    private String phoneNumber;

    @Column(nullable = false, name = "used_vacation")
    private Integer usedVacation;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void initUsedVacation() {
        this.usedVacation = 0;
    }

}

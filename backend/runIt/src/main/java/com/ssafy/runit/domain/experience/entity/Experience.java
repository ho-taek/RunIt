package com.ssafy.runit.domain.experience.entity;

import com.ssafy.runit.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Experience {
    @Id
    @GeneratedValue
    @Column(name = "experience_id")
    private long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String activity;

    private long changed;

    private Timestamp createAt;

    private Date startDate;

}

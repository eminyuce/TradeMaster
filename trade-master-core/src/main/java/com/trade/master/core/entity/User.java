package com.trade.master.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by huseyina on 5/21/2017.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String userName;
    private String password;
    private boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastLogin;
}

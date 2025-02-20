package com.trade.master.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Created by Yuce on 4/9/2017.
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "buid"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class BotUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private Integer buId;
    private String publicKey;
    private String privateKey;
    private String userEmail;
    private boolean emailNotification = false;
    private boolean active;
    private String description;
}

package com.trade.master.core.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

/**
 * Created by habanoz on 22.04.2017.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class UserBot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(unique = true)
    private BotUser user;

    @ManyToOne
    private Bot bot;

    private boolean active;
}

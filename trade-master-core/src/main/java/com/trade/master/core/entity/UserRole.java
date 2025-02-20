package com.trade.master.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.io.Serializable;

/**
 * Represents the user roles in the system.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Builder
public class UserRole implements Serializable {

    @Id
    private String userName;

    @Id
    private String role;
}

package com.anyang.maruni.domain.User.domain.entity;

import com.anyang.maruni.global.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false, unique = true, length = 50)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(length = 30)
    private String nickname;

    private LocalDate birthDate;

    @Column(length = 100)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Role role;

    /**
     * Updates the user's nickname.
     *
     * @param nickname the new nickname to set for the user
     */
    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Updates the user's address.
     *
     * @param address the new address to set for the user
     */
    public void updateAddress(String address) {
        this.address = address;
    }

    /**
     * Updates the user's role.
     *
     * @param role the new role to assign to the user
     */
    public void updateRole(Role role) {
        this.role = role;
    }

    /**
     * Determines whether this user is equal to another object based on the userId.
     *
     * Two User instances are considered equal if their userId fields are non-null and equal.
     *
     * @param o the object to compare with this user
     * @return true if the specified object is a User with the same non-null userId; false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return userId != null && userId.equals(user.userId);
    }

    /**
     * Returns a hash code value for the user based on the userId.
     *
     * @return the hash code derived from the userId
     */
    @Override
    public int hashCode() {
        return Objects.hash(userId);
    }
}
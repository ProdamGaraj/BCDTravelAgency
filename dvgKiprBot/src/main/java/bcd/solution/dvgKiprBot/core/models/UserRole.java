package bcd.solution.dvgKiprBot.core.models;

import jakarta.persistence.Entity;


@Entity
public enum UserRole {
    admin,
    partner,
    client,
}

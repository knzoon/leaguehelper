package se.knzoon.leaguehelper.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.ZonedDateTime;

@Entity
public class User {

    @Id
    private Long id;

    private String username;

    private boolean imported = false;

    private ZonedDateTime lastImport;

    public User() {
    }

    public User(Long id, String username) {
        this.id = id;
        this.username = username;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public ZonedDateTime getLastImport() {
        return lastImport;
    }

    public void setLastImport(ZonedDateTime lastImport) {
        this.lastImport = lastImport;
    }
}

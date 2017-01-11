package gr.devian.talosquests.backend.Models;

import com.fasterxml.jackson.annotation.JsonView;
import gr.devian.talosquests.backend.Views.View;
import org.springframework.stereotype.Component;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

/**
 * Created by Nikolas on 9/1/2017.
 */
@Component
@Entity
public class AccessLevel {
    @GeneratedValue
    @Id
    private Long id;

    @JsonView(View.Simple.class)
    private String name = "User";
    @JsonView(View.Simple.class)
    private Boolean canManageOwnData = true;
    @JsonView(View.Simple.class)
    private Boolean canManageUsers = false;
    @JsonView(View.Simple.class)
    private Boolean canManageService = false;
    @JsonView(View.Simple.class)
    private Boolean canManageQuests = false;
    @JsonView(View.Simple.class)
    private Boolean canWipeUsers = false;
    @JsonView(View.Simple.class)
    private Boolean canWipeGames = false;
    @JsonView(View.Simple.class)
    private Boolean canWipeQuests = false;
    @JsonView(View.Simple.class)
    private Boolean canBanUsers = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getCanWipeQuests() {
        return canWipeQuests;
    }

    public void setCanWipeQuests(Boolean canWipeQuests) {
        this.canWipeQuests = canWipeQuests;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCanManageOwnData() {
        return canManageOwnData;
    }

    public void setCanManageOwnData(Boolean canManageOwnData) {
        this.canManageOwnData = canManageOwnData;
    }

    public Boolean getCanManageService() {
        return canManageService;
    }

    public void setCanManageService(Boolean canManageService) {
        this.canManageService = canManageService;
    }

    public Boolean getCanManageUsers() {
        return canManageUsers;
    }

    public void setCanManageUsers(Boolean canManageUsers) {
        this.canManageUsers = canManageUsers;
    }

    public Boolean getCanManageQuests() {
        return canManageQuests;
    }

    public void setCanManageQuests(Boolean canManageQuests) {
        this.canManageQuests = canManageQuests;
    }

    public Boolean getCanWipeUsers() {
        return canWipeUsers;
    }

    public void setCanWipeUsers(Boolean canWipeUsers) {
        this.canWipeUsers = canWipeUsers;
    }

    public Boolean getCanWipeGames() {
        return canWipeGames;
    }

    public void setCanWipeGames(Boolean canWipeGames) {
        this.canWipeGames = canWipeGames;
    }

    public Boolean getCanBanUsers() {
        return canBanUsers;
    }

    public void setCanBanUsers(Boolean canBanUsers) {
        this.canBanUsers = canBanUsers;
    }

    static {
        AccessLevel level = new AccessLevel();
        level.setCanBanUsers(true);
        level.setName("Root");
        level.setCanWipeUsers(true);
        level.setCanWipeGames(true);
        level.setCanWipeQuests(true);
        level.setCanManageUsers(true);
        level.setCanManageQuests(true);
        level.setCanManageService(true);
        level.setCanManageOwnData(true);
        Root = level;

        level = new AccessLevel();
        level.setCanBanUsers(true);
        level.setName("Admin");
        level.setCanWipeUsers(false);
        level.setCanWipeGames(false);
        level.setCanWipeQuests(false);
        level.setCanManageUsers(true);
        level.setCanManageQuests(true);
        level.setCanManageService(false);
        level.setCanManageOwnData(true);

        Admin = level;

        level = new AccessLevel();
        level.setName("User");
        User = level;
    }

    public static AccessLevel Root;
    public static AccessLevel Admin;
    public static AccessLevel User;

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof AccessLevel)) {
            return false;
        }
        AccessLevel obj = (AccessLevel) other;
        return Objects.equals(other, obj);
    }
}

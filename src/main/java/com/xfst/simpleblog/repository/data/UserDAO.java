package com.xfst.simpleblog.repository.data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "user")
public class UserDAO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<PostDAO> posts;

    @Column(name = "active", nullable = false, columnDefinition = "BIT DEFAULT '1'", length = 1)
    private boolean active;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "deleted_time")
    private Date deletedTime;

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

    public List<PostDAO> getPosts() {
        return posts;
    }

    public void setPosts(List<PostDAO> postDAOS) {
        this.posts = postDAOS;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
    }
}

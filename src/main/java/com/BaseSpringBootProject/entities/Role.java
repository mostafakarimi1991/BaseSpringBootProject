package com.BaseSpringBootProject.entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Role {
    @Id
    private Long id;
    private String name;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<ServiceAccess> serviceAccessList;
    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ServiceAccess> getServiceAccessList() {
        return serviceAccessList;
    }

    public void setServiceAccessList(List<ServiceAccess> serviceAccessList) {
        this.serviceAccessList = serviceAccessList;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return "Role{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", serviceAccessList=" + serviceAccessList +
                ", users=" + users +
                '}';
    }
}

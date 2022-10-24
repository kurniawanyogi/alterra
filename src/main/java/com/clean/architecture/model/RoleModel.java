package com.clean.architecture.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleModel {
    @Id
    @Column(name = "ID")
    private Integer id;
    @Column(name = "ROLE_NAME")
    private String roleName;
}

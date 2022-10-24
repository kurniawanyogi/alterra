package com.clean.architecture.model;

import com.clean.architecture.constant.AppConstant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public class BaseModel {
    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @Column(name = "is_deleted")
    private boolean isDeleted;

    @PrePersist()
    void onCreate(){
        this.createdAt = LocalDate.now();
        this.createdBy = AppConstant.CREATED_BY_SYSTEM;
        this.isDeleted = false;
    }

    @PostPersist()
    void onUpdate(){
        this.updatedAt = LocalDate.now();
    }

}

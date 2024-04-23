package com.timetable.entities;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="schedules")
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties({})
public class ScheduleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long subjectId;
    @Column(name = "subjectname")
    private String subjectname;
    @Column(name = "numberofcredits")    
    private int numberofcredits;
 	@Column(name = "dayofweek")    
    private int dayofweek;
    @Column(name = "lesson")   
    private String lesson ;
    @Column(name = "location")   
    private String location ;
    @Column(name = "dayofsubject")  
    private String dayofsubject;
    @Column(name = "type")  
    private String type;
    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private UserEntity user;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }

	

  
}

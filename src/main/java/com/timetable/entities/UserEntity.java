package com.timetable.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties({"roles"})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;
    @Column(unique = true)
    private String email;
    private String password;
    @Column(name = "phone_number")
    private String phoneNumber;
    private String status;
    @Column(name = "display_name")
    private String displayName;
    @Column(name = "refresh_token")
    private String refreshToken;
    @Column(name = "user_code")
    private String userCode;
    @Column(name = "created_at")
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updatedAt;
    @OneToMany(mappedBy="user")
    private List<ScheduleEntity> subjects;
    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<RoleEntity> roles = new HashSet<>();
    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
   

	@Override
	public String toString() {
		return "UserEntity [userId=" + userId + ", email=" + email + ", password=" + password + ", phoneNumber="
				+ phoneNumber + ", status=" + status + ", displayName=" + displayName + ", refreshToken=" + refreshToken
				+ ", userCode=" + userCode + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + ", roles="
				+ roles + "]";
	}

	public UserEntity(Long userId, String email, String password, String phoneNumber, String status, String displayName,
			String userCode) {
		this.email = email;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.status = status;
		this.displayName = displayName;
		this.userCode = userCode;
	}


}

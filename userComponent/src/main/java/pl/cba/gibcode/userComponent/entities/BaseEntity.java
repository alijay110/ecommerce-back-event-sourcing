package pl.cba.gibcode.userComponent.entities;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id", nullable = false)
	protected Long id;

	@Column(name = "version")
	@Version
	private Long version;

	@Column(name = "created_on")
	@CreatedDate
	private LocalDateTime createdOn;

	@Column(name = "created_by")
	@CreatedBy
	private String createdBy;

	@Column(name = "updated_on")
	@LastModifiedDate
	private LocalDateTime updatedOn;

	@Column(name = "updated_by")
	@LastModifiedBy
	private String updatedBy;

	@Column(name = "deleted")
	private boolean deleted = false;

	BaseEntity() {
	}

	public Long getId() {
		return id;
	}

	public Long getVersion() {
		return version;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public LocalDateTime getUpdatedOn() {
		return updatedOn;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public boolean isDeleted() {
		return deleted;
	}
}
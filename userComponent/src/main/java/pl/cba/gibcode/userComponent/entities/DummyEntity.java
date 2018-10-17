package pl.cba.gibcode.userComponent.entities;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "dummy_entities")
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE dummy_entities SET deleted = true WHERE id = ? and version = ?")
public class DummyEntity extends BaseEntity {

	@Column(name = "attribute", unique = true, nullable = false)
	@NotNull
	String attribute;

	public DummyEntity() {
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
}


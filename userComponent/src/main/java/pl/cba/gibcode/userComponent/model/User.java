package pl.cba.gibcode.userComponent.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import pl.cba.gibcode.modelLibrary.model.UserType;
import pl.cba.gibcode.userComponent.entities.BaseEntity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@Table(name = "users")
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@Where(clause = "deleted='false'")
@SQLDelete(sql = "UPDATE users SET deleted = true WHERE id = ? and version = ?")
public class User extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String username;

	@ElementCollection(targetClass = UserType.class)
	@Enumerated(EnumType.STRING)
	@CollectionTable(name = "user_usertype")
	@Column(name = "user_type")
	private Set<UserType> userTypes = new HashSet<>();

}

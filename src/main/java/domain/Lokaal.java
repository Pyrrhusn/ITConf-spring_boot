package domain;

import java.io.Serializable;

import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EqualsAndHashCode(of = "naam")
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(toBuilder = true)
@Getter
@Setter
@ToString
@JsonPropertyOrder({"lokaal_id", "lokaal_naam", "capaciteit"})
public class Lokaal implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("lokaal_id")
	private Long id;
	
	@Column(nullable = false, unique = true)
	@NotBlank(message = "{lokaal.naam.notBlank}")
	@Pattern(regexp = "^[a-zA-Z]?\\d{3}$", message = "{lokaal.naam.pattern}")
	@JsonProperty("lokaal_naam")
	private String naam;
	
	@NotNull(message = "{lokaal.capaciteit.notNull}")
	@Range(min = 1, max = 50, message = "{lokaal.capaciteit.range}")
	private Integer capaciteit;
	
}

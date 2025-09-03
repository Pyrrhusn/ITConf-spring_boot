package domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@EqualsAndHashCode(of = "naam")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Spreker implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@NotBlank(message = "{spreker.naam.notBlank}")
	@JsonProperty("spreker_naam")
	private String naam;
	
}

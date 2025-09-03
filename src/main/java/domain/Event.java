package domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.format.annotation.NumberFormat;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import utils.LocalDateDeserializer;
import utils.LocalDateSerializer;
import utils.LocalTimeDeserializer;
import utils.LocalTimeSerializer;
import validator.CijferCodeLengte;
import validator.EventDatumInPeriode;
import validator.ValidEventBeamer;

@Entity
@EqualsAndHashCode(of = {"naam", "datum"})
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
@JsonPropertyOrder({"event_id", "naam", "beschrijving", "sprekers", "lokaal", "datum",
					"startTijdstip", "eindTijdstip", "beamercode", "beamercheck", "prijs"})
@ValidEventBeamer
public class Event implements Serializable {
	 
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonProperty("event_id")
	private Long id;
	
	@NotBlank(message = "{event.naam.notBlank}")
	@Pattern(regexp = "^[a-zA-Z].*", message = "{event.naam.pattern}")
	private String naam;
	
	private String beschrijving;
	
	@NotEmpty(message = "{event.sprekers.notEmpty}")
	@Size(min = 1, max = 3, message = "{event.sprekers.size}")
	@ManyToMany
	@Valid // recursive validation -- notBlank op naam in spreker
	private List<Spreker> sprekers;
	
	@ManyToOne
	@NotNull(message = "{event.lokaal.notNull}")
	private Lokaal lokaal;
	
	@NotNull(message = "{event.datum.notNull}")
	@EventDatumInPeriode(begin = "2025-04-14", eind = "2025-04-18", message = "{event.datum.periode}")
	@DateTimeFormat(iso = ISO.DATE) //geen dateformatter voor compatability with browsers
	@JsonSerialize(using = LocalDateSerializer.class)
	@JsonDeserialize(using = LocalDateDeserializer.class)
	private LocalDate datum;
	
	@NotNull(message = "{event.startTijdstip.notNull}")
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	private LocalTime startTijdstip;
	
	@NotNull(message = "{event.eindTijdstip.notNull}")
	@JsonSerialize(using = LocalTimeSerializer.class)
	@JsonDeserialize(using = LocalTimeDeserializer.class)
	private LocalTime eindTijdstip;
	
	@NotBlank(message = "{event.beamercode.notBlank}")
	@CijferCodeLengte(lengte = 4, message = "{event.beamercode.cijfers}")
	private String beamercode;
	
	@NotBlank(message = "{event.beamercheck.notBlank}")
	@CijferCodeLengte(lengte = 2, message = "{event.beamercheck.cijfers}")
	private String beamercheck;
	
	@NotNull(message = "{event.prijs.notNull}")
	@DecimalMin(value = "9.99", message = "{event.prijs.decimalMin}")
	@DecimalMax(value = "100", message = "{event.prijs.decimalMax}", inclusive = false)
	@NumberFormat(pattern="#,##0.00")
	private Double prijs;

}

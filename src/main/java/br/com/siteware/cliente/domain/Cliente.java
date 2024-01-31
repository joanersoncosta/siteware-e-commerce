package br.com.siteware.cliente.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import br.com.siteware.cliente.application.api.ClienteNovoRequest;
import br.com.siteware.cliente.domain.enuns.Sexo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Document(collection = "Cliente")
public class Cliente {

	@Id
	private UUID idCliente;
	@NotBlank
	private String nome;
	@Email
	@NotNull
	@Indexed(unique = true)
	private String email;
	@NotNull
	private Sexo sexo;
	@NotNull
	private LocalDate dataNascimento;

	private LocalDateTime momentoDoDacastro;
	private LocalDateTime dataHoraDaultimaAlteracao;

	public Cliente(ClienteNovoRequest clienteRequest) {
		this.idCliente = UUID.randomUUID();
		this.nome = clienteRequest.getNome();
		this.email = clienteRequest.getEmail();
		this.sexo = clienteRequest.getSexo();
		this.dataNascimento = LocalDate.parse(clienteRequest.getDataNascimento());
		this.momentoDoDacastro = LocalDateTime.now();
	}

}

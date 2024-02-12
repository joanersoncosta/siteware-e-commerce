package br.com.siteware.cliente.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.siteware.ClienteDataHelper;
import br.com.siteware.cliente.application.api.ClienteDetalhadoResponse;
import br.com.siteware.cliente.application.api.ClienteIdResponse;
import br.com.siteware.cliente.application.api.ClienteNovoRequest;
import br.com.siteware.cliente.application.repository.ClienteRepository;
import br.com.siteware.cliente.domain.Cliente;

@ExtendWith(MockitoExtension.class)
class ClienteApplicationServiceTest{
	
	@InjectMocks
	private ClienteApplicationService clienteApplicationService;
	@Mock
	private ClienteRepository clienteRepository;

	@Test
	@DisplayName("Salva Cliente - com dados validos - retorna ClienteIdResponse")
	void criaNovoCliente() {
		ClienteNovoRequest request = ClienteDataHelper.createClienteRequest();
	
		when(clienteRepository.salva(any())).thenReturn(new Cliente(request));
		ClienteIdResponse response = clienteApplicationService.criaNovoCliente(request);
	
		verify(clienteRepository, times(1)).salva(any());
		
		assertThat(response).isNotNull();
		assertThat(ClienteIdResponse.class).isEqualTo(response.getClass());
	}

	@Test
	@DisplayName("Busca Cliente por id - com dados validos - retorna dados do cliente")
	void buscaClientePorId() {
		Cliente cliente = ClienteDataHelper.createCliente();
		String email = cliente.getEmail();
		UUID idCliente = cliente.getIdCliente();
		
		when(clienteRepository.detalhaClientePorId(any())).thenReturn(Optional.of(cliente));
		when(clienteRepository.detalhaClientePorEmail(any())).thenReturn(cliente);

		ClienteDetalhadoResponse response = clienteApplicationService.buscaClientePorId(email, idCliente);

		verify(clienteRepository, times(1)).detalhaClientePorEmail(email);
		verify(clienteRepository, times(1)).detalhaClientePorId(idCliente);
		
		assertNotNull(response);
		assertEquals(response.getIdCliente(), cliente.getIdCliente());
		assertThat(ClienteDetalhadoResponse.class).isEqualTo(response.getClass());
	}
}

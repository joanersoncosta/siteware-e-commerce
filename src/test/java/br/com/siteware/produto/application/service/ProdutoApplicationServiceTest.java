package br.com.siteware.produto.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.siteware.ClienteDataHelper;
import br.com.siteware.ProdutoDataHelper;
import br.com.siteware.categoria.domain.Categoria;
import br.com.siteware.cliente.application.repository.ClienteRepository;
import br.com.siteware.cliente.domain.Cliente;
import br.com.siteware.produto.application.api.AlteraPromocaoProdutoRequest;
import br.com.siteware.produto.application.api.EditaProdutoRequest;
import br.com.siteware.produto.application.api.ProdutoDetalhadoResponse;
import br.com.siteware.produto.application.api.ProdutoIdResponse;
import br.com.siteware.produto.application.api.ProdutoListResponse;
import br.com.siteware.produto.application.api.ProdutoRequest;
import br.com.siteware.produto.application.repository.ProdutoRepository;
import br.com.siteware.produto.domain.Produto;
import br.com.siteware.produto.domain.enuns.PromocaoProduto;
import br.com.siteware.produto.domain.enuns.PromocaoProdutoStatus;

@ExtendWith(MockitoExtension.class)
class ProdutoApplicationServiceTest {

	@InjectMocks
	private ProdutoApplicationService produtoApplicationService;
	@Mock
	private ProdutoRepository produtoRepository;
	@Mock
	private ClienteRepository clienteRepository;

	@Test
	@DisplayName("Cadastra Produto com sucesso")
	void cadastraProduto_comDadosValidos_retornaProdutoIdResponse() {
		Cliente cliente = ClienteDataHelper.createCliente();
		ProdutoRequest request = ProdutoDataHelper.createProdutorequest();
		String email = cliente.getEmail();
		
		when(clienteRepository.detalhaClientePorEmail(any())).thenReturn(cliente);
		when(produtoRepository.salva(any())).thenReturn(new Produto(request));
		
		ProdutoIdResponse response = produtoApplicationService.cadastraProduto(email, request);
	
		verify(clienteRepository, times(1)).detalhaClientePorEmail(email);
		verify(produtoRepository, times(1)).salva(any());

		assertThat(response).isNotNull();
		assertEquals(ProdutoIdResponse.class, response.getClass());
	}
	
	@Test
	@DisplayName("Busca Produto Por Id")
	void buscaProduto_comIdValido_retornaProdutoDetalhadoResponse() {
		Produto produto = ProdutoDataHelper.createProduto();
		UUID idProduto = produto.getIdProduto();
		when(produtoRepository.detalhaProdutoPorId(any())).thenReturn(Optional.of(produto));
		
		ProdutoDetalhadoResponse response = produtoApplicationService.buscaProdutoPorId(idProduto);
	
		verify(produtoRepository, times(1)).detalhaProdutoPorId(idProduto);

		assertThat(response).isNotNull();
		assertEquals(ProdutoDetalhadoResponse.class, response.getClass());
	}

	@Test
	@DisplayName("Busca Todos os Produto")
	void listaProduto_retornaListaDeProdutos() {
		List<Produto> produtos = ProdutoDataHelper.createListProduto();
		
		when(produtoRepository.buscaTodosOsProdutos()).thenReturn(produtos);
		
		List<ProdutoListResponse> response = produtoApplicationService.buscaTodosOsProdutos();
	
		verify(produtoRepository, times(1)).buscaTodosOsProdutos();

		assertThat(response).isNotEmpty();
		assertEquals(4, response.size());
		assertEquals(produtos.get(0).getIdProduto(), response.get(0).getIdProduto());
	}
	
	@Test
	@DisplayName("Busca Produtos Por Categoria")
	void listaProdutoPorCategoria_retornaListaDeProdutos() {
		List<Produto> produtos = ProdutoDataHelper.createListProdutoCategoria();
		String categoria = "ELETRONICO";
		when(produtoRepository.buscaProdutosPorCategoria(any())).thenReturn(produtos);
		
		List<ProdutoListResponse> response = produtoApplicationService.buscaProdutosPorCategoria(categoria);
	
		verify(produtoRepository, times(1)).buscaProdutosPorCategoria(Categoria.ELETRONICO);

		assertThat(response).isNotEmpty();
		assertEquals(4, response.size());
		assertEquals(Categoria.ELETRONICO, response.get(0).getCategoria());
	}

	@Test
	@DisplayName("Busca Todos os Produto")
	void listaProdutoPorNome_retornaListaDeProdutos() {
		List<Produto> produtos = ProdutoDataHelper.createListProduto();
		String nome = "Produto";
		when(produtoRepository.buscaTodosOsProdutos()).thenReturn(produtos);
		
		List<ProdutoListResponse> response = produtoApplicationService.buscaProdutosPorNome(nome);
	
		verify(produtoRepository, times(1)).buscaTodosOsProdutos();

		assertThat(response).isNotEmpty();
		assertEquals(4, response.size());
	}
	
	@Test
	@DisplayName("Deleta Produto Por Id")
	void deletaProduto_comIdValido_semRetorno() {
		Produto produto = ProdutoDataHelper.createProduto();
		UUID idProduto = produto.getIdProduto();
		
		when(produtoRepository.detalhaProdutoPorId(any())).thenReturn(Optional.of(produto));
		doNothing().when(produtoRepository).deletaProduto(produto);
		
		produtoApplicationService.deletaProdutoPorId(idProduto);
	
		verify(produtoRepository, times(1)).detalhaProdutoPorId(idProduto);
		verify(produtoRepository, times(1)).deletaProduto(produto);
	}
	
	@Test
	@DisplayName("Edita Produto")
	void editaProduto_comDadosValidos_alteraProduto() {
		Cliente cliente = ClienteDataHelper.createCliente();
		Produto produto = ProdutoDataHelper.createProduto();
		EditaProdutoRequest request = ProdutoDataHelper.editaProdutoRequest();
		String email = cliente.getEmail();
		UUID idProduto = produto.getIdProduto();

		when(clienteRepository.detalhaClientePorEmail(any())).thenReturn(cliente);
		when(produtoRepository.detalhaProdutoPorId(any())).thenReturn(Optional.of(produto));

		produtoApplicationService.editaProdutoPorId(email, idProduto, request);
	
		verify(clienteRepository, times(1)).detalhaClientePorEmail(email);
		verify(produtoRepository, times(1)).detalhaProdutoPorId(idProduto);
		verify(produtoRepository, times(1)).editaProduto(produto, request);
	}
	
	@Test
	@DisplayName("Altera promocao do Produto")
	void alteraPromocaoDoProduto_comPromocaoValida_alteraPromocao() {
		Cliente cliente = ClienteDataHelper.createCliente();
		Produto produto = mock(Produto.class);
		AlteraPromocaoProdutoRequest request = ProdutoDataHelper.alteraPromocaoProdutoRequest();
		String email = cliente.getEmail();
		UUID idProduto = ProdutoDataHelper.createProduto().getIdProduto();
		PromocaoProduto promocao = PromocaoProduto.LEVE_2_PAGUE_1;

		when(clienteRepository.detalhaClientePorEmail(any())).thenReturn(cliente);
		when(produtoRepository.detalhaProdutoPorId(any())).thenReturn(Optional.of(produto));
		doNothing().when(produtoRepository).alteraPromocaoDoProduto(produto, promocao);

		produtoApplicationService.alteraPromocaoDoProdutoPorId(email, idProduto, request);
	
		verify(clienteRepository, times(1)).detalhaClientePorEmail(email);
		verify(produtoRepository, times(1)).detalhaProdutoPorId(idProduto);
		verify(produto, times(1)).alteraStatusPromocao(any());
		verify(produtoRepository, times(1)).alteraPromocaoDoProduto(produto, promocao);
	}
	
	@Test
	@DisplayName("Busca Produto com Promocao")
	void listaProdutoComPromocao_retornaListaDeProdutos() {
		List<Produto> produtos = ProdutoDataHelper.createListProdutoComPromocao();
		when(produtoRepository.buscaProdutoComPromocao()).thenReturn(produtos);
		
		List<ProdutoListResponse> response = produtoApplicationService.buscaProdutoComPromocao();
	
		verify(produtoRepository, times(1)).buscaProdutoComPromocao();

		assertThat(response).isNotEmpty();
		assertEquals(4, response.size());
		assertEquals(PromocaoProdutoStatus.ATIVO, response.get(0).getStatusPromocao());
	}
}

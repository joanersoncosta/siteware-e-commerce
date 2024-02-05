package br.com.siteware.produto.application.api;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/v1/produto")
public interface ProdutoAPI {

	@PostMapping(path = "/cadastra")
	@ResponseStatus(value = HttpStatus.CREATED)
	ProdutoIdResponse cadastraProduto(@PathParam(value = "email") String email, @RequestBody @Valid ProdutoRequest produtoRequest);

	@GetMapping(path = "/{idProduto}/busca")
	@ResponseStatus(value = HttpStatus.OK)
	ProdutoDetalhadoResponse buscaProdutoPorId(@PathVariable(value = "idProduto") UUID idProduto);

	@GetMapping(path = "/busca-produtos")
	@ResponseStatus(value = HttpStatus.OK)
	List<ProdutoListResponse> buscaTodosProdutos();

}
package br.com.alurafood.pagamentos.controller;

import br.com.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alurafood.pagamentos.service.PagamentoServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RequestMapping("/pagamentos")
@RestController
public class PagamentoController {

    private final PagamentoServiceImpl pagamentoService;

    @GetMapping
    public ResponseEntity<Page<PagamentoDTO>> getAllPagamentos(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(pagamentoService.obterTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PagamentoDTO> getPagamentoById(@PathVariable Long id) {
        PagamentoDTO pagamentoDTO = pagamentoService.obterPorId(id);
        return ResponseEntity.ok(pagamentoDTO);
    }

    @PostMapping
    public ResponseEntity<PagamentoDTO> cadastrar(@RequestBody @Valid PagamentoDTO dto, UriComponentsBuilder uriBuilder) {
        PagamentoDTO pagamento = pagamentoService.criarPagamento(dto);
        URI endereco = uriBuilder.path("/pagamentos/{id}").buildAndExpand(pagamento.getId()).toUri();

        return ResponseEntity.created(endereco).body(pagamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PagamentoDTO> atualizar(@PathVariable @NotNull Long id, @RequestBody @Valid PagamentoDTO dto) {
        PagamentoDTO atualizado = pagamentoService.atualizarPagamento(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PagamentoDTO> remover(@PathVariable @NotNull Long id) {
        pagamentoService.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/confirmar")
    public void confirmarPagamento(@PathVariable @NotNull Long id){
        pagamentoService.confirmarPagamento(id);
    }
}

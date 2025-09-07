package br.com.alurafood.pagamentos.dto;

import br.com.alurafood.pagamentos.model.Status;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PagamentoDTO {

    private Long id;
    private Double valor;
    private String nome;
    private String numero;
    private String expiracao;
    private String codigo;
    private Status status;
    private Long pedidoId;
    private Long formaDePagamentoId;

}

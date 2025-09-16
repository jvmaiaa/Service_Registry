package br.com.alurafood.pagamentos.service;

import br.com.alurafood.pagamentos.dto.PagamentoDTO;
import br.com.alurafood.pagamentos.http.PedidoClient;
import br.com.alurafood.pagamentos.model.Pagamento;
import br.com.alurafood.pagamentos.model.Status;
import br.com.alurafood.pagamentos.repository.PagamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PagamentoServiceImpl {

    private final PagamentoRepository pagamentoRepository;
    private final ModelMapper modelMapper;
    private final PedidoClient pedido;

    public Page<PagamentoDTO> obterTodos(Pageable pageable) {
        return pagamentoRepository.findAll(pageable)
                .map(p -> modelMapper.map(p, PagamentoDTO.class));
    }

    public PagamentoDTO obterPorId(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public PagamentoDTO criarPagamento(PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
        pagamento.setStatus(Status.CRIADO);
        pagamento = pagamentoRepository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public PagamentoDTO atualizarPagamento(Long id, PagamentoDTO pagamentoDTO) {
        Pagamento pagamento = modelMapper.map(pagamentoDTO, Pagamento.class);
        pagamento.setId(id);
        pagamentoRepository.save(pagamento);

        return modelMapper.map(pagamento, PagamentoDTO.class);
    }

    public void excluirPagamento(Long id) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException());
        pagamentoRepository.delete(pagamento);
    }

    public void confirmarPagamento(Long id){
        Optional<Pagamento> pagamento = pagamentoRepository.findById(id);

        if (!pagamento.isPresent()) {
            throw new EntityNotFoundException();
        }

        pagamento.get().setStatus(Status.CONFIRMADO);
        pagamentoRepository.save(pagamento.get());
        pedido.atualizaPagamento(pagamento.get().getPedidoId());
    }
}

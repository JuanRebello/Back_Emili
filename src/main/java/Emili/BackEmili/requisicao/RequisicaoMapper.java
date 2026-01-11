package Emili.BackEmili.requisicao;

import Emili.BackEmili.status.StatusTipo;
import org.springframework.stereotype.Component;

@Component
public class RequisicaoMapper {
    
    // Mapeia DTO de criação para entidade (sem usuário e sem dataCriacao)
    public RequisicaoModel fromCreateDto(RequisicaoCreateDTO dto) {
        RequisicaoModel model = new RequisicaoModel();
        model.setModais(dto.getModais());
        model.setDescricao(dto.getDescricao());
        return model;
    }



    public RequisicaoResponseDTO toResponseDto(RequisicaoModel model) {
        RequisicaoResponseDTO dto = new RequisicaoResponseDTO();
        dto.setIdRequisicao(model.getIdRequisicao());
        dto.setIdUsuario(model.getUsuario() != null ? model.getUsuario().getId() : null);
        dto.setDataCriacao(model.getDataCriacao());
        dto.setDescricao(model.getDescricao());
        dto.setModais(model.getModais());
        
        return dto;
    }

    // Sobrecarga para preencher status atual quando disponível no Service
    public RequisicaoResponseDTO toResponseDtoWithStatus(RequisicaoModel model, StatusTipo statusAtual) {
        RequisicaoResponseDTO dto = toResponseDto(model);
        dto.setStatus(statusAtual);
        return dto;
    }

}

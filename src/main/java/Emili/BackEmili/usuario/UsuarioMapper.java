package Emili.BackEmili.usuario;

import org.springframework.stereotype.Component;

@Component
public class UsuarioMapper {

    // Mapeia DTO de criação para entidade (sem lidar com senha hash ou role)
    public UsuarioModel fromCreateDto(UsuarioCreateDTO dto) {
        UsuarioModel model = new UsuarioModel();
        model.setNome(dto.getNome());
        model.setEmail(dto.getEmail());
        model.setIdade(dto.getIdade());
        return model;
    }

    // Atualiza a entidade existente com dados do DTO de atualização (campos permitidos)
    public void updateModelFromUpdateDto(UsuarioModel model, UsuarioUpdateDTO dto) {
        model.setNome(dto.getNome());
        model.setEmail(dto.getEmail());
        model.setIdade(dto.getIdade());
    }

    // Mapeia entidade para DTO de resposta (sem senha)
    public UsuarioResponseDTO toResponseDto(UsuarioModel model) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setId(model.getId());
        dto.setNome(model.getNome());
        dto.setEmail(model.getEmail());
        dto.setIdade(model.getIdade());
        dto.setRole(model.getRole());
        return dto;
    }
}

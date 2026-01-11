package Emili.BackEmili.usuario;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioMapper usuarioMapper;
    private final PasswordEncoder passwordEncoder;

    public UsuarioService(UsuarioRepository usuarioRepository, UsuarioMapper usuarioMapper, PasswordEncoder passwordEncoder){
        this.usuarioRepository = usuarioRepository;
        this.usuarioMapper = usuarioMapper;
        this.passwordEncoder = passwordEncoder;
    }

    // Criar Usuario (role padrão USER e hash de senha)
    public UsuarioResponseDTO criarUsuario (UsuarioCreateDTO usuarioDTO){
        UsuarioModel usuario = usuarioMapper.fromCreateDto(usuarioDTO);
        usuario.setRole(Role.USER);
        usuario.setPasswordHash(passwordEncoder.encode(usuarioDTO.getPassword()));
        usuario = usuarioRepository.save(usuario);
        return usuarioMapper.toResponseDto(usuario);
    }

    // Listar Usuarios
    public List<UsuarioResponseDTO> listarUsuarios(){
        List<UsuarioModel> usuarios = usuarioRepository.findAll();
        return usuarios.stream()
                .map(usuarioMapper::toResponseDto)
                .toList();
    }

    // Listar Usuario por ID
    public UsuarioResponseDTO listarUsuarioPorId(Long id){
        Optional<UsuarioModel> usuarioPorId = usuarioRepository.findById(id);
        return usuarioPorId.map(usuarioMapper::toResponseDto).orElse(null);
    }

    // Atualizar Usuario por ID (apenas campos permitidos; re-hash se senha enviada)
    public UsuarioResponseDTO atualizarUsuarioPorId(Long id, UsuarioUpdateDTO dto){
        Optional<UsuarioModel> usuarioPorId = usuarioRepository.findById(id);
        if (usuarioPorId.isPresent()) {
            UsuarioModel existente = usuarioPorId.get();
            usuarioMapper.updateModelFromUpdateDto(existente, dto);
            if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
                existente.setPasswordHash(passwordEncoder.encode(dto.getPassword()));
            }
            UsuarioModel salvo = usuarioRepository.save(existente);
            return usuarioMapper.toResponseDto(salvo);
        }
        return null;
    }

    public void deletarUsuarioPorId(Long id){
        usuarioRepository.deleteById(id);
    }
}

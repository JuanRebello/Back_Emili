package Emili.BackEmili.usuario;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    // Criar Usuario
    public UsuarioModel criarUsuario (UsuarioModel usuarioModel){
        return usuarioRepository.save(usuarioModel);
    }

    // Listar Usuario
    public List<UsuarioModel> listarUsuarios(){
        return usuarioRepository.findAll();
    }

    // Lista Usuraio do po ID
    public UsuarioModel listarUsuarioPorId(Long id){
        Optional<UsuarioModel> usuarioPorId = usuarioRepository.findById(id);
        return usuarioPorId.orElse(null);
    }

    // Deletar Usuario por ID
    public void deletarUsuarioPorId(Long id){
        usuarioRepository.deleteById(id);
    }



}

package Emili.BackEmili.usuario;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {


    private UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping("/boasVindas")
    public String boasVindas(){
        return "Essa é minha primeira mensagem nessa rota";
    }

    @PostMapping("/criar")
    public UsuarioModel criarUsuario(@RequestBody UsuarioModel usuario){
        return usuarioService.criarUsuario(usuario);
    }

    @GetMapping("/listar")
    public List<UsuarioModel> listarUsuario(){
        return usuarioService.listarUsuarios();
    }

    @GetMapping("/listar/{id}")
    public UsuarioModel listarUsuarioPorId(@PathVariable Long id){
        return usuarioService.listarUsuarioPorId(id);
    }

    @DeleteMapping("/deletar/{id}")
    public void deletarUsuarioPorId(@PathVariable Long id){
        usuarioService.deletarUsuarioPorId(id);
    }

}
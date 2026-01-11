package Emili.BackEmili.usuario;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {
    
    private Long id;
    private String nome;
    private String email;
    private int idade;
    private String password;
    private Role role;
}

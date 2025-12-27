package Emili.BackEmili.usuario;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tb_usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(unique = true)
    private String email;

    @Column(name = "idade")
    private int idade;

    private String passwordHash;

    @Enumerated(EnumType.STRING)
    private  Role role;

    


}

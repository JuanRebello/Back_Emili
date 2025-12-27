package Emili.BackEmili.requisicao;

import Emili.BackEmili.usuario.UsuarioModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "t_requisicao")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequisicaoModel {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long IdRequisicao;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioModel usuario;


    private LocalDateTime dataCriacao;

    private String descricao;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "t_requisicao_modal",
            joinColumns = @JoinColumn(name = "id_requisicao")
    )
    @Column(name = "modal")
    private Set<Modal> modais;




}

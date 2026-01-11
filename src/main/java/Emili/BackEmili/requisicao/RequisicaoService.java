package Emili.BackEmili.requisicao;

import Emili.BackEmili.usuario.UsuarioModel;
import Emili.BackEmili.usuario.UsuarioRepository;
import Emili.BackEmili.status.RequisicaoStatus;
import Emili.BackEmili.status.RequisicaoStatusRepository;
import Emili.BackEmili.status.StatusTipo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequisicaoService {

    private final RequisicaoRepository requisicaoRepository;
    private final RequisicaoMapper requisicaoMapper;
    private final UsuarioRepository usuarioRepository;
    private final RequisicaoStatusRepository requisicaoStatusRepository;

    public RequisicaoService(RequisicaoRepository requisicaoRepository,
                             RequisicaoMapper requisicaoMapper,
                             UsuarioRepository usuarioRepository,
                             RequisicaoStatusRepository requisicaoStatusRepository) {
        this.requisicaoRepository = requisicaoRepository;
        this.requisicaoMapper = requisicaoMapper;
        this.usuarioRepository = usuarioRepository;
        this.requisicaoStatusRepository = requisicaoStatusRepository;
    }

    // Create request: validate user, set creation date, and persist
    @Transactional
    public RequisicaoResponseDTO criarRequisicao(RequisicaoCreateDTO dto) {
        Long usuarioId = java.util.Objects.requireNonNull(dto.getUsuarioId(), "ID do usuário é obrigatório");
        UsuarioModel usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + usuarioId));

        RequisicaoModel requisicao = requisicaoMapper.fromCreateDto(dto);
        requisicao.setUsuario(usuario);
        requisicao.setDataCriacao(LocalDateTime.now());

        requisicao = requisicaoRepository.save(requisicao);

        return requisicaoMapper.toResponseDto(requisicao);
    }

    public RequisicaoResponseDTO listarRequisicaoPorId(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("ID da requisição é obrigatório");
        }
        RequisicaoModel requisicao = requisicaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requisição não encontrada: " + id));

        RequisicaoStatus ultimoStatus = requisicaoStatusRepository.findTopByRequisicaoOrderByDataStatusDesc(requisicao);
        if (ultimoStatus != null) {
            return requisicaoMapper.toResponseDtoWithStatus(requisicao, ultimoStatus.getStatus());
        }
        return requisicaoMapper.toResponseDto(requisicao);
    }

    public List<RequisicaoResponseDTO> listarRequisicoes(){
        List<RequisicaoModel> requisicoes = requisicaoRepository.findAll();
        return requisicoes.stream()
                .map(r -> {
                    RequisicaoStatus ultimo = requisicaoStatusRepository.findTopByRequisicaoOrderByDataStatusDesc(r);
                    return (ultimo != null)
                            ? requisicaoMapper.toResponseDtoWithStatus(r, ultimo.getStatus())
                            : requisicaoMapper.toResponseDto(r);
                })
                .collect(Collectors.toList());
    }

    // Update status with validation and history
    @Transactional
    public RequisicaoResponseDTO atualizarRequisicaoPorId(Long id, RequisicaoUpdateDTO dto){
        if (id == null) {
            throw new IllegalArgumentException("ID da requisição é obrigatório");
        }
        RequisicaoModel requisicao = requisicaoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Requisição não encontrada: " + id));

        RequisicaoStatus atual = requisicaoStatusRepository.findTopByRequisicaoOrderByDataStatusDesc(requisicao);
        StatusTipo statusAtual = atual != null ? atual.getStatus() : null;
        StatusTipo novoStatus = dto.getStatus();

        if (statusAtual == StatusTipo.CANCELADA || statusAtual == StatusTipo.FINALIZADA) {
            throw new IllegalStateException("Transição de status não permitida a partir de " + statusAtual);
        }

        RequisicaoStatus registro = new RequisicaoStatus();
        registro.setRequisicao(requisicao);
        registro.setStatus(novoStatus);
        registro.setDataStatus(LocalDateTime.now());
        requisicaoStatusRepository.save(registro);

        return requisicaoMapper.toResponseDtoWithStatus(requisicao, novoStatus);
    }

    @Transactional
    public void deletarRequisicaoPorId(Long id){
        if (id == null) {
            throw new IllegalArgumentException("ID da requisição é obrigatório");
        }
        boolean existe = requisicaoRepository.existsById(id);
        if (!existe){
            throw new IllegalArgumentException("Requisição não encontrada: " + id);
        }
        requisicaoRepository.deleteById(id);
    }
}













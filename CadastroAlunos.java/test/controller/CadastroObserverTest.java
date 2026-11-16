package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exception.AlunoNaoEncontradoException;
import exception.DadosInvalidosException;
import exception.MatriculaDuplicadaException;
import java.util.ArrayList;
import java.util.List;
import model.Aluno;
import observer.CadastroObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes do padrão Observer no CadastroAlunos: quando os observers são avisados
 * e quando não são.
 */
@DisplayName("CadastroAlunos (Observer)")
class CadastroObserverTest {

    private CadastroAlunos cadastro;

    // guarda uma cópia da lista recebida a cada aviso
    private List<List<Aluno>> avisos;
    private CadastroObserver observerDeTeste;

    @BeforeEach
    void prepararCadastroComObserver() {
        cadastro = new CadastroAlunos();
        avisos = new ArrayList<>();
        observerDeTeste = alunos -> avisos.add(new ArrayList<>(alunos));
        cadastro.adicionarObserver(observerDeTeste);
    }

    @Test
    @DisplayName("cadastrar avisa o observer uma vez, com a lista já atualizada")
    void cadastrarAvisaObserver() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        assertEquals(1, avisos.size());
        assertEquals(1, avisos.get(0).size());
        assertEquals("Maria", avisos.get(0).get(0).getNome());
    }

    @Test
    @DisplayName("atualizar avisa o observer com os dados novos")
    void atualizarAvisaObserver() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        cadastro.atualizarAluno(1, "Maria Souza", "2026001", "CC", 9.0);

        assertEquals(2, avisos.size());
        assertEquals("Maria Souza", avisos.get(1).get(0).getNome());
    }

    @Test
    @DisplayName("deletar avisa o observer com a lista sem o aluno")
    void deletarAvisaObserver() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        cadastro.deletarAluno(1);

        assertEquals(2, avisos.size());
        assertTrue(avisos.get(1).isEmpty());
    }

    @Test
    @DisplayName("operações que dão erro não avisam o observer")
    void erroNaoAvisaObserver() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);
        avisos.clear();

        assertThrows(DadosInvalidosException.class, () -> cadastro.cadastrarAluno("", "2026002", "CC", 8.5));
        assertThrows(MatriculaDuplicadaException.class, () -> cadastro.cadastrarAluno("João", "2026001", "CC", 8.5));
        assertThrows(AlunoNaoEncontradoException.class, () -> cadastro.atualizarAluno(9, "A", "1", "CC", 1));
        assertThrows(AlunoNaoEncontradoException.class, () -> cadastro.deletarAluno(9));

        assertTrue(avisos.isEmpty());
    }

    @Test
    @DisplayName("carregar alunos do arquivo não avisa o observer (senão salvaria de volta o que acabou de ler)")
    void carregarNaoAvisaObserver() throws Exception {
        cadastro.carregarAlunos(List.of(new Aluno(1, "Maria", "2026001", "CC", 8.5)));

        assertTrue(avisos.isEmpty());
    }

    @Test
    @DisplayName("todos os observers registrados são avisados")
    void variosObservers() throws Exception {
        List<String> segundoObserver = new ArrayList<>();
        cadastro.adicionarObserver(alunos -> segundoObserver.add("avisado"));

        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        assertEquals(1, avisos.size());
        assertEquals(1, segundoObserver.size());
    }

    @Test
    @DisplayName("observer removido não é mais avisado")
    void removerObserver() throws Exception {
        cadastro.removerObserver(observerDeTeste);

        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        assertTrue(avisos.isEmpty());
    }

    @Test
    @DisplayName("a lista entregue ao observer não pode ser modificada")
    void listaDoObserverEImutavel() throws Exception {
        cadastro.adicionarObserver(alunos -> assertThrows(UnsupportedOperationException.class, alunos::clear));

        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        assertEquals(1, cadastro.listarAlunos().size());
    }
}

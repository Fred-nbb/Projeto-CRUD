package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exception.AlunoNaoEncontradoException;
import exception.DadosInvalidosException;
import exception.MatriculaDuplicadaException;
import java.util.List;
import model.Aluno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes da classe CadastroAlunos.
 * Cada teste começa com um cadastro vazio (método criarCadastroVazio)
 * e confere uma regra do CRUD ou uma validação.
 */
@DisplayName("CadastroAlunos")
class CadastroAlunosTest {

    private CadastroAlunos cadastro;

    @BeforeEach
    void criarCadastroVazio() {
        cadastro = new CadastroAlunos();
    }

    // ---------- cadastrar e listar ----------

    @Test
    @DisplayName("cadastrar gera IDs em sequência começando em 1")
    void cadastrarGeraIdsEmSequencia() throws Exception {
        Aluno maria = cadastro.cadastrarAluno("Maria", "2026001", "Ciência da Computação", 8.5);
        Aluno joao = cadastro.cadastrarAluno("João", "2026002", "Engenharia", 7.0);

        assertEquals(1, maria.getId());
        assertEquals(2, joao.getId());
    }

    @Test
    @DisplayName("listar devolve os alunos na ordem em que foram cadastrados")
    void listarMantemOrdemDeCadastro() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);
        cadastro.cadastrarAluno("João", "2026002", "SI", 7.0);
        cadastro.cadastrarAluno("Ana", "2026003", "CC", 9.0);

        List<Aluno> alunos = cadastro.listarAlunos();

        assertEquals(3, alunos.size());
        assertEquals("Maria", alunos.get(0).getNome());
        assertEquals("João", alunos.get(1).getNome());
        assertEquals("Ana", alunos.get(2).getNome());
    }

    @Test
    @DisplayName("listar devolve uma cópia: mexer na lista não altera o cadastro")
    void listarDevolveCopia() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        cadastro.listarAlunos().clear();

        assertEquals(1, cadastro.listarAlunos().size());
    }

    @Test
    @DisplayName("cadastrar tira os espaços do começo e do fim dos textos")
    void cadastrarTiraEspacos() throws Exception {
        Aluno aluno = cadastro.cadastrarAluno("  Maria  ", " 2026001 ", " CC ", 8.5);

        assertEquals("Maria", aluno.getNome());
        assertEquals("2026001", aluno.getMatricula());
        assertEquals("CC", aluno.getCurso());
    }

    // ---------- validações ----------

    @Test
    @DisplayName("não aceita nome vazio")
    void naoAceitaNomeVazio() {
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> cadastro.cadastrarAluno("   ", "2026001", "CC", 8.5));

        assertEquals("O campo nome não pode ficar vazio.", erro.getMessage());
    }

    @Test
    @DisplayName("não aceita nota menor que 0 nem maior que 10")
    void naoAceitaNotaForaDoIntervalo() {
        assertThrows(DadosInvalidosException.class, () -> cadastro.cadastrarAluno("Maria", "1", "CC", -0.5));
        assertThrows(DadosInvalidosException.class, () -> cadastro.cadastrarAluno("Maria", "1", "CC", 10.5));
    }

    @Test
    @DisplayName("aceita as notas limite 0 e 10")
    void aceitaNotasLimite() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 0);
        cadastro.cadastrarAluno("João", "2026002", "CC", 10);

        assertEquals(2, cadastro.listarAlunos().size());
    }

    @Test
    @DisplayName("não aceita ';' nos textos, porque ele separa as colunas do arquivo")
    void naoAceitaPontoEVirgula() {
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> cadastro.cadastrarAluno("Maria", "2026001", "CC;SI", 8.5));

        assertTrue(erro.getMessage().contains("curso"));
    }

    @Test
    @DisplayName("não aceita duas vezes a mesma matrícula")
    void naoAceitaMatriculaRepetida() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        MatriculaDuplicadaException erro = assertThrows(MatriculaDuplicadaException.class,
                () -> cadastro.cadastrarAluno("João", "2026001", "SI", 7.0));

        assertEquals("Já existe um aluno com a matrícula 2026001.", erro.getMessage());
        assertEquals(1, cadastro.listarAlunos().size());
    }

    // ---------- buscar, atualizar e deletar ----------

    @Test
    @DisplayName("buscar um ID que não existe lança AlunoNaoEncontradoException com mensagem útil")
    void buscarIdInexistente() {
        AlunoNaoEncontradoException erro = assertThrows(AlunoNaoEncontradoException.class,
                () -> cadastro.buscarPorId(42));

        assertEquals("Nenhum aluno encontrado com o ID 42.", erro.getMessage());
    }

    @Test
    @DisplayName("atualizar muda todos os dados do aluno")
    void atualizarMudaOsDados() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        cadastro.atualizarAluno(1, "Maria Souza", "2026009", "SI", 9.5);

        Aluno aluno = cadastro.buscarPorId(1);
        assertEquals("Maria Souza", aluno.getNome());
        assertEquals("2026009", aluno.getMatricula());
        assertEquals("SI", aluno.getCurso());
        assertEquals(9.5, aluno.getNota());
    }

    @Test
    @DisplayName("atualizar pode manter a própria matrícula")
    void atualizarMantendoAMatricula() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        cadastro.atualizarAluno(1, "Maria Souza", "2026001", "CC", 9.0);

        assertEquals("Maria Souza", cadastro.buscarPorId(1).getNome());
    }

    @Test
    @DisplayName("atualizar não deixa usar a matrícula de outro aluno")
    void atualizarComMatriculaDeOutroAluno() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);
        cadastro.cadastrarAluno("João", "2026002", "SI", 7.0);

        assertThrows(MatriculaDuplicadaException.class,
                () -> cadastro.atualizarAluno(2, "João", "2026001", "SI", 7.0));
        assertEquals("2026002", cadastro.buscarPorId(2).getMatricula());
    }

    @Test
    @DisplayName("atualizar libera a matrícula antiga para outro aluno")
    void atualizarLiberaMatriculaAntiga() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);
        cadastro.atualizarAluno(1, "Maria", "2026009", "CC", 8.5);

        Aluno joao = cadastro.cadastrarAluno("João", "2026001", "SI", 7.0);

        assertEquals("2026001", joao.getMatricula());
    }

    @Test
    @DisplayName("atualizar um ID que não existe lança AlunoNaoEncontradoException")
    void atualizarIdInexistente() {
        assertThrows(AlunoNaoEncontradoException.class,
                () -> cadastro.atualizarAluno(7, "Maria", "2026001", "CC", 8.5));
    }

    @Test
    @DisplayName("deletar remove o aluno e libera a matrícula")
    void deletarRemoveAluno() throws Exception {
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        cadastro.deletarAluno(1);

        assertTrue(cadastro.listarAlunos().isEmpty());
        assertThrows(AlunoNaoEncontradoException.class, () -> cadastro.buscarPorId(1));
        cadastro.cadastrarAluno("Ana", "2026001", "SI", 9.0); // matrícula pode ser usada de novo
    }

    @Test
    @DisplayName("deletar um ID que não existe lança AlunoNaoEncontradoException")
    void deletarIdInexistente() {
        assertThrows(AlunoNaoEncontradoException.class, () -> cadastro.deletarAluno(99));
    }

    // ---------- carregar (dados vindos do arquivo) ----------

    @Test
    @DisplayName("carregar alunos continua os IDs a partir do maior ID lido")
    void carregarContinuaOsIds() throws Exception {
        cadastro.carregarAlunos(List.of(
                new Aluno(3, "Maria", "2026001", "CC", 8.5),
                new Aluno(7, "João", "2026002", "SI", 7.0)));

        Aluno novo = cadastro.cadastrarAluno("Ana", "2026003", "CC", 9.0);

        assertEquals(8, novo.getId());
        assertEquals(3, cadastro.listarAlunos().size());
    }

    @Test
    @DisplayName("carregar com matrícula repetida dá erro e não carrega nenhum aluno")
    void carregarComMatriculaRepetida() {
        List<Aluno> alunos = List.of(
                new Aluno(1, "Maria", "2026001", "CC", 8.5),
                new Aluno(2, "João", "2026001", "SI", 7.0));

        assertThrows(MatriculaDuplicadaException.class, () -> cadastro.carregarAlunos(alunos));
        assertTrue(cadastro.listarAlunos().isEmpty());
    }

    @Test
    @DisplayName("carregar com ID repetido dá erro")
    void carregarComIdRepetido() {
        List<Aluno> alunos = List.of(
                new Aluno(1, "Maria", "2026001", "CC", 8.5),
                new Aluno(1, "João", "2026002", "SI", 7.0));

        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> cadastro.carregarAlunos(alunos));

        assertEquals("O ID 1 aparece mais de uma vez.", erro.getMessage());
    }
}

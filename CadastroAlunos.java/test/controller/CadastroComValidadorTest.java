package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.DadosInvalidosException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import validation.NotaRegra;
import validation.ValidadorAluno;

/**
 * Testes do padrão Strategy no CadastroAlunos: o cadastro usa o validador que recebe,
 * então dá para trocar as regras sem mexer no código do cadastro.
 */
@DisplayName("CadastroAlunos (Strategy)")
class CadastroComValidadorTest {

    @Test
    @DisplayName("o cadastro padrão usa as regras do sistema")
    void cadastroPadraoUsaRegrasDoSistema() {
        CadastroAlunos cadastro = new CadastroAlunos();

        assertThrows(DadosInvalidosException.class, () -> cadastro.cadastrarAluno("Maria", "2026001", "CC", 11));
    }

    @Test
    @DisplayName("validador sem nenhuma regra aceita qualquer dado")
    void validadorSemRegras() throws Exception {
        CadastroAlunos cadastro = new CadastroAlunos(new ValidadorAluno(List.of()));

        assertEquals(50.0, cadastro.cadastrarAluno("Maria", "2026001", "CC", 50).getNota());
    }

    @Test
    @DisplayName("validador só com a regra da nota deixa o nome ficar vazio, mas confere a nota")
    void validadorSoComRegraDaNota() throws Exception {
        CadastroAlunos cadastro = new CadastroAlunos(new ValidadorAluno(List.of(new NotaRegra())));

        cadastro.cadastrarAluno("", "2026001", "CC", 8);

        assertThrows(DadosInvalidosException.class, () -> cadastro.cadastrarAluno("João", "2026002", "CC", 12));
    }

    @Test
    @DisplayName("uma regra nova, escrita como lambda, passa a valer sem mexer no cadastro")
    void regraNovaComoLambda() {
        CadastroAlunos cadastro = new CadastroAlunos(new ValidadorAluno(List.of(aluno -> {
            if (aluno.getNota() < 5) {
                throw new DadosInvalidosException("A nota mínima é 5.");
            }
        })));

        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> cadastro.cadastrarAluno("João", "2026002", "SI", 4));

        assertEquals("A nota mínima é 5.", erro.getMessage());
    }

    @Test
    @DisplayName("a atualização também usa o validador recebido")
    void atualizacaoUsaOValidador() throws Exception {
        CadastroAlunos cadastro = new CadastroAlunos(new ValidadorAluno(List.of(new NotaRegra())));
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8);

        assertThrows(DadosInvalidosException.class, () -> cadastro.atualizarAluno(1, "Maria", "2026001", "CC", 15));
        assertEquals(8.0, cadastro.buscarPorId(1).getNota());
    }
}

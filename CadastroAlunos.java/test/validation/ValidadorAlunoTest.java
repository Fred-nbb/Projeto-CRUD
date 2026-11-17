package validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.DadosInvalidosException;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import model.Aluno;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes do ValidadorAluno (o contexto do padrão Strategy): o validador padrão,
 * a ordem das regras e o comportamento quando uma regra falha.
 */
@DisplayName("ValidadorAluno (Strategy)")
class ValidadorAlunoTest {

    @Test
    @DisplayName("o validador padrão aceita um aluno válido")
    void padraoAceitaAlunoValido() {
        ValidadorAluno validador = ValidadorAluno.padrao();

        assertDoesNotThrow(() -> validador.validar(new Aluno(1, "Maria", "2026001", "CC", 8.5)));
    }

    @Test
    @DisplayName("o validador padrão recusa cada tipo de dado inválido")
    void padraoRecusaDadosInvalidos() {
        ValidadorAluno validador = ValidadorAluno.padrao();

        assertThrows(DadosInvalidosException.class,
                () -> validador.validar(new Aluno(1, "", "2026001", "CC", 8.5)));
        assertThrows(DadosInvalidosException.class,
                () -> validador.validar(new Aluno(1, "Maria", "", "CC", 8.5)));
        assertThrows(DadosInvalidosException.class,
                () -> validador.validar(new Aluno(1, "Maria", "2026001", "C;C", 8.5)));
        assertThrows(DadosInvalidosException.class,
                () -> validador.validar(new Aluno(1, "Maria", "2026001", "CC", 11)));
    }

    @Test
    @DisplayName("as regras são conferidas na ordem: nome, matrícula, curso e nota")
    void regrasSaoConferidasNaOrdem() {
        ValidadorAluno validador = ValidadorAluno.padrao();

        // nome e nota inválidos ao mesmo tempo: aparece primeiro o erro do nome
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> validador.validar(new Aluno(1, "", "2026001", "CC", 99)));

        assertEquals("O campo nome não pode ficar vazio.", erro.getMessage());
    }

    @Test
    @DisplayName("o validador para na primeira regra que falha")
    void paraNaPrimeiraRegraQueFalha() {
        AtomicInteger vezesQueASegundaRodou = new AtomicInteger();
        ValidadorAluno validador = new ValidadorAluno(List.of(
                aluno -> {
                    throw new DadosInvalidosException("falhou a primeira");
                },
                aluno -> vezesQueASegundaRodou.incrementAndGet()));

        assertThrows(DadosInvalidosException.class,
                () -> validador.validar(new Aluno(1, "Maria", "2026001", "CC", 8.5)));
        assertEquals(0, vezesQueASegundaRodou.get());
    }
}

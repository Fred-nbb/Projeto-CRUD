package validation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.DadosInvalidosException;
import model.Aluno;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Testes de cada regra de validação (cada estratégia do padrão Strategy) sozinha.
 */
@DisplayName("Regras de validação (Strategy)")
class RegrasValidacaoTest {

    private final CampoTextoRegra regraDoNome = new CampoTextoRegra("nome", Aluno::getNome, ";");
    private final NotaRegra regraDaNota = new NotaRegra();

    private Aluno alunoComNome(String nome) {
        return new Aluno(1, nome, "2026001", "CC", 8.0);
    }

    private Aluno alunoComNota(double nota) {
        return new Aluno(1, "Maria", "2026001", "CC", nota);
    }

    // ---------- CampoTextoRegra ----------

    @Test
    @DisplayName("campo de texto preenchido é aceito")
    void textoPreenchidoEAceito() {
        assertDoesNotThrow(() -> regraDoNome.validar(alunoComNome("Maria")));
    }

    @Test
    @DisplayName("campo de texto vazio, só com espaços ou nulo é recusado")
    void textoVazioERecusado() {
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> regraDoNome.validar(alunoComNome("   ")));

        assertEquals("O campo nome não pode ficar vazio.", erro.getMessage());
        assertThrows(DadosInvalidosException.class, () -> regraDoNome.validar(alunoComNome("")));
        assertThrows(DadosInvalidosException.class, () -> regraDoNome.validar(alunoComNome(null)));
    }

    @Test
    @DisplayName("campo de texto com o separador do arquivo é recusado")
    void textoComSeparadorERecusado() {
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> regraDoNome.validar(alunoComNome("Maria;Silva")));

        assertEquals("O campo nome não pode ter o caractere ';'.", erro.getMessage());
    }

    @Test
    @DisplayName("a mesma regra serve para outro campo: a mensagem usa o nome do campo")
    void mesmaRegraParaOutroCampo() {
        CampoTextoRegra regraDoCurso = new CampoTextoRegra("curso", Aluno::getCurso, ";");
        Aluno semCurso = new Aluno(1, "Maria", "2026001", "", 8.0);

        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> regraDoCurso.validar(semCurso));

        assertEquals("O campo curso não pode ficar vazio.", erro.getMessage());
    }

    // ---------- NotaRegra ----------

    @Test
    @DisplayName("nota entre 0 e 10 é aceita, incluindo 0 e 10")
    void notaDentroDoIntervaloEAceita() {
        assertDoesNotThrow(() -> regraDaNota.validar(alunoComNota(0)));
        assertDoesNotThrow(() -> regraDaNota.validar(alunoComNota(7.5)));
        assertDoesNotThrow(() -> regraDaNota.validar(alunoComNota(10)));
    }

    @Test
    @DisplayName("nota menor que 0, maior que 10 ou NaN é recusada")
    void notaForaDoIntervaloERecusada() {
        DadosInvalidosException erro = assertThrows(DadosInvalidosException.class,
                () -> regraDaNota.validar(alunoComNota(10.1)));

        assertEquals("A nota deve estar entre 0 e 10.", erro.getMessage());
        assertThrows(DadosInvalidosException.class, () -> regraDaNota.validar(alunoComNota(-0.1)));
        assertThrows(DadosInvalidosException.class, () -> regraDaNota.validar(alunoComNota(Double.NaN)));
    }
}

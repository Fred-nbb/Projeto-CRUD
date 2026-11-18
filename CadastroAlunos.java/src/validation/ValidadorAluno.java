package validation;

import exception.DadosInvalidosException;
import java.util.List;
import model.Aluno;
import persistence.ArquivoAlunos;

/**
 * Valida um aluno aplicando uma lista de regras (RegraValidacao). Essa é a classe que usa
 * as estratégias do padrão Strategy. Quem decide quais regras existem é quem cria o validador.
 */
public class ValidadorAluno {

    private final List<RegraValidacao> regras;

    /**
     * Cria um validador com as regras indicadas, conferidas na ordem da lista.
     *
     * @param regras as regras que serão aplicadas a cada aluno
     */
    public ValidadorAluno(List<RegraValidacao> regras) {
        this.regras = List.copyOf(regras);
    }

    /**
     * Cria o validador usado pelo sistema: nome, matrícula e curso preenchidos e nota entre 0 e 10.
     *
     * @return um validador com as regras do sistema
     */
    public static ValidadorAluno padrao() {
        return new ValidadorAluno(List.of(
                new CampoTextoRegra("nome", Aluno::getNome, ArquivoAlunos.SEPARADOR),
                new CampoTextoRegra("matrícula", Aluno::getMatricula, ArquivoAlunos.SEPARADOR),
                new CampoTextoRegra("curso", Aluno::getCurso, ArquivoAlunos.SEPARADOR),
                new NotaRegra()));
    }

    /**
     * Confere o aluno com todas as regras, na ordem, e para na primeira que falhar.
     *
     * @param aluno o aluno que será conferido
     * @throws DadosInvalidosException se alguma regra não for respeitada
     */
    public void validar(Aluno aluno) throws DadosInvalidosException {
        for (RegraValidacao regra : regras) {
            regra.validar(aluno);
        }
    }
}

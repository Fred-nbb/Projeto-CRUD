package validation;

import exception.DadosInvalidosException;
import model.Aluno;

/**
 * Padrão Strategy: uma regra de validação do aluno.
 *
 * Cada regra é uma classe que implementa esta interface. O ValidadorAluno chama o
 * validar() de todas sem saber qual é qual, então dá pra adicionar ou trocar regras
 * sem mexer no CadastroAlunos.
 */
@FunctionalInterface
public interface RegraValidacao {

    /**
     * Confere se o aluno respeita esta regra.
     *
     * @param aluno o aluno que será conferido
     * @throws DadosInvalidosException se o aluno não respeitar a regra
     */
    void validar(Aluno aluno) throws DadosInvalidosException;
}

package validation;

import exception.DadosInvalidosException;
import model.Aluno;

/**
 * Padrão <b>Strategy</b>: uma regra de validação do aluno.
 *
 * <p>Cada regra (campo de texto obrigatório, nota entre 0 e 10, etc.) é uma classe
 * que implementa esta interface. O {@link ValidadorAluno} só chama {@link #validar(Aluno)}
 * em cada regra, sem saber qual regra está sendo executada. Por isso dá para adicionar,
 * tirar ou trocar regras sem mexer no {@code CadastroAlunos}.</p>
 *
 * <p>Como tem só um método, uma regra nova também pode ser escrita como lambda.</p>
 */
@FunctionalInterface
public interface RegraValidacao {

    /**
     * Confere se o aluno respeita esta regra.
     *
     * @param aluno o aluno que será conferido
     * @throws DadosInvalidosException se o aluno não respeitar a regra (a mensagem
     *                                 explica qual dado está errado)
     */
    void validar(Aluno aluno) throws DadosInvalidosException;
}

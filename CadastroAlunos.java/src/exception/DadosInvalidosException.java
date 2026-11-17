package exception;

/**
 * Lançada quando algum dado do aluno é inválido (campo vazio, nota fora de 0 a 10, etc.).
 *
 * <p>A mensagem diz exatamente qual dado está errado.</p>
 */
public class DadosInvalidosException extends CadastroException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção com a mensagem que explica qual dado está inválido.
     *
     * @param mensagem texto explicando o problema
     */
    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}

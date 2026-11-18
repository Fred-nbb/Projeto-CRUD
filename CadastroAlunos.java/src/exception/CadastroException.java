package exception;

/**
 * Classe base das exceções do cadastro. Assim o Menu trata todas com um catch só.
 */
public class CadastroException extends Exception {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção com a mensagem que será mostrada ao usuário.
     *
     * @param mensagem texto explicando o que deu errado
     */
    public CadastroException(String mensagem) {
        super(mensagem);
    }
}

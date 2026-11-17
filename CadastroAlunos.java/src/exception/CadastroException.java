package exception;

/**
 * Classe base das exceções do cadastro.
 *
 * <p>Todas as exceções do sistema (aluno não encontrado, dados inválidos,
 * matrícula duplicada) herdam desta classe. Assim o {@code Menu} consegue
 * tratar todas com um único {@code catch}.</p>
 */
public class CadastroException extends Exception {

    // número de versão que o Java pede em classes de exceção (evita aviso do compilador)
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

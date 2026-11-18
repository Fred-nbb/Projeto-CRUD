package exception;

/**
 * Lançada quando não existe aluno com o ID informado.
 */
public class AlunoNaoEncontradoException extends CadastroException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção com uma mensagem que mostra o ID procurado.
     *
     * @param id o ID que não foi encontrado
     */
    public AlunoNaoEncontradoException(int id) {
        super("Nenhum aluno encontrado com o ID " + id + ".");
    }
}

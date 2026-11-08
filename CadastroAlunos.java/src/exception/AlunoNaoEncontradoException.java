package exception;

// Lançada quando não existe aluno com o id informado.
public class AlunoNaoEncontradoException extends CadastroException {

    private static final long serialVersionUID = 1L;

    public AlunoNaoEncontradoException(int id) {
        super("Nenhum aluno encontrado com o ID " + id + ".");
    }
}

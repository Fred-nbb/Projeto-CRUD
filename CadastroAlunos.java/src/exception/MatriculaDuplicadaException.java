package exception;

/**
 * Lançada quando tentam usar uma matrícula que já pertence a outro aluno.
 */
public class MatriculaDuplicadaException extends CadastroException {

    private static final long serialVersionUID = 1L;

    /**
     * Cria a exceção com uma mensagem que mostra a matrícula repetida.
     *
     * @param matricula a matrícula que já está em uso
     */
    public MatriculaDuplicadaException(String matricula) {
        super("Já existe um aluno com a matrícula " + matricula + ".");
    }
}

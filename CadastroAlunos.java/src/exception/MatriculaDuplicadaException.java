package exception;

// Lançada quando tentam usar uma matrícula que já pertence a outro aluno.
public class MatriculaDuplicadaException extends CadastroException {

    private static final long serialVersionUID = 1L;

    public MatriculaDuplicadaException(String matricula) {
        super("Já existe um aluno com a matrícula " + matricula + ".");
    }
}

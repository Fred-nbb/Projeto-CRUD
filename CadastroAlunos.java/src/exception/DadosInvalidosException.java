package exception;

// Lançada quando algum dado do aluno é inválido (campo vazio, nota fora de 0 a 10, etc.).
// A mensagem diz exatamente qual dado está errado.
public class DadosInvalidosException extends CadastroException {

    private static final long serialVersionUID = 1L;

    public DadosInvalidosException(String mensagem) {
        super(mensagem);
    }
}

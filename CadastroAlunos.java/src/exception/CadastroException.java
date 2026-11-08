package exception;

// Classe base das exceções do cadastro.
// Assim o Menu consegue tratar todos esses erros com um catch só.
public class CadastroException extends Exception {

    // número de versão que o Java pede em classes de exceção (evita aviso do compilador)
    private static final long serialVersionUID = 1L;

    public CadastroException(String mensagem) {
        super(mensagem);
    }
}

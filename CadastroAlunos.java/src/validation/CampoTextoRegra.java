package validation;

import exception.DadosInvalidosException;
import java.util.function.Function;
import model.Aluno;

/**
 * Regra para um campo de texto do aluno (nome, matrícula ou curso).
 *
 * <p>O campo não pode ser nulo, vazio ou só espaços, e não pode conter o caractere
 * separador do arquivo CSV. A mesma classe serve para os três campos: o que muda
 * é o nome do campo (usado na mensagem de erro) e qual getter do aluno ela lê.</p>
 */
public class CampoTextoRegra implements RegraValidacao {

    private final String nomeDoCampo;
    private final Function<Aluno, String> leitor;
    private final String separadorProibido;

    /**
     * Cria a regra para um campo de texto.
     *
     * @param nomeDoCampo       nome do campo, usado na mensagem de erro (por exemplo, "nome")
     * @param leitor            função que pega o valor do campo no aluno (por exemplo, {@code Aluno::getNome})
     * @param separadorProibido texto que não pode aparecer no campo (o separador do arquivo)
     */
    public CampoTextoRegra(String nomeDoCampo, Function<Aluno, String> leitor, String separadorProibido) {
        this.nomeDoCampo = nomeDoCampo;
        this.leitor = leitor;
        this.separadorProibido = separadorProibido;
    }

    /**
     * Confere se o campo do aluno está preenchido e sem o separador.
     *
     * @param aluno o aluno que será conferido
     * @throws DadosInvalidosException se o campo estiver vazio ou tiver o separador
     */
    @Override
    public void validar(Aluno aluno) throws DadosInvalidosException {
        String valor = leitor.apply(aluno);

        if (valor == null || valor.isBlank()) {
            throw new DadosInvalidosException("O campo " + nomeDoCampo + " não pode ficar vazio.");
        }
        // o separador divide as colunas no arquivo, então não pode aparecer dentro dos dados
        if (valor.contains(separadorProibido)) {
            throw new DadosInvalidosException(
                    "O campo " + nomeDoCampo + " não pode ter o caractere '" + separadorProibido + "'.");
        }
    }
}

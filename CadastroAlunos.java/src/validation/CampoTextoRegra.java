package validation;

import exception.DadosInvalidosException;
import java.util.function.Function;
import model.Aluno;

/**
 * Regra para um campo de texto (nome, matrícula ou curso): não pode ser vazio
 * nem conter o separador do arquivo CSV. A mesma classe serve para os três campos.
 */
public class CampoTextoRegra implements RegraValidacao {

    private final String nomeDoCampo;
    private final Function<Aluno, String> leitor;
    private final String separadorProibido;

    /**
     * Cria a regra para um campo de texto.
     *
     * @param nomeDoCampo       nome do campo, usado na mensagem de erro
     * @param leitor            função que pega o valor do campo no aluno (ex.: Aluno::getNome)
     * @param separadorProibido texto que não pode aparecer no campo
     */
    public CampoTextoRegra(String nomeDoCampo, Function<Aluno, String> leitor, String separadorProibido) {
        this.nomeDoCampo = nomeDoCampo;
        this.leitor = leitor;
        this.separadorProibido = separadorProibido;
    }

    /**
     * Confere se o campo está preenchido e sem o separador.
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

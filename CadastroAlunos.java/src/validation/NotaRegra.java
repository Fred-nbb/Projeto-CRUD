package validation;

import exception.DadosInvalidosException;
import model.Aluno;

/**
 * Regra da nota: tem que estar entre 0 e 10 (os dois valores são aceitos).
 */
public class NotaRegra implements RegraValidacao {

    /** Menor nota aceita. */
    public static final double NOTA_MINIMA = 0.0;

    /** Maior nota aceita. */
    public static final double NOTA_MAXIMA = 10.0;

    /**
     * Cria a regra da nota.
     */
    public NotaRegra() {
        // nada para configurar: os limites são constantes
    }

    /**
     * Confere se a nota está entre NOTA_MINIMA e NOTA_MAXIMA.
     *
     * @param aluno o aluno que será conferido
     * @throws DadosInvalidosException se a nota for menor que 0, maior que 10 ou não for um número
     */
    @Override
    public void validar(Aluno aluno) throws DadosInvalidosException {
        double nota = aluno.getNota();

        if (Double.isNaN(nota) || nota < NOTA_MINIMA || nota > NOTA_MAXIMA) {
            throw new DadosInvalidosException("A nota deve estar entre 0 e 10.");
        }
    }
}

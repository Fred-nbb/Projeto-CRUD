package persistence;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import model.Aluno;
import observer.CadastroObserver;

/**
 * Observer que salva os alunos no arquivo sempre que o cadastro muda.
 *
 * <p>Antes o {@code Menu} chamava o salvamento depois de cada operação. Com este observer,
 * o {@code CadastroAlunos} só avisa que mudou e o arquivo é atualizado sozinho, sem o
 * cadastro nem o menu precisarem conhecer o {@link ArquivoAlunos}.</p>
 */
public class SalvarAoAlterarObserver implements CadastroObserver {

    private final ArquivoAlunos arquivo;
    private final Consumer<String> avisador;

    /**
     * Cria o observer.
     *
     * @param arquivo  o arquivo onde os alunos serão salvos
     * @param avisador o que fazer com a mensagem quando não for possível salvar
     *                 (por exemplo, {@code System.out::println})
     */
    public SalvarAoAlterarObserver(ArquivoAlunos arquivo, Consumer<String> avisador) {
        this.arquivo = arquivo;
        this.avisador = avisador;
    }

    /**
     * Salva a lista atual de alunos no arquivo. Se der erro, não lança exceção:
     * a alteração já foi feita no cadastro, então só avisa o usuário.
     *
     * @param alunos a lista atual de alunos
     */
    @Override
    public void cadastroAlterado(List<Aluno> alunos) {
        try {
            arquivo.salvar(alunos);
        } catch (IOException e) {
            avisador.accept("Atenção: a alteração foi feita, mas não foi possível salvar no arquivo: "
                    + e.getMessage());
        }
    }
}

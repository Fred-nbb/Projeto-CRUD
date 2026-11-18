package persistence;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;
import model.Aluno;
import observer.CadastroObserver;

/**
 * Observer que salva os alunos no arquivo sempre que o cadastro muda.
 * Assim o cadastro e o menu não precisam conhecer a classe ArquivoAlunos.
 */
public class SalvarAoAlterarObserver implements CadastroObserver {

    private final ArquivoAlunos arquivo;
    private final Consumer<String> avisador;

    /**
     * Cria o observer.
     *
     * @param arquivo  o arquivo onde os alunos serão salvos
     * @param avisador o que fazer com a mensagem quando não for possível salvar
     */
    public SalvarAoAlterarObserver(ArquivoAlunos arquivo, Consumer<String> avisador) {
        this.arquivo = arquivo;
        this.avisador = avisador;
    }

    /**
     * Salva a lista atual no arquivo. Se der erro, só avisa o usuário,
     * porque a alteração já foi feita no cadastro.
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

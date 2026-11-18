package observer;

import java.util.List;
import model.Aluno;

/**
 * Padrão Observer: quem quer saber quando o cadastro de alunos mudou.
 *
 * O CadastroAlunos avisa todos os observers registrados depois de cada cadastro,
 * atualização ou remoção, sem saber o que cada um faz com o aviso.
 */
@FunctionalInterface
public interface CadastroObserver {

    /**
     * Chamado depois que o cadastro mudou.
     *
     * @param alunos a lista atual de alunos (não pode ser modificada, pois é compartilhada)
     */
    void cadastroAlterado(List<Aluno> alunos);
}

package observer;

import java.util.List;
import model.Aluno;

/**
 * Padrão <b>Observer</b>: quem quer saber quando o cadastro de alunos mudou.
 *
 * <p>O {@code CadastroAlunos} (o "sujeito" observado) guarda uma lista de observers e
 * avisa todos eles, chamando {@link #cadastroAlterado(List)}, depois de cada cadastro,
 * atualização ou remoção. Ele não sabe o que cada observer faz com o aviso: um pode
 * salvar em arquivo, outro poderia guardar um histórico, e assim por diante.</p>
 *
 * <p>Como tem só um método, um observer simples também pode ser escrito como lambda.</p>
 */
@FunctionalInterface
public interface CadastroObserver {

    /**
     * Chamado depois que o cadastro mudou (aluno cadastrado, atualizado ou removido).
     *
     * @param alunos a lista atual de alunos, na ordem de cadastro. A lista não pode ser
     *               modificada, porque é compartilhada com os outros observers.
     */
    void cadastroAlterado(List<Aluno> alunos);
}

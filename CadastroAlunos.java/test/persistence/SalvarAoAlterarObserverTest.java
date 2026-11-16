package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import controller.CadastroAlunos;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Testes do observer que salva no arquivo, funcionando junto com o CadastroAlunos.
 */
@DisplayName("SalvarAoAlterarObserver")
class SalvarAoAlterarObserverTest {

    @TempDir
    Path pastaTemporaria;

    @Test
    @DisplayName("cadastrar, atualizar e deletar atualizam o arquivo sozinhos")
    void arquivoAcompanhaOCadastro() throws Exception {
        ArquivoAlunos arquivo = new ArquivoAlunos(pastaTemporaria.resolve("alunos.csv"));
        CadastroAlunos cadastro = new CadastroAlunos();
        cadastro.adicionarObserver(new SalvarAoAlterarObserver(arquivo, mensagem -> { }));

        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);
        cadastro.cadastrarAluno("João", "2026002", "SI", 7.0);
        assertEquals(2, arquivo.carregar().size());

        cadastro.atualizarAluno(1, "Maria Souza", "2026001", "CC", 9.5);
        assertEquals("Maria Souza", arquivo.carregar().get(0).getNome());
        assertEquals(9.5, arquivo.carregar().get(0).getNota());

        cadastro.deletarAluno(2);
        assertEquals(1, arquivo.carregar().size());
    }

    @Test
    @DisplayName("se não conseguir salvar, avisa pelo avisador e a alteração continua no cadastro")
    void avisaQuandoNaoConsegueSalvar() throws Exception {
        // a "pasta" do arquivo é na verdade um arquivo comum, então salvar vai dar erro
        Path arquivoComum = Files.writeString(pastaTemporaria.resolve("comum.txt"), "texto");
        ArquivoAlunos arquivo = new ArquivoAlunos(arquivoComum.resolve("alunos.csv"));
        List<String> mensagens = new ArrayList<>();
        CadastroAlunos cadastro = new CadastroAlunos();
        cadastro.adicionarObserver(new SalvarAoAlterarObserver(arquivo, mensagens::add));

        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);

        assertEquals(1, mensagens.size());
        assertTrue(mensagens.get(0).startsWith("Atenção: a alteração foi feita"));
        assertEquals(1, cadastro.listarAlunos().size());
    }
}

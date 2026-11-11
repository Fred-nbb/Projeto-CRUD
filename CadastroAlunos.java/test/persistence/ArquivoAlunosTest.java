package persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import model.Aluno;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Testes da classe ArquivoAlunos (leitura e escrita do arquivo CSV).
 * O JUnit cria uma pasta temporária (@TempDir) para cada teste,
 * então os testes não mexem no alunos.csv de verdade.
 */
@DisplayName("ArquivoAlunos")
class ArquivoAlunosTest {

    @TempDir
    Path pastaTemporaria;

    private Path caminho;
    private ArquivoAlunos arquivo;

    @BeforeEach
    void criarArquivo() {
        caminho = pastaTemporaria.resolve("alunos.csv");
        arquivo = new ArquivoAlunos(caminho);
    }

    @Test
    @DisplayName("salvar e depois carregar devolve os mesmos alunos")
    void salvarECarregar() throws IOException {
        arquivo.salvar(List.of(
                new Aluno(1, "Maria Silva", "2026001", "Ciência da Computação", 8.5),
                new Aluno(2, "João", "2026002", "Engenharia", 10.0)));

        List<Aluno> lidos = arquivo.carregar();

        assertEquals(2, lidos.size());
        Aluno maria = lidos.get(0);
        assertEquals(1, maria.getId());
        assertEquals("Maria Silva", maria.getNome());
        assertEquals("2026001", maria.getMatricula());
        assertEquals("Ciência da Computação", maria.getCurso());
        assertEquals(8.5, maria.getNota());
        assertEquals("João", lidos.get(1).getNome());
    }

    @Test
    @DisplayName("salvar cria o arquivo com o cabeçalho e uma linha por aluno")
    void salvarCriaArquivoComCabecalho() throws IOException {
        arquivo.salvar(List.of(new Aluno(1, "Maria", "2026001", "CC", 8.5)));

        List<String> linhas = Files.readAllLines(caminho, StandardCharsets.UTF_8);

        assertEquals(List.of("id;nome;matricula;curso;nota", "1;Maria;2026001;CC;8.5"), linhas);
    }

    @Test
    @DisplayName("arquivo que ainda não existe é lido como lista vazia")
    void arquivoInexistente() throws IOException {
        assertTrue(arquivo.carregar().isEmpty());
    }

    @Test
    @DisplayName("linhas em branco são ignoradas")
    void ignoraLinhasEmBranco() throws IOException {
        Files.writeString(caminho, "id;nome;matricula;curso;nota\n\n1;Maria;2026001;CC;8.5\n\n");

        assertEquals(1, arquivo.carregar().size());
    }

    @Test
    @DisplayName("nota que não é número gera IOException dizendo a linha do problema")
    void notaInvalidaNoArquivo() throws IOException {
        Files.writeString(caminho, "id;nome;matricula;curso;nota\n1;Maria;2026001;CC;abc\n");

        IOException erro = assertThrows(IOException.class, () -> arquivo.carregar());

        assertTrue(erro.getMessage().startsWith("Linha 2 do arquivo alunos.csv"));
    }

    @Test
    @DisplayName("linha com colunas faltando gera IOException")
    void linhaComColunasFaltando() throws IOException {
        Files.writeString(caminho, "1;Maria;2026001\n");

        IOException erro = assertThrows(IOException.class, () -> arquivo.carregar());

        assertTrue(erro.getMessage().contains("deveria ter 5 colunas, mas tem 3"));
    }

    @Test
    @DisplayName("fazerBackup cria uma cópia do arquivo com final .bak")
    void fazerBackupCriaCopia() throws IOException {
        Files.writeString(caminho, "conteúdo qualquer");

        Path backup = arquivo.fazerBackup();

        assertEquals("alunos.csv.bak", backup.getFileName().toString());
        assertEquals("conteúdo qualquer", Files.readString(backup));
    }
}

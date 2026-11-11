package controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import exception.AlunoNaoEncontradoException;
import java.nio.file.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import persistence.ArquivoAlunos;

/**
 * Testes de integração: CadastroAlunos funcionando junto com ArquivoAlunos.
 * Simulam o uso real do programa: fazer alterações, salvar no arquivo,
 * fechar e abrir de novo (criar um cadastro novo que lê o mesmo arquivo).
 */
@DisplayName("Cadastro + arquivo (integração)")
class CadastroComArquivoTest {

    @TempDir
    Path pastaTemporaria;

    private ArquivoAlunos arquivo;

    @BeforeEach
    void criarArquivo() {
        arquivo = new ArquivoAlunos(pastaTemporaria.resolve("alunos.csv"));
    }

    // simula abrir o programa de novo: um cadastro vazio que carrega o arquivo
    private CadastroAlunos abrirDeNovo() throws Exception {
        CadastroAlunos cadastro = new CadastroAlunos();
        cadastro.carregarAlunos(arquivo.carregar());
        return cadastro;
    }

    @Test
    @DisplayName("alunos cadastrados continuam lá depois de fechar e abrir o programa")
    void alunosContinuamDepoisDeReabrir() throws Exception {
        CadastroAlunos cadastro = new CadastroAlunos();
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);
        cadastro.cadastrarAluno("João", "2026002", "SI", 7.0);
        arquivo.salvar(cadastro.listarAlunos());

        CadastroAlunos reaberto = abrirDeNovo();

        assertEquals(2, reaberto.listarAlunos().size());
        assertEquals("Maria", reaberto.buscarPorId(1).getNome());
        assertEquals("João", reaberto.buscarPorId(2).getNome());
    }

    @Test
    @DisplayName("atualização e remoção continuam valendo depois de fechar e abrir o programa")
    void atualizacaoERemocaoContinuamDepoisDeReabrir() throws Exception {
        CadastroAlunos primeiraVez = new CadastroAlunos();
        primeiraVez.cadastrarAluno("Maria", "2026001", "CC", 8.5);
        primeiraVez.cadastrarAluno("João", "2026002", "SI", 7.0);
        arquivo.salvar(primeiraVez.listarAlunos());

        CadastroAlunos segundaVez = abrirDeNovo();
        segundaVez.atualizarAluno(1, "Maria Souza", "2026001", "CC", 9.5);
        segundaVez.deletarAluno(2);
        arquivo.salvar(segundaVez.listarAlunos());

        CadastroAlunos terceiraVez = abrirDeNovo();

        assertEquals(1, terceiraVez.listarAlunos().size());
        assertEquals("Maria Souza", terceiraVez.buscarPorId(1).getNome());
        assertEquals(9.5, terceiraVez.buscarPorId(1).getNota());
        assertThrows(AlunoNaoEncontradoException.class, () -> terceiraVez.buscarPorId(2));
    }

    @Test
    @DisplayName("depois de abrir de novo, o próximo ID continua de onde parou")
    void proximoIdContinuaDepoisDeReabrir() throws Exception {
        CadastroAlunos cadastro = new CadastroAlunos();
        cadastro.cadastrarAluno("Maria", "2026001", "CC", 8.5);
        cadastro.cadastrarAluno("João", "2026002", "SI", 7.0);
        arquivo.salvar(cadastro.listarAlunos());

        CadastroAlunos reaberto = abrirDeNovo();

        assertEquals(3, reaberto.cadastrarAluno("Ana", "2026003", "CC", 9.0).getId());
    }
}

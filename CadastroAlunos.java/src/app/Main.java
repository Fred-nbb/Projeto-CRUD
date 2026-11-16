package app;

import controller.CadastroAlunos;
import java.nio.file.Path;
import persistence.ArquivoAlunos;
import persistence.SalvarAoAlterarObserver;
import view.Menu;

/**
 * Classe que inicia o programa. É aqui que as peças são montadas e ligadas:
 * o cadastro, o arquivo, o observer que salva e o menu.
 */
public class Main {

    private Main() {
        // classe só com o main: não precisa ser instanciada
    }

    /**
     * Ponto de entrada do programa.
     *
     * @param args argumentos da linha de comando (não são usados)
     */
    public static void main(String[] args) {
        CadastroAlunos cadastro = new CadastroAlunos();
        ArquivoAlunos arquivo = new ArquivoAlunos(Path.of("alunos.csv"));

        // Observer: a partir daqui, toda alteração no cadastro é salva no arquivo automaticamente
        cadastro.adicionarObserver(new SalvarAoAlterarObserver(arquivo, System.out::println));

        Menu menu = new Menu(cadastro, arquivo);
        menu.iniciar();
    }
}

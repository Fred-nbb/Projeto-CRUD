package app;

import controller.CadastroAlunos;
import java.nio.file.Path;
import persistence.ArquivoAlunos;
import persistence.SalvarAoAlterarObserver;
import view.Menu;

/**
 * Inicia o programa e liga as peças: cadastro, arquivo, observer que salva e menu.
 */
public class Main {

    private Main() {
        // só tem o main, não precisa ser instanciada
    }

    /**
     * Ponto de entrada do programa.
     *
     * @param args argumentos da linha de comando (não são usados)
     */
    public static void main(String[] args) {
        CadastroAlunos cadastro = new CadastroAlunos();
        ArquivoAlunos arquivo = new ArquivoAlunos(Path.of("alunos.csv"));

        // Observer: toda alteração no cadastro é salva no arquivo automaticamente
        cadastro.adicionarObserver(new SalvarAoAlterarObserver(arquivo, System.out::println));

        new Menu(cadastro, arquivo).iniciar();
    }
}

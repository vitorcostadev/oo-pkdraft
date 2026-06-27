import controller.CampanhaController;
import controller.DraftController;
import domain.models.battle.Treinador;
import domain.facade.JsonLocalFacade;
import domain.facade.PokemonDadosFacade;
import view.CLIView;


public class Main{
    public static void main(String... args) {
        CLIView view = new CLIView();
        PokemonDadosFacade facade = new JsonLocalFacade();
        DraftController draft = new DraftController();
        Treinador jogador = draft.iniciarDraft(facade, view);
        new CampanhaController().iniciarCampanha(jogador, facade, view);
    }
}
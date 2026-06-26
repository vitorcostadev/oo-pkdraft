import controller.CampanhaController;
import controller.DraftController;
import integration.JsonLocalFacade;
import integration.PokemonDadosFacade;
import domain.models.Treinador;
import view.CLIView;


void main() {
    CLIView view = new CLIView();
    PokemonDadosFacade facade = new JsonLocalFacade();

    DraftController moduloDraft = new DraftController();
    Treinador jogador = moduloDraft.iniciarDraft(facade, view);

    CampanhaController moduloCampanha = new CampanhaController();
    moduloCampanha.iniciarCampanha(jogador, facade, view);
}
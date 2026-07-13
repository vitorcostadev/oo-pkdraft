import controller.DraftController;
import controller.LeagueController;
import domain.facade.DataPokemonFacade;
import domain.facade.JsonDataFacade;
import domain.models.battle.Player;
import view.CLIView;
import view.InterfaceJogo;


void main() {
    InterfaceJogo view = new CLIView();
    DataPokemonFacade facade = new JsonDataFacade();

    DraftController draft = new DraftController();
    Player jogador = draft.iniciarDraft(facade, view);
    new LeagueController().iniciarCampanha(jogador, facade, view);
}
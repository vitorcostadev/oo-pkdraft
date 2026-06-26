#pragma once
#include <vector>
#include <string>
#include <thread>
#include <chrono>
#include <iostream>
#include <fstream>
#include <filesystem>
#include <iomanip>
#include <sstream>

class CLIView;

class LogService {
    private:
        std::vector<std::string> events;
        CLIView* view;
    public:
        LogService(CLIView* view) : view(view) {};
        void registerEvent(std::string event){
            events.push_back(event);
            if(view != NULL){
                std::this_thread::sleep_for(std::chrono::milliseconds(1200));
                //view->displayMessage(event);
            }
        }
        void saveLogToAFile() {
            namespace fs = std::filesystem;

            fs::path diretorio("logs");

            if (!fs::exists(diretorio)) {
                fs::create_directories(diretorio);
            }

            auto agora = std::chrono::system_clock::now();
            std::time_t tempo = std::chrono::system_clock::to_time_t(agora);

            std::tm dataHora;
        #ifdef _WIN32
            localtime_s(&dataHora, &tempo);
        #else
            localtime_r(&tempo, &dataHora);
        #endif

            std::ostringstream nomeArquivo;
            nomeArquivo << "campanha_"
                        << std::put_time(&dataHora, "%Y%m%d_%H%M%S")
                        << ".pklog";

            fs::path arquivo = diretorio / nomeArquivo.str();

            try {
                std::ofstream escritor(arquivo);

                if (!escritor.is_open()) {
                    throw std::runtime_error("Não foi possível abrir o arquivo.");
                }

                for (const std::string& evento : events) {
                    escritor << evento << '\n';
                }

            } catch (const std::exception& e) {
                std::cerr << "Falha critica ao guardar logs: "
                        << e.what() << std::endl;
            }
        }
        
};
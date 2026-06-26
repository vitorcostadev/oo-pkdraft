#pragma once
#include <iostream>
#include <string>
#include <thread>
#include <chrono>
#include "../domain/models/Battle.h"

class CLIView {
    public:

        static void displayMessage(const std::string& message) {
            std::cout << message << std::endl;
        }

        static void waitNextOpponent(){
            std::cout << "Battle finished. Waiting for the next opponent..." << std::endl;
            std::this_thread::sleep_for(std::chrono::seconds(7));
        }

        static void statusOfBattle(Battle* battle){
            Pokemon* p1 = battle->getPlayer()->getActivePokemon();
            Pokemon* p2 = battle->getOpponent()->getActivePokemon();

            std::cout << "== Status of Battle ==" << std::endl;
            if(p1 != NULL){
                std::cout << battle->getPlayer()->getName() << " - " << p1->getName() << " (HP: " << std::to_string(p1->getActualHP()) << "/" << std::to_string(p1->getStats().getHp()) << ")" << std::endl;
            }
            if(p2 != NULL){
                std::cout << battle->getOpponent()->getName() << " - " << p2->getName() << " (HP: " << std::to_string(p2->getActualHP()) << "/" << std::to_string(p2->getStats().getHp()) << ")" << std::endl;
            }

            std::cout << "=======================" << std::endl;
        }

        static int readInputInteger(int min, int max){
            int esc = -1;
            while(true){
                std::cout << "Choose a option (" << min << " to " << max << "): ";
                std::cin >> esc;
                try{
                    int num = stoi(std::to_string(esc));;
                }
                catch(const std::exception& e){
                    std::cout << "Invalid input. Please enter a valid integer." << std::endl;
                    std::cin.clear();
                    std::cin.ignore(1000, '\n');
                    continue;
                }   
                if(esc >= min && esc <= max){
                    break;
                }
                std::cout << "Invalid option. Please try again." << std::endl;
            }

            return esc;
        }
};
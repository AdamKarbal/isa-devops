package fr.univcotedazur.skimaster.inbox.components;

import fr.univcotedazur.skimaster.inbox.interfaces.MessageInput;
import org.springframework.stereotype.Component;

@Component
public class Inbox implements MessageInput{
    @Override
    public void sendMessage(String msg){
        //Temporaire en attendant de voir comment les pannels sont implémentés.
        System.out.println("[AFFICHAGE PANNEAU] : " + msg + ".");
    }

    @Override
    public void clearPannel(){
        System.out.println("[AFFICHAGE PANNEAU]: FAITES ATTENTION AUX SKIEURS EN AMONT.");
    }
}

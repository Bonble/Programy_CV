package com.example.projekt.scheduled;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusStrony {
    private static StatusStrony statusStrony; //Obiekt posiada zmienną statyczną, która jest typu tego obiektu
    private StatusStrony(){ }

    private boolean maintenance=false; // zmienna włączenia trybu roboczego

    private boolean maintenanceAdmin=false;// zmienna włączenia administracyjnego trybu roboczego

    public static StatusStrony getInstance() {
        if (statusStrony == null)
            statusStrony =new StatusStrony();
        return statusStrony;
    } //funkcja getInstance singletonu, do której odwołujemy się gdy chcemy użyć instancji obiektu

    public boolean getMaintenece(){
        return maintenance;
    }
    public boolean getMainteneceAdmin(){
        return maintenanceAdmin;
    }
}

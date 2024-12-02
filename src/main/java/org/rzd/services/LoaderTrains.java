package org.rzd.services;

import org.rzd.model.TicketOptions;
import org.rzd.model.Train;

import java.util.List;

public interface LoaderTrains {
    List<String> getStationList(String mask);

    List<Train> getTrainList(TicketOptions ticketOptions);
}

package com.s4mz.eventbooking.services;

import com.s4mz.eventbooking.domain.entities.QrCode;
import com.s4mz.eventbooking.domain.entities.Ticket;

public interface QrCodeService {

    QrCode generateQrCode(Ticket ticket);

}

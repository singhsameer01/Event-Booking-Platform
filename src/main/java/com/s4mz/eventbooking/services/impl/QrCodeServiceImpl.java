package com.s4mz.eventbooking.services.impl;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.s4mz.eventbooking.domain.entities.QrCode;
import com.s4mz.eventbooking.domain.entities.QrCodeStatusEnum;
import com.s4mz.eventbooking.domain.entities.Ticket;
import com.s4mz.eventbooking.exceptions.QrCodeGenerationException;
import com.s4mz.eventbooking.repositories.QrCodeRepository;
import com.s4mz.eventbooking.services.QrCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class QrCodeServiceImpl implements QrCodeService {

    private static final int QR_HEIGHT=300;
    private static final int QR_WIDTH=300;
    private final QRCodeWriter qrCodeWriter;
    private final QrCodeRepository qrCodeRepository;

    @Override
    public QrCode generateQrCode(Ticket ticket) {
        try{
            UUID qrCodeId = UUID.randomUUID();
            String qrCodeImage= generateQrCodeImage(qrCodeId);
            QrCode qrCode=new QrCode();
            qrCode.setId(qrCodeId);
            qrCode.setStatus(QrCodeStatusEnum.ACTIVE);
            qrCode.setValue(qrCodeImage);
            qrCode.setTicket(ticket);
            return qrCodeRepository.saveAndFlush(qrCode);
        }
        catch (WriterException | IOException e){
            throw  new QrCodeGenerationException("Failed to generate QR code", e);
        }
    }

    @Override
    public byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId) {
        QrCode qrCode= qrCodeRepository.findByTicketIdAndTicketPurchaserId(ticketId,userId)
                .orElseThrow(()->new QrCodeGenerationException("QR code not found for user: "+userId+" and ticket: "+ticketId));
        try{
            return Base64.getDecoder().decode(qrCode.getValue());
        }
        catch (IllegalArgumentException e){
            log.error("Failed to decode QR code image for user: {} and ticket: {}", userId, ticketId);
            throw new QrCodeGenerationException("Failed to decode QR code image", e);
        }
    }

    private String generateQrCodeImage(UUID qrCodeId) throws WriterException, IOException {
        BitMatrix bitMatrix=qrCodeWriter.encode(
                qrCodeId.toString(),
                BarcodeFormat.QR_CODE,
                QR_WIDTH,
                QR_HEIGHT
        );
        BufferedImage image=MatrixToImageWriter.toBufferedImage(bitMatrix);
        try(ByteArrayOutputStream outputStream=new ByteArrayOutputStream()){
            ImageIO.write(image,"PNG",outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            return Base64.getEncoder().encodeToString(imageBytes);
        }
    }
}

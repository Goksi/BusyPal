package tech.goksi.busypal.utils;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import it.auties.qr.QrTerminal;
import it.auties.whatsapp.api.QrHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class QrCodeConsoleLogHandler implements QrHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(QrCodeConsoleLogHandler.class);

    public static QrCodeConsoleLogHandler getInstance() {
        return InstanceHolder.INSTANCE;
    }

    @Override
    public void accept(String qr) {
        LOGGER.info("Please scan this qr code with whatsapp");
        var qrRepresentation = qrToStringRepresentation(qr);
        LOGGER.info("\n{}", qrRepresentation);
    }

    private String qrToStringRepresentation(String qr) {
        var matrix = createMatrix(qr, 10, 0);
        return QrTerminal.toString(matrix, true);
    }

    private BitMatrix createMatrix(String qr, int size, int margin) {
        try {
            var writer = new MultiFormatWriter();
            return writer.encode(qr, BarcodeFormat.QR_CODE, size, size, Map.of(EncodeHintType.MARGIN, margin, EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L));
        } catch (WriterException exception) {
            throw new UnsupportedOperationException("Cannot create qr countryCode", exception);
        }
    }

    private static final class InstanceHolder {

        private static final QrCodeConsoleLogHandler INSTANCE = new QrCodeConsoleLogHandler();
    }
}

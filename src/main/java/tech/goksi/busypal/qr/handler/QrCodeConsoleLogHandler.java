package tech.goksi.busypal.qr.handler;

import it.auties.qr.QrTerminal;
import it.auties.whatsapp.api.QrHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    var matrix = QrHandler.createMatrix(qr, 10, 0);
    return QrTerminal.toString(matrix, true);
  }

  private static final class InstanceHolder {

    private static final QrCodeConsoleLogHandler INSTANCE = new QrCodeConsoleLogHandler();
  }
}

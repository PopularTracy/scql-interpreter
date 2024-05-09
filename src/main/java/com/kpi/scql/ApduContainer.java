package com.kpi.scql;

import com.sun.javacard.apduio.Apdu;

public class ApduContainer {

    private Apdu apdu;
    private String scqlCommand;

    public ApduContainer(Apdu apdu, String scqlCommand) {
        this.apdu = apdu;
        this.scqlCommand = scqlCommand;
    }

    public Apdu getApdu() {
        return apdu;
    }

    public void setApdu(Apdu apdu) {
        this.apdu = apdu;
    }

    public String getScqlCommand() {
        return scqlCommand;
    }

    public void setScqlCommand(String scqlCommand) {
        this.scqlCommand = scqlCommand;
    }
}

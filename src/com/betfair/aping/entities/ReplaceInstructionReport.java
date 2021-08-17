package com.betfair.aping.entities;

import com.betfair.aping.enums.InstructionReportErrorCode;
import com.betfair.aping.enums.InstructionReportStatus;

public class ReplaceInstructionReport { 
    public void setStatus(InstructionReportStatus status) {
        this.status = status;
    }
    
    public InstructionReportStatus getStatus() {
        return status;
    }

    public void setErrorCode(InstructionReportErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    
    public InstructionReportErrorCode getErrorCode() {
        return errorCode;
    }

    public void setCancelInstructionReport(CancelInstructionReport cancelInstructionReport) {
        this.cancelInstructionReport = cancelInstructionReport;
    }
    
    public CancelInstructionReport getCancelInstructionReport() {
        return cancelInstructionReport;
    }

    public void setPlaceInstructionReport(PlaceInstructionReport placeInstructionReport) {
        this.placeInstructionReport = placeInstructionReport;
    }
    
    public PlaceInstructionReport getPlaceInstructionReport() {
        return placeInstructionReport;
    }

    private InstructionReportStatus status;
    private InstructionReportErrorCode errorCode;
    private CancelInstructionReport cancelInstructionReport;
    private PlaceInstructionReport placeInstructionReport;
}

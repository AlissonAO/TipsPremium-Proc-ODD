package com.betfair.aping.entities;

import java.util.List;

import com.betfair.aping.containers.Container;
import com.betfair.aping.enums.ExecutionReportErrorCode;
import com.betfair.aping.enums.ExecutionReportStatus;

public class CancelExecutionReport extends Container {
    public void setCustomerRef(String customerRef) {
        this.customerRef = customerRef;
    }
    
    public String getCustomerRef() {
        return customerRef;
    }

    public void setStatus(ExecutionReportStatus status) {
        this.status = status;
    }
    
    public ExecutionReportStatus getStatus() {
        return status;
    }

    public void setErrorCode(ExecutionReportErrorCode errorCode) {
        this.errorCode = errorCode;
    }
    
    public ExecutionReportErrorCode getErrorCode() {
        return errorCode;
    }

    public void setMarketId(String marketId) {
        this.marketId = marketId;
    }
    
    public String getMarketId() {
        return marketId;
    }

    public void setInstructionReports(List<CancelInstructionReport> instructionReports) {
        this.instructionReports = instructionReports;
    }
    
    public List<CancelInstructionReport> getInstructionReports() {
        return instructionReports;
    }

    private String customerRef;
    private ExecutionReportStatus status;
    private ExecutionReportErrorCode errorCode;
    private String marketId;
    private List<CancelInstructionReport> instructionReports;
}

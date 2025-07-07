package com.library.fine.dto;

public class FineResponseDTO {

    private FineDTO fineDTO;
    private BorrowingTransactionResponseDTO borrowingTransactionResponseDTO;

    public FineResponseDTO() {
    }

    public FineResponseDTO(FineDTO fineDTO, BorrowingTransactionResponseDTO borrowingTransactionResponseDTO) {
        this.fineDTO = fineDTO;
        this.borrowingTransactionResponseDTO = borrowingTransactionResponseDTO;
    }

    public FineDTO getFineDTO() {
        return fineDTO;
    }

    public void setFineDTO(FineDTO fineDTO) {
        this.fineDTO = fineDTO;
    }

    public BorrowingTransactionResponseDTO getBorrowingTransactionResponseDTO() {
        return borrowingTransactionResponseDTO;
    }

    public void setBorrowingTransactionResponseDTO(BorrowingTransactionResponseDTO borrowingTransactionResponseDTO) {
        this.borrowingTransactionResponseDTO = borrowingTransactionResponseDTO;
    }
}

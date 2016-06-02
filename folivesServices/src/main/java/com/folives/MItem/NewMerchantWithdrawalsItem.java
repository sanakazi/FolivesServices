package com.folives.MItem;

/**
 * Created by ravikant on 17-03-2016.
 */
public class NewMerchantWithdrawalsItem {

    String WithdrawalId, Amount, PaymentType, PaymentMethod, PaymentStatus, BankAccNum, DateToProcess, DateTime;

    public NewMerchantWithdrawalsItem() {

    }

    public NewMerchantWithdrawalsItem(String WithdrawalId, String Amount, String PaymentType, String PaymentMethod, String PaymentStatus,
                                      String BankAccNum, String DateToProcess, String DateTime) {
        this.WithdrawalId = WithdrawalId;
        this.Amount = Amount;
        this.PaymentType = PaymentType;
        this.PaymentMethod = PaymentMethod;
        this.PaymentStatus = PaymentStatus;
        this.BankAccNum = BankAccNum;
        this.DateToProcess = DateToProcess;
        this.DateTime = DateTime;
    }

    public String getWithdrawalId() {
        return WithdrawalId;
    }

    public void setWithdrawalId(String withdrawalId) {
        WithdrawalId = withdrawalId;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getPaymentType() {
        return PaymentType;
    }

    public void setPaymentType(String paymentType) {
        PaymentType = paymentType;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return PaymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        PaymentStatus = paymentStatus;
    }

    public String getBankAccNum() {
        return BankAccNum;
    }

    public void setBankAccNum(String bankAccNum) {
        BankAccNum = bankAccNum;
    }

    public String getDateToProcess() {
        return DateToProcess;
    }

    public void setDateToProcess(String dateToProcess) {
        DateToProcess = dateToProcess;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }
}

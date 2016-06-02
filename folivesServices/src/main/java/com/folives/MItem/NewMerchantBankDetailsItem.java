package com.folives.MItem;

/**
 * Created by ravikant on 22-04-2016.
 */
public class NewMerchantBankDetailsItem {

    String id, account_name, bank_name, acc_num, status;

    public NewMerchantBankDetailsItem(){

    }

    public NewMerchantBankDetailsItem(String id, String account_name, String bank_name, String acc_num, String status) {
        this.id = id;
        this.account_name = account_name;
        this.bank_name = bank_name;
        this.acc_num = acc_num;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getAcc_num() {
        return acc_num;
    }

    public void setAcc_num(String acc_num) {
        this.acc_num = acc_num;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

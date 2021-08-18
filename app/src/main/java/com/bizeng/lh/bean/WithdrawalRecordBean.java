package com.bizeng.lh.bean;

import java.math.BigDecimal;

public class WithdrawalRecordBean {
    /**
     * {
     * "searchValue": null,
     * "createBy": null,
     * "createTime": null,
     * "updateBy": null,
     * "updateTime": null,
     * "remark": null,
     * "params": {},
     * "withdrawalId": 27,
     * "withdrawalType": 1,
     * "userId": 3,
     * "address": null,
     * "businessId": null,
     * "tradeId": null,
     * "money": 1.0000,
     * "afterMoney": null,
     * "proceMoney": null,
     * "feeMoney": null,
     * "status": 3,
     * "coinType": null,
     * "errorMsg": null,
     * "autoExamine": 0,
     * "requestBy": null,
     * "creatTime": "2021-07-06 10:24:30"
     * }
     */

    public String searchValue;
    public String createBy;
    public String createTime;
    public String updateBy;
    public String updateTime;
    public String remark;
    public BigDecimal withdrawalId;
    public BigDecimal userId;
    public String address;
    public String businessId;
    public String tradeId;
    public BigDecimal money;
    public BigDecimal afterMoney;
    public BigDecimal proceMoney;
    public String feeMoney;
    public int status;//状态：-1-申请中；0-已审核；1-审核通过；2-审核不通过；3-兑换已到账
    public String coinType;
    public String errorMsg;
    public BigDecimal autoExamine;
    public BigDecimal requestBy;
    public String creatTime;
    public String withdrawalType;

    public String getWithdrawalTypeValue() {
        switch (withdrawalType) {
            default:
                return "提币";
            case "1":
                return "兑换点卡";
        }
    }


}

package com.bizeng.lh.bean;

import java.util.List;

public class HomePageBean {

    /**
     * banners : [{"searchValue":null,"createBy":null,"createTime":"2021-05-07 11:26:51","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":2,"imgSrc":"/file/statics/2021/05/07/14c3ff6b-f68e-4e25-91c0-883d1ae20c07.png","conHref":"https://baidu.com","orderNum":1},{"searchValue":null,"createBy":null,"createTime":"2021-05-07 15:50:55","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":3,"imgSrc":"/file/statics/2021/05/07/b0563b07-861e-421e-ba72-cc9c736e5d68.jpg","conHref":null,"orderNum":2},{"searchValue":null,"createBy":null,"createTime":"2021-05-07 15:53:21","updateBy":null,"updateTime":null,"remark":null,"params":{},"id":4,"imgSrc":"/file/statics/2021/05/07/d98ad19a-753d-469c-9a35-d5954315c33a.jpeg","conHref":"/file/statics/2021/05/07/e35e7cb0-5a98-42d6-94d2-0ec26ef787fd/index.html","orderNum":3}]
     * strategies : [{"searchValue":null,"createBy":null,"createTime":"2021-05-04 14:43:50","updateBy":null,"updateTime":"2021-05-07 11:31:40","remark":"策略长测","params":{},"strategyId":20,"title":"HT低买高卖策略","strategyConfig":"{\"in\":24.80, \"out\":24.91,\"subjectMatter\":\"htusdt\",\"type\":1,\"number\":6}","proposalCapacity":"1000","userGroups":"1","tag":"[\"低风险\",\"低收益\"]","annualized":"308%","url":"/file/statics/2021/05/04/bc7b7e17-6e90-413e-8cfe-ac4f4f6b2e65/index.html","isHot":1,"isNew":1,"isRecommend":1,"useNumber":8}]
     * informationList : [{"searchValue":null,"createBy":"admin","createTime":"2021-05-07 14:26:36","updateBy":null,"updateTime":null,"remark":null,"params":{},"informationId":1,"informationType":0,"isRecommend":1,"title":"资讯标题测试","coverSrc":"/file/statics/2021/05/07/f72e023d-aeff-49ad-bf00-97fbdfe5a59d.png","informationAbstract":"摘要摘要","conHref":"/file/statics/2021/05/07/31a81574-acbc-4380-8e6e-5c9967bc33f5/index.html","userGroups":"1,2"}]
     * notice : {"searchValue":null,"createBy":"admin","createTime":"2021-05-07 15:07:31","updateBy":null,"updateTime":null,"remark":null,"params":{},"informationId":2,"informationType":1,"isRecommend":0,"title":"公告标题测试","coverSrc":null,"informationAbstract":"公告","conHref":null,"userGroups":"1"}
     */

    public List<BannerBean> banners;
    public List<StrategyBean> strategies;
    public List<TutorialAreaBean> informationList;
    public TutorialAreaBean notice;

}

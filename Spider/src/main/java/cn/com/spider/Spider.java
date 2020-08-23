package cn.com.spider;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Spider implements Runnable{

    private static Logger logger = LoggerFactory.getLogger(Spider.class);
    private static final String BASE_URL="http://www.vegnet.com.cn/Price/List_ar";
    private static final int AREA_SIZE = 10000 ;
    private int pageNum;

    public Spider(int pageNum){
        this.pageNum = pageNum;
    }

    public void requestByPages(int page) throws IOException {
        Date y = new Date(System.currentTimeMillis()-1000*60*60*24);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String yesterday = sdf.format(y);
        int beginIndex = 110000 + page * AREA_SIZE;
        for (int j = 1; j < 15000; j++) {
            try{
                String linkurl = BASE_URL + beginIndex + "_p" + j + ".html?marketID=0";
                Document document = Jsoup.connect(linkurl).userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36")
                        .timeout(30000).get();
                String areaname = subString(document.select("body > div.box > div.current1.gap > p").toString(), document.select("body > div.box > div.current1.gap > p > a:nth-child(2)").toString(), " 价格行情");
                areaname = areaname.substring(6);
                Elements elements = document.select("body > div.box > div.main.gap > div > div.pri_k > p");
                boolean flag = false;
                for (Element element : elements) {
                    String time = element.getElementsByTag("span").get(0).text();
                    time = time.substring(1,time.length()-1);
                    String fTime = this.format(time);
                    if(!fTime.equals(yesterday)){
                        System.out.println(yesterday);
                        return;
                    }
                    flag = fTime.equals(yesterday);
                    String name = element.getElementsByTag("span").get(1).text();
                    String market = element.getElementsByTag("span").get(2).selectFirst("a").text();
                    String lowestPrice = element.getElementsByTag("span").get(3).text().substring(1);
                    String highestPrice = element.getElementsByTag("span").get(4).text().substring(1);
                    String avgPrice = element.getElementsByTag("span").get(5).text().substring(1);
                    StringBuilder str = new StringBuilder();
                    str.append(areaname).append(",").append(market).append(",").append(name).append(",")
                            .append(lowestPrice).append(",").append(highestPrice).append(",").append(avgPrice).append(",").append(fTime);
                    logger.info(str.toString());
                    System.out.println(str.toString()+"======================"+Thread.currentThread().getName());
                }
            }catch (Exception e){
                System.out.println(e.getMessage());
                return;
            }
        }
    }

    //匹配字符串
    public static String subString(String str, String strStart, String strEnd) {
        /* 找出指定的2个字符在 该字符串里面的 位置 */
        int strStartIndex = str.indexOf(strStart);
        int strEndIndex = str.indexOf(strEnd);

        /* index 为负数 即表示该字符串中 没有该字符 */
        if (strStartIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strStart + ", 无法截取目标字符串";
        }
        if (strEndIndex < 0) {
            return "字符串 :---->" + str + "<---- 中不存在 " + strEnd + ", 无法截取目标字符串";
        }
        /* 开始截取 */
        String result = str.substring(strStartIndex, strEndIndex).substring(strStart.length());
        return result;
    }

    public String format(String date){
        String[] ymds = date.split("-");
        String str = ymds[0];
        for (int i = 1;i<3;i++){
            if(ymds[i].length()<2){
                ymds[i] = "0"+ymds[i];
            }
            str = str + "-" + ymds[i];
        }
        return str;
    }

    @Override
    public void run() {
        try {
            this.requestByPages(pageNum);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static  void  main(String [] args) throws IOException, InterruptedException {
        ExecutorService pool = Executors.newCachedThreadPool();
        int i = 0;
        while(i<60) {
            Spider spider = new Spider(i);
            pool.submit(spider);
            i++;
        }
        pool.shutdown();
        if(!pool.awaitTermination(50,TimeUnit.SECONDS)){
            pool.shutdownNow();
            System.exit(0);
        }
    }
}

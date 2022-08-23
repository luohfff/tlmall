package com.tuling.tulingmall.promotion.service.impl;

import com.tuling.tulingmall.model.PmsProduct;
import com.tuling.tulingmall.promotion.domain.FlashPromotionProduct;
import com.tuling.tulingmall.promotion.service.HomePromotionService;
import com.tuling.tulingmall.promotion.service.ISecKillService;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

@Slf4j
@Service
public class SecKillServiceImpl implements ISecKillService {

    @Value("${seckill.templateDir}")
    private String templateDir;

    @Value("${seckill.templateName:seckill.ftl}")
    private String templateName;

    @Value("${seckill.htmlDir}")
    private String htmlDir;

    @Autowired
    private HomePromotionService homePromotionService;

    /*产品页面的静态化*/
    private String toStatic(FlashPromotionProduct flashPromotionProduct,long secKillId) throws IOException, TemplateException {
        String outPath = "";
        // 第一步：创建一个Configuration对象，直接new一个对象。构造方法的参数就是freemarker对于的版本号。
        Configuration configuration = new Configuration(Configuration.getVersion());
        // 第二步：设置模板文件所在的路径。
        configuration.setDirectoryForTemplateLoading(new File(templateDir));
        // 第三步：设置模板文件使用的字符集。一般就是utf-8.
        configuration.setDefaultEncoding("utf-8");
        // 第四步：加载一个模板，创建一个模板对象。
        Template template = configuration.getTemplate(templateName);
        // 第五步：创建一个模板使用的数据集，可以是pojo也可以是map。一般是Map。
        Map dataModel = new HashMap();
        // 向数据集中添加数据
        dataModel.put("item", flashPromotionProduct);

        String images = flashPromotionProduct.getPic();
        if (StringUtils.isNotEmpty(images)) {
            String[] split = images.split(",");
            List<String> imageList = Arrays.asList(split);
            dataModel.put("imageList", imageList);
        }
        // 第六步：创建一个Writer对象，一般创建一FileWriter对象，指定生成的文件名。
        outPath = htmlDir + "/seckill_" + secKillId + "_" + flashPromotionProduct.getId() + ".html";
        Writer out = new FileWriter(new File(outPath));
        // 第七步：调用模板对象的process方法输出文件。
        template.process(dataModel, out);
        // 第八步：关闭流。
        out.close();

        return outPath;

    }

    @Override
    public List<String> makeStaticHtml(long secKillId ) throws TemplateException, IOException {
        if(StringUtils.isEmpty(templateDir)){
            templateDir = System.getProperty("user.home") + "/template/ftl";
        }
        if(StringUtils.isEmpty(htmlDir)){
            templateDir = System.getProperty("user.home") + "/template/html";
        }
        //查询商品信息
        List<FlashPromotionProduct> flashPromotionProducts = homePromotionService.secKillContent(secKillId);
        List<String> result = new ArrayList<>();
        for(FlashPromotionProduct flashPromotionProduct : flashPromotionProducts){
            result.add(toStatic(flashPromotionProduct,secKillId));
        }
        return result;
    }

    @Override
    public void deployHtml() {

    }
}

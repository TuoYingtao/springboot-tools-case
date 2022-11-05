package com.tuoyingtao.easypoiexceltools.util;

import cn.hutool.json.JSONUtil;
import com.tuoyingtao.easypoiexceltools.entity.MemberEntity;
import com.tuoyingtao.easypoiexceltools.entity.OrderEntity;
import com.tuoyingtao.easypoiexceltools.entity.ProductEntity;
import com.tuoyingtao.easypoiexceltools.vo.MemberVo;
import io.micrometer.core.instrument.util.IOUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 假数据处理
 * @author TuoYingtao
 * @create 2022-10-30 12:04
 */
public class DataJsonUtil {

    private static List<OrderEntity> orderEntityList;
    private static List<MemberEntity> memberEntityList;
    private static List<ProductEntity> productEntityList;

    static  {
        orderEntityList = LocalJsonUtil.getListFromJson("json/orders.json", OrderEntity.class);
        memberEntityList = LocalJsonUtil.getListFromJson("json/members.json", MemberEntity.class);
        productEntityList = LocalJsonUtil.getListFromJson("json/products.json", ProductEntity.class);
    }

    /**
     * 会员列表
     * @return
     */
    public static List<MemberEntity> getMemberList() {
        return memberEntityList;
    }

    /**
     * 会员列表大数据
     * @return
     */
    public static List<MemberEntity> getMemberLarger() {
        String json = IOUtils.toString(Thread.currentThread().getContextClassLoader().getResourceAsStream("json/members2.json"), StandardCharsets.UTF_8);
        return LocalJsonUtil.getListFromJson("json/members2.json", MemberEntity.class);
    }

    /**
     * 商品列表
     * @return
     */
    public static List<ProductEntity> getProductList() {
        return productEntityList;
    }

    /**
     * 生产订单列表
     * @return
     */
    public static List<OrderEntity> getOrderList() {
        return orderEntityList.stream().map(order -> {
            if (productEntityList.size() != 0) {
                order.setProductEntityList(productEntityList);
            }
            if (memberEntityList.size() != 0) {
                order.setMemberEntity(memberEntityList.get((int) Math.round(Math.random() * (memberEntityList.size() - 1))));
            }
            return order;
        }).collect(Collectors.toList());
    }


    public static String createMemberLargerData(Integer num) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        List<MemberVo> memberVos = new ArrayList<>();
        Integer id = 10000;
        for (int i = 0; i < num; i++) {
            Long memberId = Long.parseLong(String.valueOf(id + 1));
            MemberVo memberVo = new MemberVo(memberId,
                    "测试" + i,
                    null,
                    "昵称" + i,
                    simpleDateFormat.format(new Date()), "15875555557", null, (i == 0) || (i % 2) == 0 ? "0" : "1");
            memberVos.add(memberVo);
        }
        String json = JSONUtil.toJsonStr(memberVos);
        String formatJson = JSONUtil.formatJsonStr(json);
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream("F:\\Material\\tyt\\springboot-tools-case\\easypoi-excel-tools\\src\\main\\resources\\json\\members2.json");
            fileOutputStream.write(formatJson.getBytes(StandardCharsets.UTF_8));
            return "成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "失败";
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

package com.common.core.utils.geo;

/**
 * 经纬度计算工具
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-01 16:09
 * @Version: v1.0.0
 */
public class GeoUtils {

    /**
     * 地球半径，单位为千米
     */
    private static final Double EARTH_RADIUS = 6371.0;

    /**
     * 简单的计算两个经纬度之间的距离。
     * 使用 Haversine 公式计算两个经纬度之间的距离时，我们需要首先将经纬度从度数转换为弧度，然后根据公式进行计算
     * 在实际项目中请使用 JTS、GeoTools等相关的地理空间计算库
     * @param lat1 纬度1
     * @param lon1 经度1
     * @param lat2 纬度2
     * @param lon2 经度2
     * @return
     */
    public static Double calculateDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        // 表示纬度之差，将纬度差转换为弧度
        Double dLat = Math.toRadians(lat2 - lat1);
        // 表示经度之差，将经度差转换为弧度。
        Double dLon = Math.toRadians(lon2 - lon1);
        // Math.sin(dLat / 2) * Math.sin(dLat / 2)：计算纬度差的正弦平方
        Double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                // 计算纬度和经度之间的乘积
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        // 计算正弦平方的平方根之比的反正切
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        // 转换为米
        Double distance = EARTH_RADIUS * c * 1000;
        return distance;
    }
}

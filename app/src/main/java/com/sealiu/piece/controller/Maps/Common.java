package com.sealiu.piece.controller.Maps;

/**
 * Created by liuyang
 * on 2016/7/13.
 */
public class Common {

    private static final double PI = 3.14159265;
    private static final double CIRCUMFERENCE = 40074274.944; //地球周长
    private static final double EARTH_RADIUS = 6378137; //地球半径
    private static final double RAD = Math.PI / 180.0;

    /**
     * @param lat    中心lat
     * @param lng    中心lng
     * @param radius 半径（米）
     * @return 经纬度范围
     */
    public static double[] GetAround(double lat, double lng, int radius) {

        Double latitude = lat;
        Double longitude = lng;

        Double degree = CIRCUMFERENCE / 360;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * (double) radius;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * (double) radius;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        return new double[]{minLat, minLng, maxLat, maxLng};
    }

    /**
     * 用 haversine 公式计算球面两点间的距离
     */

    public static double GetDistance(double lat1, double lng1, double lat2, double lng2) {

        Double latDistance = toRad(lat2 - lat1);
        Double lonDistance = toRad(lng2 - lng1);
        Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
                Math.cos(toRad(lat1)) * Math.cos(toRad(lat2)) *
                        Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS * c;
    }

    private static Double toRad(Double value) {
        return value * RAD;
    }

}

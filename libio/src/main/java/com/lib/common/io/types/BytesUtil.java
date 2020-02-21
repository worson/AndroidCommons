package com.lib.common.io.types;

/**
 * 说明:
 *
 * @author wangshengxing  02.21 2020
 */
public class BytesUtil {
    /**
     * byte转string
     *
     * @return
     */
    public static String bytesToHex(byte[] b) {

        String stmp = "";
        StringBuilder sb = new StringBuilder("");
        for (int n = 0; n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0xFF);
            sb.append((stmp.length() == 1) ? "0" + stmp : stmp);
            sb.append(" ");
        }
        return sb.toString().toUpperCase().trim();
    }

}

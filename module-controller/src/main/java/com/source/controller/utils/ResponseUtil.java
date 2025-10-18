package com.source.controller.utils;

import com.source.controller.enums.ResponseCode;
import com.source.controller.model.CommonResponse;

public class ResponseUtil<T> {
    private final CommonResponse<T> commonResponse;

    private static final Integer SUCCESS_CODE = 200;

    /**
     * Phương thức khởi tạo, đặt giá trị mặc định cho kết quả phản hồi
     */
    public ResponseUtil() {
        commonResponse = new CommonResponse<>();
        commonResponse.setSuccess(true);
        commonResponse.setCode(SUCCESS_CODE);
        commonResponse.setMessage("success");
    }

    /**
     * Trả về dữ liệu
     *
     * @param data Kiểu dữ liệu
     * @return Thông báo
     */

    public CommonResponse<T> setData(T data) {
        commonResponse.setResult(data);
        return this.commonResponse;
    }

    /**
     * Trả về thông báo thành công
     *
     * @param responseCode Mã trả về
     * @return Trả về thông báo thành công
     */

    public CommonResponse<T> setSuccessMsg(ResponseCode responseCode) {
        this.commonResponse.setSuccess(true);
        this.commonResponse.setMessage(responseCode.message());
        this.commonResponse.setCode(responseCode.code());
        return this.commonResponse;
    }

    /**
     * Phương thức tĩnh trừu tượng, trả về tập kết quả
     *
     * @param data Kiểu dữ liệu
     * @param <T>  Kiểu dữ liệu
     * @return Thông báo
     */
    public static <T> CommonResponse<T> data(T data) {
        return new ResponseUtil<T>().setData(data);
    }

}

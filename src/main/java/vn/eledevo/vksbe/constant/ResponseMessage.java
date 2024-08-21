package vn.eledevo.vksbe.constant;

public class ResponseMessage {

    private ResponseMessage() {}

    public static final String USER_EXIST = "Người dùng đã tồn tại";

    public static final String EMAIL_EXIST = "Email đã tồn tại";

    public static final String USER_BLANK = "Tên đăng nhập không được để trống";

    public static final String USER_SIZE = "Tên đăng nhập phải có độ dài từ 3 đến 50 ký tự";

    public static final String DEVICE_EXIST = "Thiết bị đã tồn tại";

    public static final String DEVICE_INFO_BLANK = "Tên thiết bị không được để trống";

    public static final String DEVICE_INFO_UUID_BLANK = "Mã UUID thiết bị không được để trống";

    public static final String USER_IDENTIFICATION_NUMBER_INVALID = "Số CMND/CCCD không hợp lệ hoặc đã tồn tại";

    public static final String ACCOUNT_LOCKED = "Tài khoản của bạn đã bị khóa!";

    public static final String ROOM_TYPE_EXISTED = "Đã có loại phòng này!";

    public static final String BOOKING_EXISTS =
            "Lịch này đã có khách đặt hoặc phòng đã được đặt/sử dụng! Vui lòng chin thời gian khác";

    public static final String BOOKING_EXISTS_IN_RANGE =
            "Thời gian hoặc phòng bạn chọn đã được nguời khác đặt trước! Vui lòng lựa chọn lại!";
}

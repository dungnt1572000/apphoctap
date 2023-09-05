package com.example.apphoctap.model

class UserDetail constructor(
    val name: String = "",
    val point: Int = 0
) {
    // Các thuộc tính và phương thức khác

    // Constructor không tham số
    constructor() : this("", 0) {
        // Khởi tạo các giá trị mặc định nếu cần
    }
}